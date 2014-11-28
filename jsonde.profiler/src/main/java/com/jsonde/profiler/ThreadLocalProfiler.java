package com.jsonde.profiler;

import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.api.methodCall.MethodCallDtoFactory;
import com.jsonde.api.methodCall.MethodCallSummaryDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadLocalProfiler {

    private int maxMethodCallGraphSize = 2000;

    private final Profiler profiler;

    public ThreadLocalProfiler(Profiler profiler) {
        this.profiler = profiler;
    }

    private boolean isProfilerCode;
    private int level = 0;
    private int count = 0;

    private LinkedList<MethodCallDto> methodCallsQueue = new LinkedList<MethodCallDto>();
    private List<MethodCallDto> finishedMethodCalls = new ArrayList<MethodCallDto>(maxMethodCallGraphSize);

    private MethodCallSummaryDto contextMethodCallSummary; // todo: find out why some method ids are missing?
    private MethodCallSummaryDto rootContextMethodCallSummary;

    protected void preEnterConstructorImpl(long methodId) {
        enterMethodImpl(methodId, null, null);
    }

    protected void enterConstructorImpl(long methodId, Object object, Object[] arguments) {
        if (isProfilerCode) return;

        try {

            isProfilerCode = true;

            MethodCallDto contextMethodCallDto = methodCallsQueue.element();

            if (methodId == contextMethodCallDto.methodId) {
                if (null != object) {
                    contextMethodCallDto.actualClassId =
                            profiler.generateClassIdAndRegisterIfAbsent(object.getClass());
                    contextMethodCallDto.flags |=
                            1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG;
                }
            }

        } finally {
            isProfilerCode = false;
        }
    }

    protected void enterMethodImpl(long methodId, Object object, Object[] arguments) {

        if (isProfilerCode) return;

        try {

            isProfilerCode = true;

            level++;

            //MethodCallDto currentMethodCallDto = new MethodCallDto(profiler.generateMethodCallId(), methodId);
            MethodCallDto currentMethodCallDto = MethodCallDtoFactory.getMethodCallDtoFromPool();
            currentMethodCallDto.methodCallId = profiler.generateMethodCallId();
            currentMethodCallDto.methodId = methodId;
            currentMethodCallDto.executionTime = System.currentTimeMillis();

            if (null != object) {
                Class clazz = object.getClass();

                long classId = profiler.generateClassIdAndRegisterIfAbsent(clazz);

                currentMethodCallDto.actualClassId = classId;
                currentMethodCallDto.flags |=
                        1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG;
            }

            if (level > 1) {
                currentMethodCallDto.callerId = methodCallsQueue.element().methodCallId;
                currentMethodCallDto.flags |=
                        1 << MethodCallDto.CALLER_ID_SET_FLAG;
            }

            methodCallsQueue.addFirst(currentMethodCallDto);

            {
                // todo implement cpu profiling stuff

                MethodCallSummaryDto currentMethodCallSummary;

                if (null == contextMethodCallSummary) {
                    currentMethodCallSummary = new MethodCallSummaryDto();
                    rootContextMethodCallSummary = currentMethodCallSummary;
                } else {
                    currentMethodCallSummary = contextMethodCallSummary.getCallee(methodId);
                }

                contextMethodCallSummary = currentMethodCallSummary;

                contextMethodCallSummary.methodId = methodId;
                contextMethodCallSummary.invocationCount++;
                contextMethodCallSummary.startTime = System.nanoTime();

            }

        } finally {
            isProfilerCode = false;
        }

    }

    public void dump() {

        System.out.println(Thread.currentThread().getId() + " thread level is " + level);
        System.out.println(count);
        System.out.println(contextMethodCallSummary);
        System.out.println(rootContextMethodCallSummary);

    }

    protected void leaveMethodImpl(boolean isVoid, boolean isThrowsException, Object result) {

        if (isProfilerCode) return;

        try {

            isProfilerCode = true;

//            debug(false);

            level--;
            count++;

            MethodCallDto contextMethodCallDto = methodCallsQueue.removeFirst();

            contextMethodCallDto.executionTime = System.currentTimeMillis() - contextMethodCallDto.executionTime;

            if (!isVoid) contextMethodCallDto.flags |=
                    1 << MethodCallDto.RETURN_VALUE_FLAG;

            if (isThrowsException) contextMethodCallDto.flags |=
                    1 << MethodCallDto.THROW_EXCEPTION_FLAG;

            finishedMethodCalls.add(contextMethodCallDto);

            {
                // Begin cpu profiler stuff

                if (isThrowsException)
                    contextMethodCallSummary.exceptionCount++;

                contextMethodCallSummary.executionTime +=
                        System.nanoTime() - contextMethodCallSummary.startTime;

            }

            if (0 == level) {
                // Method call graph building finished
                profiler.processMethodCall(finishedMethodCalls, rootContextMethodCallSummary, true);

                count = 0;
                finishedMethodCalls.clear();
                rootContextMethodCallSummary = null;

            } else {
                // Method call graph building in progress
                if (0 == count % maxMethodCallGraphSize) {

                    if (0 == count % (maxMethodCallGraphSize * 5)) {
                        profiler.processMethodCall(finishedMethodCalls, rootContextMethodCallSummary, false);
                    } else {
                        profiler.processMethodCall(finishedMethodCalls, null, false);
                    }

                    finishedMethodCalls.clear();
                }

            }

            contextMethodCallSummary = contextMethodCallSummary.caller;

        } finally {
            isProfilerCode = false;
        }

    }

    public int getMaxMethodCallGraphSize() {
        return maxMethodCallGraphSize;
    }

    public void setMaxMethodCallGraphSize(int maxMethodCallGraphSize) {
        this.maxMethodCallGraphSize = maxMethodCallGraphSize;

        // todo resize finishedMethodCalls
    }

}
