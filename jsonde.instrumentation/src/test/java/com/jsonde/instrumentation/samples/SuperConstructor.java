package com.jsonde.instrumentation.samples;

public class SuperConstructor extends BaseClass {

    public SuperConstructor() {
        super();
        System.out.println("child consrtuctor called");
    }
}

class BaseClass {

    public BaseClass() {
        System.out.println("super consrtuctor called");
        throw new RuntimeException();
    }

}
