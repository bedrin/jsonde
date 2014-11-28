package com.jsonde.api.methodCall;

import java.io.Serializable;

public class MethodCallDto implements Serializable {

    public static final byte RETURN_VALUE_FLAG = 0;
    public static final byte THROW_EXCEPTION_FLAG = 1;
    public static final byte CALLER_ID_SET_FLAG = 2;
    public static final byte ACTUAL_CLASS_ID_SET_FLAG = 3;

    public byte flags;

    public long methodCallId;
    public long callerId;

    public long methodId;
    public long actualClassId;

    public long executionTime;

    public void returnToPool() {
        MethodCallDtoFactory.returnMethodCallDtoToPool(this);
    }

    @Override
    public String toString() {
        return "MethodCallDto{" +
                "flags=" + flags +
                ", methodCallId=" + methodCallId +
                ", callerId=" + callerId +
                ", methodId=" + methodId +
                ", actualClassId=" + actualClassId +
                ", executionTime=" + executionTime +
                '}';
    }
}