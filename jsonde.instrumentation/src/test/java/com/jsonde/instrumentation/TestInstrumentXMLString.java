package com.jsonde.instrumentation;

import com.jsonde.instrumentation.classloader.JSondeClassLoader;
import com.jsonde.instrumentation.profiler.InvocationCountingProfiler;
import com.jsonde.instrumentation.samples.SimpleClass;
import com.jsonde.instrumentation.samples.XMLString;
import com.jsonde.profiler.Profiler;
import junit.framework.TestCase;

public class TestInstrumentXMLString extends TestCase {

    public void testInstrumentSingleMethod() throws Exception {

        InvocationCountingProfiler invocationCountingProfiler = new InvocationCountingProfiler();

        Profiler.initializeProfiler(invocationCountingProfiler);

        ClassLoader transformingClassLoader = new JSondeClassLoader();

        Class simpleClazz = transformingClassLoader.loadClass(XMLString.class.getName());

        Object o = simpleClazz.newInstance();

        assertEquals(1, invocationCountingProfiler.enterMethodImplCounter);
        assertEquals(1, invocationCountingProfiler.leaveMethodImplCounter);
        assertEquals(1, invocationCountingProfiler.registerClassCounter);
        assertEquals(9, invocationCountingProfiler.registerMethodCounter);

    }

}
