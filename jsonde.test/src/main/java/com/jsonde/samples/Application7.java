package com.jsonde.samples;

import com.jsonde.samples.legacy.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Application7 {

    private static volatile Throwable exception;

    public static void main(String... arguments) throws Throwable {

        List<Thread> threads = new ArrayList<Thread>();

        threads.add(executeInThread(Constructor.class));
        threads.add(executeInThread(Issue13.class));
        threads.add(executeInThread(Issue8.class));
        threads.add(executeInThread(Issue9.class));
        threads.add(executeInThread(SimpleApp1.class));

        for (Thread thread : threads) {
            thread.join();
        }

        if (null != exception) {
            throw exception;
        }

    }

    private static Thread executeInThread(final Class clazz) {

        Thread thread = new Thread(new Runnable() {

            public void run() {

                try {

                    Method mainMethod = clazz.getMethod("main", String[].class);
                    mainMethod.invoke(null, new Object[]{new String[]{}});

                } catch (Throwable t) {
                    exception = t;
                }

            }

        });

        thread.start();

        return thread;

    }

}