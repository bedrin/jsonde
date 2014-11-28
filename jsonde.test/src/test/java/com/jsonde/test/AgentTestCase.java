package com.jsonde.test;

import com.jsonde.util.file.FileUtils;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

public abstract class AgentTestCase extends TestCase {

    private volatile boolean agentReady;
    private Object agentLock = new Object();
    private static final String PATH_SEPARATOR = System.getProperty("path.separator");

    private void deleteTestDbFolder() {
        File dbDirectory = new File("target/test-database").getAbsoluteFile();
        FileUtils.deleteDirectory(dbDirectory);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteTestDbFolder();
    }

    /*@Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteTestDbFolder();
    }*/

    protected void executeWithApplicationAndAgent(Class mainClass, Callable test) throws Exception {

        String version = System.getProperty("jsonde.version");

        String java = "java";
        Collection<String> classPath = Arrays.asList(
                "target/jsonde.test-" + version + ".jar"
        );

        ProcessBuilder pb = new ProcessBuilder(
                java,
                "-javaagent:target/jsonde.agent-" + version + ".jar=60001",
                "-Dcom.jsonde.test=true",
                "-classpath",
                joinClassPath(classPath),
                mainClass.getName()
        );

        File directory = new File(".").getAbsoluteFile();
        System.out.println(directory);
        pb.directory(directory);

        pb.redirectErrorStream(true);

        final Process process = pb.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                process.destroy();
            }

        }));

        redirectProcessStreams(process);

        waitForAgent();

        test.call();

        int exitStatus = process.waitFor();

        assertEquals(0, exitStatus);

        process.destroy();

    }

    protected void redirectProcessStreams(final Process process) {
        new Thread(new Runnable() {

            public void run() {
                InputStream is = process.getInputStream();
                try {
                    for (int i = is.read(); i != -1; i = is.read()) {
                        if (0 == i) {
                            agentReady = true;
                            synchronized (agentLock) {
                                agentLock.notifyAll();
                            }
                        }
                        System.out.print((char) i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }).start();
    }


    protected void waitForAgent() {
        synchronized (agentLock) {
            while (!agentReady) {
                try {
                    agentLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }
    }

    protected static String joinClassPath(Collection<String> classPath) {
        StringBuilder classPathStringBuilder = new StringBuilder();

        Iterator<String> classPathIterator = classPath.iterator();

        while (classPathIterator.hasNext()) {
            classPathStringBuilder.append(classPathIterator.next());
            if (classPathIterator.hasNext()) {
                classPathStringBuilder.append(PATH_SEPARATOR);
            }
        }

        return classPathStringBuilder.toString();
    }

}
