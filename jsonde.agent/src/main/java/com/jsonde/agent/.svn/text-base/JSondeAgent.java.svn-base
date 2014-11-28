package com.jsonde.agent;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.api.configuration.AgentConfigurationMessage;
import com.jsonde.api.configuration.ClassFilterDto;
import com.jsonde.profiler.Profiler;
import com.jsonde.profiler.network.NetworkServerException;
import com.jsonde.util.ClassUtils;
import com.jsonde.util.StringUtils;
import com.jsonde.util.io.IO;
import com.jsonde.util.log.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.ProtectionDomain;

public class JSondeAgent implements MessageListener, ClassFileTransformer {

    private final static Log log = Log.getLog(JSondeAgent.class);

    private static final int DEFAULT_PORT_NUMBER = 60001;

    private final ClassFileTransformer byteCodeTransformer;
    private final Profiler profiler;

    private final String arguments;
    private final Instrumentation instrumentation;

    private final ClassLoader resolveAgentLibrariesClassLoader;

    public static void premain(final String arg, Instrumentation instrumentation) {
        JSondeAgent jSondeAgent = new JSondeAgent(arg, instrumentation);
        jSondeAgent.execute();
        jSondeAgent.setTransformer();
    }

    @SuppressWarnings("unused")
    public static void agentmain(String arg, final Instrumentation instrumentation) {
        final JSondeAgent jSondeAgent = new JSondeAgent(arg, instrumentation);
        new Thread(new Runnable() {

            public void run() {
                jSondeAgent.execute();
                jSondeAgent.redefineLoadedClasses();
                jSondeAgent.setTransformer();
                /*try {
                    if (instrumentation.isRetransformClassesSupported()) {
                        jSondeAgent.setTransformer();
                        Class[] classes = instrumentation.getAllLoadedClasses();

                        List<Class> modifiableClasses = new ArrayList<Class>(classes.length / 2);

                        for (Class clazz : classes) {
                            if (instrumentation.isModifiableClass(clazz)) {
                                modifiableClasses.add(clazz);
                            }
                        }

                        int classesCount = modifiableClasses.size();
                        int chunkSize = 100;
                        int chunkCount = classesCount / chunkSize;

                        for (int i = 0; i < chunkCount; i++) {

                            Class[] classesChunk =
                                    modifiableClasses.
                                            subList(i * chunkSize, (i+1) * chunkSize).
                                            toArray(new Class[chunkSize]);

                            instrumentation.retransformClasses(classesChunk);

                        }

                        Class[] classesChunk =
                                    modifiableClasses.
                                            subList(classesCount - (classesCount % chunkSize), classesCount).
                                            toArray(new Class[chunkSize]);

                        instrumentation.retransformClasses(classesChunk);

                    } else {
                        jSondeAgent.redefineLoadedClasses();
                        jSondeAgent.setTransformer();
                    }
                } catch (NoSuchMethodError e) {
                    jSondeAgent.redefineLoadedClasses();
                    jSondeAgent.setTransformer();
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }*/

            }

        }).start();
    }

    public JSondeAgent(String arguments, Instrumentation instrumentation) {

        System.out.println("jSonde agent started");

        this.arguments = arguments;
        this.instrumentation = instrumentation;

        ResolveAgentLibrariesClassLoader resolveAgentLibrariesClassLoader = new ResolveAgentLibrariesClassLoader();

        this.resolveAgentLibrariesClassLoader = resolveAgentLibrariesClassLoader;

        byteCodeTransformer = createByteCodeTransformer();

        int portNumber = getPortNumber();

        profiler = Profiler.initializeProfiler(instrumentation, portNumber);
    }

    private int getPortNumber() {
        try {
            return Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            return DEFAULT_PORT_NUMBER;
        }
    }

