package com.jsonde.api.function;

import com.jsonde.api.Message;

import java.util.concurrent.atomic.AtomicLong;

public abstract class FunctionRequest<T extends FunctionResponse> extends Message {

    private final static AtomicLong requestIdSequence = new AtomicLong();

    private final long requestId;

    public FunctionRequest() {
        this.requestId = requestIdSequence.getAndIncrement();
    }

    public long getRequestId() {
        return requestId;
    }

}