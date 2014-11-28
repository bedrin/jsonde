package com.jsonde.samples;

public class BigMethodCallApplication {

    public static void main(String... arguments) throws Exception {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                method1();
            }

        });
        thread.start();
        method1();
        thread.join();
    }

    private static void method1() {

        for (int i = 0; i < 2000; i++) {
            if (0 == i % 2)
                method2(i);
            else
                method3(i);
        }

    }

    private static int method2(int i) {
        return i;
    }

    private static int method3(int i) {
        return i;
    }

}