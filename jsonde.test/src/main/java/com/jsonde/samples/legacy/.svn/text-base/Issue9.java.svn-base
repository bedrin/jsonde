package com.jsonde.samples.legacy;

public class Issue9 {

    public Issue9() throws Exception {
        this("argument");
    }

    public Issue9(String argument) throws Exception {
        throw new Exception("test exception");
    }

    public Issue9(int i) {
        this(i, i + 1);
    }

    public Issue9(int i, int j) {
        this(i, j, i + j);
    }

    public Issue9(int i, int j, int k) {
    }

    public static void main(String[] arguments) throws Exception {
        try {
            Issue9 issue9 = new Issue9();
        } catch (Exception e) {
            // do nothing
        }

        Thread thread = new Thread(
                new Runnable() {
                    public void run() {
                        Issue9 issue9 = new Issue9(1);
                    }
                }
        );

        thread.start();
        thread.join();

    }

}
