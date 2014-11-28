package com.jsonde.instrumentation;

import com.jsonde.instrumentation.classloader.JSondeClassLoader;
import com.jsonde.instrumentation.profiler.InvocationCountingProfiler;
import com.jsonde.instrumentation.samples.SimpleClass;
import com.jsonde.profiler.Profiler;
import junit.framework.TestCase;

public class TestInstrumentSingleMethod extends TestCase {

    public void testInstrumentSingleMethod() throws Exception {

        InvocationCountingProfiler invocationCountingProfiler = new InvocationCountingProfiler();

        Profiler.initializeProfiler(invocationCountingProfiler);

        ClassLoader transformingClassLoader = new JSondeClassLoader();

        Class simpleClazz = transformingClassLoader.loadClass(SimpleClass.class.getName());

        simpleClazz.newInstance();

        assertEquals(1, invocationCountingProfiler.enterConstructorImplCounter);
        assertEquals(0, invocationCountingProfiler.enterMethodImplCounter);
        assertEquals(1, invocationCountingProfiler.preEnterConstructorImplCounter);
        assertEquals(1, invocationCountingProfiler.leaveMethodImplCounter);
        assertEquals(1, invocationCountingProfiler.registerClassCounter);
        assertEquals(2, invocationCountingProfiler.registerMethodCounter);

    }

}
