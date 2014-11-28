package com.jsonde.api.methodCall;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MethodCallSummaryDto implements Serializable {

    private Map<Long, MethodCallSummaryDto> callees = new HashMap<Long, MethodCallSummaryDto>();
    public MethodCallSummaryDto caller;

    public long methodId;
    public long executionTime;
    public transient long startTime;
    public int exceptionCount;
    public int invocationCount;

    public void addCallee(MethodCallSummaryDto callee) {
        callee.caller = this;
        callees.put(callee.methodId, callee);
    }

    public MethodCallSummaryDto getCallee(long methodId) {

        if (!callees.containsKey(methodId)) {
            MethodCallSummaryDto callee = new MethodCallSummaryDto();
            callee.methodId = methodId;
            addCallee(callee);
        }

        return callees.get(methodId);
    }

    public Collection<MethodCallSummaryDto> getCallees() {
        return callees.values();
    }

    @Override
    public String toString() {
        return "MethodCallSummaryDto{" +
                "methodId=" + methodId +
                ", executionTime=" + executionTime +
                ", exceptionCount=" + exceptionCount +
                ", invocationCount=" + invocationCount +
                ", callees=" + callees +
                '}';
    }
}