    private ClassFileTransformer createByteCodeTransformer() {
        try {
            return (ClassFileTransformer)
                    resolveAgentLibrariesClassLoader.
                            loadClass("com.jsonde.instrumentation.ByteCodeTransformer").
                            newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void execute() {

        final String METHOD_NAME = "execute()";

        try {
            startServer();
        } catch (NetworkServerException e) {
            log.error(METHOD_NAME, e);
        }

    }

    public void setTransformer() {
        instrumentation.addTransformer(this);
    }

    private void redefineLoadedClasses() {
        for (Class clazz : instrumentation.getAllLoadedClasses()) {

            try {
                redefineLoadedClass(clazz);
            } catch (Exception e) {
                System.out.println("Error while transforming class " + clazz);
                e.printStackTrace();
            }

        }
    }

    private void redefineLoadedClass(Class clazz) {

        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }

        String className = clazz.getName();

        if (shouldTransformClass(className)) {

            URL classFileResourceURL;

            ClassLoader classLoader = clazz.getClassLoader();

            if (null == classLoader) {
                classFileResourceURL = ClassLoader.getSystemResource(
                        ClassUtils.convertClassNameToResourceName(className));
            } else {
                classFileResourceURL = classLoader.getResource(
                        ClassUtils.convertClassNameToResourceName(className));
            }

            if (null == classFileResourceURL)
                return;

            InputStream byteCodeInputStream = null;
            ByteArrayOutputStream originalByteArrayOutputStream = new ByteArrayOutputStream();

            try {

                byteCodeInputStream = classFileResourceURL.openStream();

                while (byteCodeInputStream.available() > 0) {
                    originalByteArrayOutputStream.write(byteCodeInputStream.read());
                }

                byte[] bytecode = originalByteArrayOutputStream.toByteArray();

                bytecode = transform(
                        classLoader, className, clazz, clazz.getProtectionDomain(), bytecode
                );

                if (null != bytecode) {
                    Profiler.getProfiler().redefineClass(
                            bytecode,
                            className,
                            clazz.getClassLoader());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalClassFormatException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                IO.close(originalByteArrayOutputStream);
                IO.close(byteCodeInputStream);
            }

        }

    }

    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {

        if (null != agentConfigurationMessage && null != agentConfigurationMessage.getClassFilters()) {

            if (!shouldTransformClass(className)) {
                return classfileBuffer;
            }

        }

        Thread currentThread = Thread.currentThread();

        for (Long profilerThreadId : Profiler.getProfiler().getProfilerThreadIds()) {
            if (currentThread.getId() == profilerThreadId) {
                return classfileBuffer;
            }
        }

        ClassLoader contextClassLoader = currentThread.getContextClassLoader();

        try {
            currentThread.setContextClassLoader(resolveAgentLibrariesClassLoader);

            byte[] transformedBytes = byteCodeTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);

            if (null == loader || null == loader.getParent()) {

                String name = ClassUtils.getFullyQualifiedName(className);

                if (!name.startsWith("com.jsonde")) {
                    Profiler.getProfiler().redefineClass(
                            transformedBytes,
                            name,
                            loader);
                }

                return classfileBuffer;

            } else {
                return transformedBytes;
            }

        } finally {
            currentThread.setContextClassLoader(contextClassLoader);
        }

    }

    private boolean shouldTransformClass(String className) {

        if ((className.startsWith("com.jsonde")) && (!className.startsWith("com.jsonde.instrumentation.samples")))
            return false;

        boolean transform = true;

        for (ClassFilterDto classFilter : agentConfigurationMessage.getClassFilters()) {
            String regex = StringUtils.wildcardToRegex(classFilter.getPackageName());
            boolean matches = ClassUtils.getFullyQualifiedName(className).matches(regex);

            if (matches) {
                transform = classFilter.isInclusive();
            }

        }
        return transform;
    }

    public synchronized void startServer() throws NetworkServerException {

        final String METHOD_NAME = "startServer()";

        profiler.addMessageListener(this);
        profiler.start();

        while (null == agentConfigurationMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(METHOD_NAME, e);
                Thread.currentThread().interrupt();
            }
        }

        profiler.removeMessageListener(this);

    }

    private volatile AgentConfigurationMessage agentConfigurationMessage;

    public synchronized void onMessage(Message message) {

        final String METHOD_NAME = "onMessage(Message)";

        if (log.isTraceEnabled()) {
            log.trace(METHOD_NAME, "Recieved Message" + message);
        }

        if (message instanceof AgentConfigurationMessage) {
            agentConfigurationMessage = (AgentConfigurationMessage) message;
            notifyAll();
        }

    }

}
