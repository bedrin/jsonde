package com.jsonde.samples;

import javax.xml.parsers.DocumentBuilderFactory;

public class Application4 implements Runnable {

    public void run() {
        DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
    }

    public static void main(String... arguments) throws Exception {

        DocumentBuilderFactory.newInstance();

        Thread.sleep(1000);

        Thread t = new Thread(new Application4());

        t.start();

        t.join();

        Thread.sleep(5000);

        System.out.println("DONE!");

    }

}
