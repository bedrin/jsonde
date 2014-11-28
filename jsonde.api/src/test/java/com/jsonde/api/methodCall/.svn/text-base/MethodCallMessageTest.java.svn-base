package com.jsonde.api.methodCall;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;

public class MethodCallMessageTest extends TestCase {

    public void testMethodCallSummary() throws Exception {

        MethodCallSummaryDto s = new MethodCallSummaryDto();

        s.methodId = 0;
        s.invocationCount = 1;
        s.startTime = System.nanoTime();

        MethodCallSummaryDto s1 = new MethodCallSummaryDto();

        s1.methodId = 1;
        s1.invocationCount = 1;
        s1.startTime = System.nanoTime();
        s1.executionTime = 100;

        s.addCallee(s1);

        MethodCallSummaryDto s2 = new MethodCallSummaryDto();

        s2.methodId = 2;
        s2.invocationCount = 10;
        s2.startTime = System.nanoTime();
        s2.executionTime = 1000;

        s1.addCallee(s2);

        MethodCallMessage message =
                new MethodCallMessage(Collections.<MethodCallDto>emptyList(), s, false);

        assertNotNull(s);

    }

    public void testSerializeMethodCallMessage() throws Exception {

        byte flag = 1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG | 1 << MethodCallDto.CALLER_ID_SET_FLAG;

        MethodCallDto mc1 = new MethodCallDto();
        mc1.callerId = 5L;
        mc1.actualClassId = 10L;
        mc1.flags = flag;

        MethodCallDto mc2 = new MethodCallDto();
        mc2.callerId = 6L;
        mc2.actualClassId = 11L;
        mc2.flags = flag;

        MethodCallMessage message = new MethodCallMessage(Arrays.asList(mc1, mc2), null, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(message);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        ObjectInputStream ois = new ObjectInputStream(bais);

        MethodCallMessage messageCopy = (MethodCallMessage) ois.readObject();

        assertEquals(true, messageCopy.isComplete());

        MethodCallDto mc1loaded = messageCopy.getMethodCallDtos()[0];

        assertEquals(5L, mc1loaded.callerId);
        assertEquals(10L, mc1loaded.actualClassId);

        MethodCallDto mc2loaded = messageCopy.getMethodCallDtos()[1];

        assertEquals(6L, mc2loaded.callerId);
        assertEquals(11L, mc2loaded.actualClassId);

    }

}
