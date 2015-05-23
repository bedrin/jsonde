package com.jsonde.instrumentation;

import com.jsonde.instrumentation.classloader.JSondeClassLoader;
import com.jsonde.instrumentation.profiler.InvocationCountingProfiler;
import com.jsonde.instrumentation.samples.DomClient;
import com.jsonde.profiler.Profiler;
import junit.framework.TestCase;

public class TestInstrumentSystemClasses extends TestCase {

    public void testInstrumentSystemClasses() throws Exception {

        InvocationCountingProfiler invocationCountingProfiler = new InvocationCountingProfiler();

        Profiler.initializeProfiler(invocationCountingProfiler);

        JSondeClassLoader loader = new JSondeClassLoader();

        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(loader);

        Class clazz = loader.loadClass(DomClient.class.getName());
        clazz.newInstance();

        currentThread.setContextClassLoader(oldClassLoader);

        assertTrue(invocationCountingProfiler.enterMethodImplCounter > 0);
        assertTrue(invocationCountingProfiler.registerMethodCounter > 0);
        assertTrue(invocationCountingProfiler.leaveMethodImplCounter > 0);
        assertTrue(invocationCountingProfiler.registerClassCounter > 0);

    }
}