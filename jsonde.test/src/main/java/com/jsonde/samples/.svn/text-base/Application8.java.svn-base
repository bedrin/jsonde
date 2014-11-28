package com.jsonde.samples;

public class Application8 implements Runnable {

    public static void main(String... arguments) throws Exception {

        int length = 4;

        Thread[] threads = new Thread[length];

        for (int i = 0; i < length; i++) {
            threads[i] = new Thread(new Application8());
        }

        for (int i = 0; i < length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < length; i++) {
            threads[i].join();
        }

    }

    public void run() {
        method1();
        method3();
        method1();
        method3();
    }

    private void method1() {
        method2();
        method2();
    }

    private void method2() {
        new Class2(1);
        new Class2(getInt());
    }

    private void method3() {
        try {
            method4();
        } catch (RuntimeException e) {
            try {
                method5();
            } catch (Throwable ex) {
            }
        } finally {
            method6();
        }

    }

    private void method4() {
        throw new RuntimeException();
    }

    private void method5() {
        throw new RuntimeException();
    }

    private void method6() {
    }

    private static int getInt() {
        return 6;
    }

    static class Class1 {

        private int field1;

        Class1(int field1) {
            this.field1 = field1;
        }

        void methodA() {

        }

    }

    static class Class2 extends Class1 {

        Class2(int field1) {
            super(field1);
        }

        @Override
        void methodA() {
            super.methodA();    //To change body of overridden methods use File | Settings | File Templates.
        }

    }

}