package com.jsonde.profiler;

import com.jsonde.api.methodCall.RegisterClassMessage;
import com.jsonde.api.methodCall.RegisterMethodMessage;
import com.jsonde.profiler.network.NetworkServer;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

public class ProfilerRegisterClassTest extends TestCase {

    final Mockery context = new JUnit4Mockery();
    final NetworkServer networkServerMock = context.mock(NetworkServer.class);

    public void testRegisterSeveralClasses() throws Exception {

        context.checking(
                new Expectations() {{
                    one(networkServerMock).sendMessage(new RegisterClassMessage(0, 0, 0, "class1", "", "", new String[]{}));
                    one(networkServerMock).sendMessage(new RegisterClassMessage(1, 0, 0, "class2", "", "", new String[]{}));
                }}
        );

        ((ProfilerImpl) Profiler.getProfiler()).setServer(networkServerMock);

        long classId1 = Profiler.getProfiler().registerClass(
                0, 0, "class1", "", "", new String[]{}, ClassLoader.getSystemClassLoader()
        );

        System.out.println(classId1);

        long classId2 = Profiler.getProfiler().registerClass(
                0, 0, "class2", "", "", new String[]{}, ClassLoader.getSystemClassLoader()
        );

        System.out.println(classId2);

        assertFalse(classId2 == classId1);

    }

    public void testRegisterSeveralMethods() throws Exception {

        context.checking(
                new Expectations() {{
                    one(networkServerMock).sendMessage(new RegisterMethodMessage(0, 0, 0, "", "", "", new String[]{}));
                    one(networkServerMock).sendMessage(new RegisterMethodMessage(1, 0, 0, "", "", "", new String[]{}));
                }}
        );

        ((ProfilerImpl) Profiler.getProfiler()).setServer(networkServerMock);

        long methodId1 = Profiler.getProfiler().registerMethod(
                0, 0, "", "", "", new String[]{}
        );

        long methodId2 = Profiler.getProfiler().registerMethod(
                0, 0, "", "", "", new String[]{}
        );

        assertFalse(methodId1 == methodId2);

    }

}
