package com.jsonde.samples;


public class Application6 {

    public static void main(String... arguments) throws Exception {

        Application6 app6 = new Child();
        app6.overridableMethod();
        app6.nonOverridableMethod();
        app6.staticMethod();

        ((Child) app6).overridableMethod();
        ((Child) app6).nonOverridableMethod();
        ((Child) app6).staticMethod();

        Child child = new Child();

        child.overridableMethod();
        child.nonOverridableMethod();
        child.staticMethod();

        ((Application6) child).overridableMethod();
        ((Application6) child).nonOverridableMethod();
        ((Application6) child).staticMethod();

    }

    void overridableMethod() {

    }

    private void nonOverridableMethod() {

    }

    protected static void staticMethod() {

    }


}

class Child extends Application6 {

    @Override
    void overridableMethod() {
        super.overridableMethod();    //To change body of overridden methods use File | Settings | File Templates.
    }

    void nonOverridableMethod() {

    }

    protected static void staticMethod() {

    }

}