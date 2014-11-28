package com.jsonde.api.configuration;

import java.io.Serializable;

public class ClassFilterDto implements Serializable {

    private boolean inclusive;
    private String packageName;

    public ClassFilterDto(boolean inclusive, String packageName) {
        this.inclusive = inclusive;
        this.packageName = packageName;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
