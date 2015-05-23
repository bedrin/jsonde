package com.jsonde.instrumentation.samples;

import com.jsonde.profiler.Profiler;

public class SimpleClass {

    static Class clazz;

    static {
        Profiler.describeClass(1, clazz);
    }

    public void method() {

    }

}
