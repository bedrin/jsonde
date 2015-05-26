package com.jsonde.instrumentation;

import com.jsonde.instrumentation.classloader.JSondeClassLoader;
import com.jsonde.instrumentation.profiler.InvocationCountingProfiler;
import com.jsonde.instrumentation.samples.SimpleClass;
import com.jsonde.instrumentation.samples.SuperConstructor;
import com.jsonde.profiler.Profiler;
import junit.framework.TestCase;

public class TestInstrumentSuperConsrtuctor extends TestCase {

    public void testInstrumentSingleMethod() throws Exception {

        InvocationCountingProfiler invocationCountingProfiler = new InvocationCountingProfiler();

        Profiler.initializeProfiler(invocationCountingProfiler);

        ClassLoader transformingClassLoader = new JSondeClassLoader();

        Class simpleClazz = transformingClassLoader.loadClass(SuperConstructor.class.getName());

        try {
            Object o = simpleClazz.newInstance();
        } catch (Exception e) {

        }

        assertEquals("leave counters mismatch", 1, invocationCountingProfiler.leaveMethodImplCounter);
        assertEquals("enter counters mismatch", 1, invocationCountingProfiler.enterMethodImplCounter);
        assertEquals("preenter counters mismatch", 2, invocationCountingProfiler.preEnterConstructorImplCounter);
        assertEquals(2, invocationCountingProfiler.registerClassCounter);
        assertEquals(2, invocationCountingProfiler.registerMethodCounter);

    }

}
