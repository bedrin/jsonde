package com.jsonde.api.methodCall;

import com.jsonde.api.Message;

public class DescribeClassMessage extends Message {

    private long methodId;
    private long classId;
    private long classLoaderId;
    private String codeLocation;

    private boolean classRedefined;

    public DescribeClassMessage(long methodId, boolean classRedefined) {
        this.methodId = methodId;
        this.classRedefined = classRedefined;
    }

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long methodId) {
        this.methodId = methodId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getClassLoaderId() {
        return classLoaderId;
    }

    public void setClassLoaderId(long classLoaderId) {
        this.classLoaderId = classLoaderId;
    }

    public String getCodeLocation() {
        return codeLocation;
    }

    public void setCodeLocation(String codeLocation) {
        this.codeLocation = codeLocation;
    }

    public boolean isClassRedefined() {
        return classRedefined;
    }

    public void setClassRedefined(boolean classRedefined) {
        this.classRedefined = classRedefined;
    }
}
