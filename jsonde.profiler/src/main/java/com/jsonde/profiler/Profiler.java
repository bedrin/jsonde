package com.jsonde.profiler;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.api.methodCall.MethodCallSummaryDto;
import com.jsonde.profiler.network.NetworkServerException;
import com.jsonde.util.CurrentClassGetter;
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

    public static final String ENTER_METHOD_METHOD_NAME =
            "enterMethod";
    public static final String ENTER_METHOD_METHOD_DESCRIPTOR =
            "(JLjava/lang/Object;[Ljava/lang/Object;)V";
    private static final String ENTER_METHOD_METHOD_NAME_WITH_DESCRIPTOR =
            ENTER_METHOD_METHOD_NAME + ENTER_METHOD_METHOD_DESCRIPTOR;

    public static void enterMethod(long methodId, Object object, Object[] arguments) {

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        ENTER_METHOD_METHOD_NAME_WITH_DESCRIPTOR,
                        methodId,
                        object,
                        arguments
                );
            }

            getProfiler().enterMethodImpl(methodId, object, arguments);

            log.exiting(ENTER_METHOD_METHOD_NAME_WITH_DESCRIPTOR);

        } catch (Throwable e) {
            log.error(ENTER_METHOD_METHOD_NAME_WITH_DESCRIPTOR, e);
        } finally {
            log.exiting(ENTER_METHOD_METHOD_NAME_WITH_DESCRIPTOR);
        }

    }

    public static final String ENTER_CONSTRUCTOR_METHOD_NAME =
            "enterConstructor";
    public static final String ENTER_CONSTRUCTOR_METHOD_DESCRIPTOR =
            "(JLjava/lang/Object;[Ljava/lang/Object;)V";
    private static final String ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR =
            ENTER_CONSTRUCTOR_METHOD_NAME + ENTER_CONSTRUCTOR_METHOD_DESCRIPTOR;

    public static void enterConstructor(long methodId, Object object, Object[] arguments) {

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR,
                        methodId,
                        object,
                        arguments
                );
            }

            getProfiler().enterConstructorImpl(methodId, object, arguments);

            log.exiting(ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR);

        } catch (Throwable e) {
            log.error(ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR, e);
        } finally {
            log.exiting(ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR);
        }

    }

    public static final String PRE_ENTER_CONSTRUCTOR_METHOD_NAME =
            "preEnterConstructor";
    public static final String PRE_ENTER_CONSTRUCTOR_METHOD_DESCRIPTOR =
            "(J)V";
    public static final String PRE_ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR =
            PRE_ENTER_CONSTRUCTOR_METHOD_NAME + PRE_ENTER_CONSTRUCTOR_METHOD_DESCRIPTOR;

    public static void preEnterConstructor(long methodId) {

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        PRE_ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR,
                        methodId
                );
            }

            getProfiler().preEnterConstructorImpl(methodId);

            log.exiting(PRE_ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR);

        } catch (Throwable e) {
            log.error(PRE_ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR, e);
        } finally {
            log.exiting(PRE_ENTER_CONSTRUCTOR_METHOD_NAME_WITH_DESCRIPTOR);
        }

    }

    public static final String LEAVE_METHOD_METHOD_NAME =
            "leaveMethod";
    public static final String LEAVE_METHOD_METHOD_DESCRIPTOR =
            "()V";
    public static final String LEAVE_METHOD_METHOD_NAME_WITH_DESCRIPTOR =
            LEAVE_METHOD_METHOD_NAME + LEAVE_METHOD_METHOD_DESCRIPTOR;

    public static void leaveMethod() {

        try {

            if (log.isTraceEnabled()) {

                log.entering(LEAVE_METHOD_METHOD_NAME_WITH_DESCRIPTOR);
            }

            getProfiler().leaveMethodImpl(true, false, null);

            log.exiting(LEAVE_METHOD_METHOD_NAME_WITH_DESCRIPTOR);

        } catch (Throwable e) {
            e.printStackTrace();
            log.error(LEAVE_METHOD_METHOD_NAME_WITH_DESCRIPTOR, e);
        } finally {
            log.exiting(LEAVE_METHOD_METHOD_NAME_WITH_DESCRIPTOR);
        }

    }

    public static final String LEAVE_METHOD_RETURN_VALUE_METHOD_NAME =
            "leaveMethodReturnValue";
    public static final String LEAVE_METHOD_RETURN_VALUE_METHOD_DESCRIPTOR =
            "(Ljava/lang/Object;)V";
    public static final String LEAVE_METHOD_RETURN_VALUE_METHOD_NAME_WITH_DESCRIPTOR =
            LEAVE_METHOD_RETURN_VALUE_METHOD_NAME + LEAVE_METHOD_RETURN_VALUE_METHOD_DESCRIPTOR;

    public static void leaveMethodReturnValue(Object returnValue) {

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        LEAVE_METHOD_RETURN_VALUE_METHOD_NAME_WITH_DESCRIPTOR,
                        returnValue
                );
            }

            getProfiler().leaveMethodImpl(false, false, returnValue);

            log.exiting(LEAVE_METHOD_RETURN_VALUE_METHOD_NAME_WITH_DESCRIPTOR);

        } catch (Throwable e) {
            log.error(LEAVE_METHOD_RETURN_VALUE_METHOD_NAME_WITH_DESCRIPTOR, e);
        } finally {
            log.exiting(LEAVE_METHOD_RETURN_VALUE_METHOD_NAME_WITH_DESCRIPTOR);
        }

    }

    public static final String LEAVE_METHOD_THROW_EXCEPTION_METHOD_NAME =
            "leaveMethodThrowException";
    public static final String LEAVE_METHOD_THROW_EXCEPTION_METHOD_DESCRIPTOR =
            "(Ljava/lang/Throwable;)V";
    public static final String LEAVE_METHOD_THROW_EXCEPTION_METHOD_NAME_WITH_DESCRIPTOR =
            LEAVE_METHOD_THROW_EXCEPTION_METHOD_NAME + LEAVE_METHOD_THROW_EXCEPTION_METHOD_DESCRIPTOR;

    public static void leaveMethodThrowException(Throwable throwable) {

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        LEAVE_METHOD_THROW_EXCEPTION_METHOD_NAME_WITH_DESCRIPTOR,
                        throwable
                );
            }

            getProfiler().leaveMethodImpl(false, true, throwable);

            log.exiting(LEAVE_METHOD_THROW_EXCEPTION_METHOD_NAME_WITH_DESCRIPTOR);

        } catch (Throwable e) {
            log.error(LEAVE_METHOD_THROW_EXCEPTION_METHOD_NAME_WITH_DESCRIPTOR, e);
        } finally {
            log.exiting(LEAVE_METHOD_THROW_EXCEPTION_METHOD_NAME_WITH_DESCRIPTOR);
        }

    }

    public static final String DESCRIBE_CLASS_METHOD_NAME =
            "describeClass";
    public static final String DESCRIBE_CLASS_METHOD_DESCRIPTOR =
            "(J)V";
    public static final String DESCRIBE_CLASS_METHOD_NAME_WITH_DESCRIPTOR =
            DESCRIBE_CLASS_METHOD_NAME + DESCRIBE_CLASS_METHOD_DESCRIPTOR;

    public static void describeClass(
            final long methodId) {

        Class clazz = new CurrentClassGetter().getCallerClass(1);

        try {

            if (log.isTraceEnabled()) {

                log.entering(
                        DESCRIBE_CLASS_METHOD_NAME_WITH_DESCRIPTOR,
                        methodId,
                        clazz
                );
            }

            getProfiler().describeClassImpl(methodId, clazz);

            log.exiting(DESCRIBE_CLASS_METHOD_NAME_WITH_DESCRIPTOR);

        } catch (Throwable e) {
            log.error(DESCRIBE_CLASS_METHOD_NAME_WITH_DESCRIPTOR, e);
        } finally {
            log.exiting(DESCRIBE_CLASS_METHOD_NAME_WITH_DESCRIPTOR);
        }

    }

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

    protected abstract void describeClassImpl(long methodId, Class clazz);

    public abstract void describeRedefinableClass(long classId, Class clazz);

    protected abstract void enterMethodImpl(long methodId, Object object, Object[] arguments);

    protected abstract void enterConstructorImpl(long methodId, Object object, Object[] arguments);

    protected abstract void preEnterConstructorImpl(long methodId);

    protected abstract void leaveMethodImpl(boolean isVoid, boolean isThrowsException, Object result);

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
