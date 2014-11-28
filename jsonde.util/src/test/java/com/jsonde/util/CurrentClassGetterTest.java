package com.jsonde.util;

import junit.framework.TestCase;

public class CurrentClassGetterTest extends TestCase {

    public void testGetCurrentClass() throws Exception {

        CurrentClassGetter currentClassGetter = new CurrentClassGetter();

        assertEquals(CurrentClassGetterTest.class, currentClassGetter.getCallerClass(0));

    }

}
