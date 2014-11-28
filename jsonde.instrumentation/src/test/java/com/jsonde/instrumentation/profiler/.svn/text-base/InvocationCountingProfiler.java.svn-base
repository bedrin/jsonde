package com.jsonde.instrumentation.profiler;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.api.methodCall.MethodCallSummaryDto;
import com.jsonde.profiler.Profiler;
import com.jsonde.profiler.network.NetworkServerException;

import java.util.Collection;
import java.util.List;

public class InvocationCountingProfiler extends Profiler {

    public int describeClassImplCounter;

    public int enterMethodImplCounter;
    public int enterConstructorImplCounter;
    public int preEnterConstructorImplCounter;

    public int leaveMethodImplCounter;

    public int registerMethodCounter;
    public int registerClassCounter;

    protected void describeClassImpl(long classId, Class clazz) {
        describeClassImplCounter++;
    }

    @Override
    public void describeRedefinableClass(long classId, Class clazz) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void enterMethodImpl(long methodId, Object object, Object[] arguments) {
        enterMethodImplCounter++;
    }

    protected void preEnterConstructorImpl(long methodId) {
        preEnterConstructorImplCounter++;
    }

    protected void leaveMethodImpl(boolean isVoid, boolean isThrowsException, Object result) {
        leaveMethodImplCounter++;
    }

    public long registerMethod(long classId, int access, String name, String desc, String signature, String[] exceptions) {
        registerMethodCounter++;
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public long registerClass(int version, int access, String name, String signature, String superName, String[] interfaces, ClassLoader classLoader) {
        registerClassCounter++;
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startServer() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stopServer() throws NetworkServerException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addMessageListener(MessageListener messageListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeMessageListener(MessageListener messageListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void enterConstructorImpl(long methodId, Object object, Object[] arguments) {
        enterConstructorImplCounter++;
    }

    public void redefineClass(byte[] bytecode, String className, ClassLoader classLoader) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<Long> getProfilerThreadIds() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long generateMethodCallId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void processMethodCall(List<MethodCallDto> methodCallDto, MethodCallSummaryDto methodCallSummaryDto, boolean complete) {
    }

    @Override
    public long generateClassId(ClassLoader classLoader, String className) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long generateClassIdAndRegisterIfAbsent(Class clazz) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendMessage(Message registerMethodMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
