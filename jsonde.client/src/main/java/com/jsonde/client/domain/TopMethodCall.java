package com.jsonde.client.domain;


public class TopMethodCall extends DomainObject {

    private long methodCallId;
    private long hashCode;
    private long count;

    public long getMethodCallId() {
        return methodCallId;
    }

    public void setMethodCallId(long methodCallId) {
        this.methodCallId = methodCallId;
    }

    public long getHashCode() {
        return hashCode;
    }

    public void setHashCode(long hashCode) {
        this.hashCode = hashCode;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
