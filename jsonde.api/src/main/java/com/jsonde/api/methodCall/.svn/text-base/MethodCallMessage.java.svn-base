package com.jsonde.api.methodCall;

import com.jsonde.api.Message;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

public class MethodCallMessage extends Message implements Externalizable {

    private transient MethodCallDto[] methodCallDtos;
    private transient boolean complete;

    private transient MethodCallSummaryDto methodCallSummaryDto;

    public MethodCallMessage() {
    }

    public MethodCallMessage(
            List<MethodCallDto> methodCallDtos,
            MethodCallSummaryDto methodCallSummaryDto,
            boolean complete) {

        this.methodCallDtos = methodCallDtos.toArray(new MethodCallDto[methodCallDtos.size()]);
        this.complete = complete;
        this.methodCallSummaryDto =
                complete ? methodCallSummaryDto :
                        copyMethodCallSummaryDto(methodCallSummaryDto);
    }

    private MethodCallSummaryDto copyMethodCallSummaryDto(MethodCallSummaryDto original) {

        if (null == original) {
            return null;
        }

        MethodCallSummaryDto copy = new MethodCallSummaryDto();

        copy.methodId = original.methodId;
        copy.exceptionCount = original.exceptionCount;
        copy.executionTime = original.executionTime;
        copy.invocationCount = original.invocationCount;

        original.exceptionCount = 0;
        original.executionTime = 0;
        original.invocationCount = 0;

        for (MethodCallSummaryDto calleeSummaryOriginal : original.getCallees()) {
            copy.addCallee(copyMethodCallSummaryDto(calleeSummaryOriginal));
        }

        return copy;

    }

    public MethodCallDto[] getMethodCallDtos() {
        return methodCallDtos;
    }

    public MethodCallSummaryDto getMethodCallSummaryDto() {
        return methodCallSummaryDto;
    }

    public boolean isComplete() {
        return complete;
    }

    @Override
    public void returnToPool() {
        super.returnToPool();
        for (int i = 0; i < this.methodCallDtos.length; i++) {
            MethodCallDto methodCallDto = methodCallDtos[i];
            methodCallDto.returnToPool();
            methodCallDtos[i] = null;
        }
        methodCallDtos = null;
    }

    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeBoolean(complete);
        out.writeInt(methodCallDtos.length);

        for (MethodCallDto methodCallDto : methodCallDtos) {

            out.writeByte(methodCallDto.flags);
            out.writeLong(methodCallDto.methodCallId);
            out.writeLong(methodCallDto.methodId);

            if ((1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG) ==
                    (methodCallDto.flags & (1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG))) {
                out.writeLong(methodCallDto.actualClassId);
            }

            out.writeLong(methodCallDto.executionTime);

            if ((1 << MethodCallDto.CALLER_ID_SET_FLAG) ==
                    (methodCallDto.flags & (1 << MethodCallDto.CALLER_ID_SET_FLAG))) {
                out.writeLong(methodCallDto.callerId);
            }

        }

        //if (complete) {
        out.writeObject(methodCallSummaryDto);
        //}

    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        complete = in.readBoolean();
        int methodCallDtoLength = in.readInt();

        methodCallDtos = new MethodCallDto[methodCallDtoLength];

        for (int i = 0; i < methodCallDtoLength; i++) {
            MethodCallDto methodCallDto = MethodCallDtoFactory.getMethodCallDtoFromPool();

            methodCallDto.flags = in.readByte();
            methodCallDto.methodCallId = in.readLong();
            methodCallDto.methodId = in.readLong();

            if ((1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG) ==
                    (methodCallDto.flags & (1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG))) {
                methodCallDto.actualClassId = in.readLong();
            }

            methodCallDto.executionTime = in.readLong();

            if ((1 << MethodCallDto.CALLER_ID_SET_FLAG) ==
                    (methodCallDto.flags & (1 << MethodCallDto.CALLER_ID_SET_FLAG))) {
                methodCallDto.callerId = in.readLong();
            }

            methodCallDtos[i] = methodCallDto;

        }

        //if (complete) {
        methodCallSummaryDto = (MethodCallSummaryDto) in.readObject();
        //}

    }

}
