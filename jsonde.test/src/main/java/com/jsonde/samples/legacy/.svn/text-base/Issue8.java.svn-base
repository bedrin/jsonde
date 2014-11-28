package com.jsonde.samples.legacy;

class JMSManager {

    public void createConnection() {
    }

    public void sendMessage(String message) {
    }

    public void close() {
    }

}

public class Issue8 {

    public static void main(String[] arguments) throws Exception {
        method1();
        method2();
    }

    private static void method1() throws Exception {
        JMSManager manager = new JMSManager();
        manager.createConnection();
        manager.sendMessage("hello, world");
        manager.close();
    }

    private static void method2() {
        try {
            JMSManager manager = new JMSManager();
            manager.createConnection();
            manager.sendMessage("hello, world");
            manager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
