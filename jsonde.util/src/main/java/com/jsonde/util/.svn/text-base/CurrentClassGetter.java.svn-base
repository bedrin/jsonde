package com.jsonde.util;

public class CurrentClassGetter extends SecurityManager {

    public Class getCallerClass(int depth) {
        return getClassContext()[depth + 1];
    }

}
