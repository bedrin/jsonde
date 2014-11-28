package com.jsonde.profiler;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.api.function.FunctionRequest;
import com.jsonde.api.function.FunctionResponse;
import com.jsonde.api.function.echo.EchoFunctionRequest;
import com.jsonde.api.function.echo.EchoFunctionResponse;
import com.jsonde.api.function.heap.ClassHeapDataDto;
import com.jsonde.api.function.heap.DumpHeapFunctionRequest;
import com.jsonde.api.function.heap.DumpHeapFunctionResponse;
import com.jsonde.api.methodCall.*;
import com.jsonde.profiler.heap.ClassHeapData;
import com.jsonde.profiler.heap.HeapAnalyzer;
import com.jsonde.profiler.network.NetworkServer;
import com.jsonde.profiler.network.NetworkServerException;
import com.jsonde.profiler.network.NetworkServerImpl;
import com.jsonde.profiler.telemetry.TelemetryDataProvider;
import com.jsonde.util.ClassUtils;
import com.jsonde.util.ObjectIdGenerator;
import com.jsonde.util.ObjectIsAbsentException;
import com.jsonde.util.log.Log;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ProfilerImpl extends Profiler implements MessageListener {

    private static final Log log = Log.getLog(ProfilerImpl.class);

    private Set<ThreadLocalProfiler> threadLocalProfilers = Collections.synchronizedSet(
            new HashSet<ThreadLocalProfiler>()
    );

    private final ThreadLocal<ThreadLocalProfiler> threadLocalProfiler =
            new ThreadLocal<ThreadLocalProfiler>() {

                @Override
                protected synchronized ThreadLocalProfiler initialValue() {
                    ThreadLocalProfiler threadLocalProfiler = new ThreadLocalProfiler(ProfilerImpl.this);
                    threadLocalProfilers.add(threadLocalProfiler);
                    return threadLocalProfiler;
                }

            };

    private final Instrumentation instrumentation;

    private final AtomicLong methodCallIdGenerator = new AtomicLong();
    private final AtomicLong methodIdGenerator = new AtomicLong();

    private final ObjectIdGenerator<ClassLoader> classLoaderIdGenerator =
            new ObjectIdGenerator<ClassLoader>();

    private final ObjectIdGenerator<ObjectIdGenerator.Pair<String, ClassLoader>> classIdGenerator =
            new ObjectIdGenerator<ObjectIdGenerator.Pair<String, ClassLoader>>();

    private NetworkServer networkServer;
    private HeapAnalyzer heapAnalyzer;

    private DaemonThreadFactory daemonThreadFactory = new DaemonThreadFactory();
    private Executor redefineClassesExecutor = Executors.newSingleThreadExecutor(daemonThreadFactory);
    private Executor invokeFunctionExecutor = Executors.newCachedThreadPool(daemonThreadFactory);

    private ScheduledExecutorService telemetryExecutor = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);

    private Set<Class> redefinedClasses = new HashSet<Class>();

    public ProfilerImpl(Instrumentation instrumentation, int port) {
        this.instrumentation = instrumentation;
        networkServer = new NetworkServerImpl(port, daemonThreadFactory);
        heapAnalyzer = new HeapAnalyzer(daemonThreadFactory);
        networkServer.addMessageListener(this);
    }

    public ProfilerImpl() {
        instrumentation = null;
    }

    protected NetworkServer getServer() {
        return networkServer;
    }

    protected void setServer(NetworkServer networkServer) {
        this.networkServer = networkServer;
    }

    public void onMessage(Message message) {

        if (message instanceof FunctionRequest) {

            final FunctionRequest functionRequest = (FunctionRequest) message;

            invokeFunctionExecutor.execute(new Runnable() {

                public void run() {
                    FunctionResponse functionResponse = invokeFunction(functionRequest);
                    if (null != functionResponse) {
                        networkServer.sendMessage(functionResponse);
                    }
                }

            });

            for (ThreadLocalProfiler tlp : threadLocalProfilers) {
                tlp.dump();
            }

        }

    }

    private FunctionResponse invokeFunction(FunctionRequest functionRequest) {

        if (functionRequest instanceof EchoFunctionRequest) {
            return new EchoFunctionResponse((EchoFunctionRequest) functionRequest);
        } else if (functionRequest instanceof DumpHeapFunctionRequest) {

            DumpHeapFunctionResponse functionResponse = new DumpHeapFunctionResponse(functionRequest);

            for (Map.Entry<Long, ClassHeapData> mapEntry : heapAnalyzer.getHeapData().entrySet()) {
                functionResponse.addClassHeapDataDto(new ClassHeapDataDto(
                        mapEntry.getKey(),
                        mapEntry.getValue().getCreateCounter(),
                        mapEntry.getValue().getCollectCounter(),
                        mapEntry.getValue().getTotalCurrentSize()
                ));
            }

            return functionResponse;

        }

        return null;

    }

    @Override
    protected void enterMethodImpl(long methodId, Object object, Object[] arguments) {
        threadLocalProfiler.get().enterMethodImpl(methodId, object, arguments);
    }

    @Override
    protected void enterConstructorImpl(long methodId, Object object, Object[] arguments) {
        threadLocalProfiler.get().enterConstructorImpl(methodId, object, arguments);
        long objectSize = instrumentation.getObjectSize(object);
        heapAnalyzer.createObject(object, methodId, objectSize);
    }

    @Override
    protected void preEnterConstructorImpl(long methodId) {
        threadLocalProfiler.get().preEnterConstructorImpl(methodId);
    }

    @Override
    protected void leaveMethodImpl(boolean isVoid, boolean isThrowsException, Object result) {
        threadLocalProfiler.get().leaveMethodImpl(isVoid, isThrowsException, result);
    }

    @Override
    public long registerMethod(
            final long classId,
            final int access,
            final String name,
            final String desc,
            final String signature,
            final String[] exceptions) {

        final String METHOD_NAME = "registerMethod";

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        METHOD_NAME,
                        classId,
                        access,
                        name,
                        desc,
                        signature,
                        Arrays.toString(exceptions)
                );
            }

            final long methodId = methodIdGenerator.getAndIncrement();

            RegisterMethodMessage registerMethodMessage = new RegisterMethodMessage(
                    methodId, classId, access, name, desc, signature, exceptions
            );

            sendMessage(registerMethodMessage);

            log.exiting(METHOD_NAME, methodId);

            return methodId;

        } catch (Throwable e) {
            log.error(METHOD_NAME, e);
            return UNDEFINED_METHOD_ID;
        } finally {
            log.exiting(METHOD_NAME);
        }

    }

    public void sendMessage(Message registerMethodMessage) {
        networkServer.sendMessage(registerMethodMessage);
    }

    @Override
    public long registerClass(
            final int version,
            final int access,
            final String name,
            final String signature,
            final String superName,
            final String[] interfaces,
            final ClassLoader classLoader) {

        final String METHOD_NAME = "registerClass";

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        METHOD_NAME,
                        version,
                        access,
                        name,
                        signature,
                        superName,
                        Arrays.toString(interfaces),
                        classLoader
                );
            }

            String className = ClassUtils.getFullyQualifiedName(name);

            final long classId = generateClassId(classLoader, className);

            RegisterClassMessage registerClassMessage = new RegisterClassMessage(
                    classId, version, access, className, signature, superName, interfaces
            );

            networkServer.sendMessage(registerClassMessage);

            log.exiting(METHOD_NAME, classId);

            return classId;

        } catch (Throwable e) {
            e.printStackTrace();
            log.error(METHOD_NAME, e);
            return UNDEFINED_CLASS_ID;
        } finally {
            log.exiting(METHOD_NAME);
        }

    }

    public long generateClassId(ClassLoader classLoader, String className) {
        return classIdGenerator.getId(ObjectIdGenerator.pair(className, classLoader));
    }

    public long generateClassIdAndRegisterIfAbsent(Class clazz) {

        ClassLoader classLoader = clazz.getClassLoader();
        String className = clazz.getName();

        try {
            return classIdGenerator.pollId(ObjectIdGenerator.pair(className, classLoader));
        } catch (ObjectIsAbsentException e) {

            long classId = generateClassId(classLoader, className);

            RegisterClassMessage registerClassMessage = new RegisterClassMessage(classId, className);

            networkServer.sendMessage(registerClassMessage);

            describeClassImpl(classId, clazz);

            return classId;

        }

    }

    @Override
    protected void describeClassImpl(long classId, Class clazz) {

        DescribeClassMessage describeClassMessage = new DescribeClassMessage(classId, false);

        describeClassMessage.setClassLoaderId(
                classLoaderIdGenerator.getId(clazz.getClassLoader())
        );

        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        if (null != protectionDomain) {
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (null != codeSource) {
                URL codeLocation = codeSource.getLocation();
                describeClassMessage.setCodeLocation(codeLocation.toExternalForm());
            }
        }

        networkServer.sendMessage(describeClassMessage);

    }

    @Override
    public void describeRedefinableClass(long classId, Class clazz) {

        DescribeClassMessage describeClassMessage = new DescribeClassMessage(classId, true);

        describeClassMessage.setClassLoaderId(
                classLoaderIdGenerator.getId(clazz.getClassLoader())
        );

        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        if (null != protectionDomain) {
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (null != codeSource) {
                URL codeLocation = codeSource.getLocation();
                describeClassMessage.setCodeLocation(codeLocation.toExternalForm());
            }
        }

        networkServer.sendMessage(describeClassMessage);

    }

    @Override
    protected void processMethodCall(
            List<MethodCallDto> methodCallDtos,
            MethodCallSummaryDto methodCallSummaryDto,
            boolean complete) {

        MethodCallMessage methodCallMessage = new MethodCallMessage(
                methodCallDtos,
                methodCallSummaryDto,
                complete);

        networkServer.sendMessage(methodCallMessage);

    }

    @Override
    public void startServer() throws NetworkServerException {
        networkServer.start();
        heapAnalyzer.start();
        TelemetryDataProvider telemetryDataProvider = new TelemetryDataProvider(this);
        telemetryExecutor.scheduleWithFixedDelay(telemetryDataProvider, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stopServer() throws NetworkServerException {
        networkServer.stop();
        heapAnalyzer.stop();
        telemetryExecutor.shutdown();
        try {
            telemetryExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("stopServer", e);
            throw new NetworkServerException(e);
        }
    }

    public void addMessageListener(MessageListener messageListener) {
        networkServer.addMessageListener(messageListener);
    }

    public void removeMessageListener(MessageListener messageListener) {
        networkServer.removeMessageListener(messageListener);
    }

    public void redefineClass(
            final byte[] bytecode,
            final String className,
            final ClassLoader classLoader) {

        redefineClassesExecutor.execute(new Runnable() {

            public void run() {
                try {
                    Class clazz;

                    if (null == classLoader) {
                        clazz = ClassLoader.getSystemClassLoader().loadClass(className);
                    } else {
                        clazz = classLoader.loadClass(className);
                    }

                    if (redefinedClasses.contains(clazz)) return;

                    redefinedClasses.add(clazz);

                    ClassDefinition classDefinition = new ClassDefinition(clazz, bytecode);

                    instrumentation.redefineClasses(new ClassDefinition[]{classDefinition});

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public Collection<Long> getProfilerThreadIds() {

        ThreadGroup threadGroup = daemonThreadFactory.getThreadGroup();

        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);

        Collection<Long> profilerThreadIds = new HashSet<Long>();

        for (Thread thread : threads) {
            profilerThreadIds.add(thread.getId());
        }

        return profilerThreadIds;
    }

    @Override
    public long generateMethodCallId() {
        return methodCallIdGenerator.getAndIncrement();
    }

}
