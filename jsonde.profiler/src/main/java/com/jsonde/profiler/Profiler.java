package com.jsonde.profiler;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.api.methodCall.MethodCallSummaryDto;
import com.jsonde.profiler.network.NetworkServerException;
import com.jsonde.util.log.Log;

import java.lang.instrument.Instrumentation;
import java.util.Collection;
import java.util.List;

public abstract class Profiler {

    public static final String CLASS_CANONICAL_NAME = Profiler.class.getCanonicalName();

    private static final Log log = Log.getLog(Profiler.class);

    public static final long UNDEFINED_CLASS_ID = -1;
    public static final long UNDEFINED_METHOD_ID = -1;

    private static volatile Profiler profiler;

    public static Profiler initializeProfiler(Instrumentation instrumentation, int port) {
        profiler = new ProfilerImpl(instrumentation, port);
        return profiler;
    }

    public static void initializeProfiler(Profiler profiler) {
        Profiler.profiler = profiler;
    }

    public static Profiler getProfiler() {
        if (null == profiler) {
            profiler = new ProfilerImpl();
        }
        return profiler;
    }

    public static void enterMethod(long methodId) {
        getProfiler().enterMethodImpl(methodId);
    }

    public static final String ENTER_CONSTRUCTOR_METHOD_NAME =
            "enterConstructor";
    public static final String ENTER_CONSTRUCTOR_METHOD_DESCRIPTOR =
            "(JLjava/lang/Object;[Ljava/lang/Object;)V";
    private static final String ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR =
            ENTER_CONSTRUCTOR_METHOD_NAME + ENTER_CONSTRUCTOR_METHOD_DESCRIPTOR;

    public static final String LEAVE_METHOD_METHOD_NAME =
            "leaveMethod";
    public static final String LEAVE_METHOD_METHOD_DESCRIPTOR =
            "()V";
    public static final String LEAVE_METHOD_METHOD_NAME_WITH_DESCRIPTOR =
            LEAVE_METHOD_METHOD_NAME + LEAVE_METHOD_METHOD_DESCRIPTOR;

    public void start() throws NetworkServerException {

        Runtime.getRuntime().addShutdownHook(new Thread(
                new Runnable() {

                    public void run() {

                        int threadsCount = Thread.activeCount();
                        Thread[] threads = new Thread[threadsCount];
                        Thread.enumerate(threads);

                        try {

                            for (int i = 0; i < 1000; i++) {
                                Thread.yield();
                            }

                            for (Thread thread : threads) {

                                if (Thread.currentThread() == thread || null == thread)
                                    continue;

                                if (thread.isAlive()) {
                                    thread.join(250);
                                }

                            }

                            if (Thread.activeCount() > 1) {
                                Thread.sleep(500);
                            }

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            log.error("Profiler Shutdown Hook", e);
                        }

                        try {
                            stop();
                        } catch (NetworkServerException e) {
                            log.error("Profiler Shutdown Hook", e);
                        }
                    }
                }
        ));

        startServer();
    }

    public void stop() throws NetworkServerException {
        stopServer();
    }

    public static void describeClass(long methodId, Class clazz) {
        getProfiler().describeClassImpl(methodId, clazz);
    }

    public static void enterConstructor(long methodId, Object that) {
        getProfiler().enterConstructorImpl(methodId, that);
    }

    protected void describeClassImpl(long methodId, Class clazz) {};

    public void enterMethodImpl(long methodId) {};

    protected void enterConstructorImpl(long methodId, Object that) {};

    public static void leaveMethod() {
        getProfiler().leaveMethodImpl(null);
    }

    public static void leaveMethod(Throwable e) {
        getProfiler().leaveMethodImpl(e);
    }

    public void leaveMethodImpl(Throwable e) {};

    public abstract void sendMessage(Message registerMethodMessage);

    protected abstract void processMethodCall(
            List<MethodCallDto> methodCallDtos,
            MethodCallSummaryDto methodCallSummaryDto,
            boolean complete);

    public abstract long registerMethod(
            long classId,
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions);

    public abstract long generateClassId(ClassLoader classLoader, String className);

    public abstract long generateClassIdAndRegisterIfAbsent(Class clazz);

    public abstract long registerClass(
            int version,
            int access,
            String name,
            String signature,
            String superName,
            String[] interfaces,
            ClassLoader classLoader);

    public abstract void startServer() throws NetworkServerException;

    public abstract void stopServer() throws NetworkServerException;

    public abstract void addMessageListener(MessageListener messageListener);

    public abstract void removeMessageListener(MessageListener messageListener);

    public abstract void redefineClass(byte[] bytecode, String className, ClassLoader classLoader);

    public abstract Collection<Long> getProfilerThreadIds();

    public abstract long generateMethodCallId();

}
