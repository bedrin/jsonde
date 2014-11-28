package com.jsonde.api.methodCall;

import junit.framework.TestCase;

public class MethodCallDtoFactoryTest extends TestCase {

    public void testPool() throws Exception {

        for (int i = 0; i < 10; i++) {
            MethodCallDtoFactory.getMethodCallDtoFromPool();
        }

        MethodCallDto mc = MethodCallDtoFactory.getMethodCallDtoFromPool();

        mc.returnToPool();

        MethodCallDto mc1 = MethodCallDtoFactory.getMethodCallDtoFromPool();

        assertTrue(mc == mc1);

    }

}
