package com.jsonde.profiler;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.profiler.network.NetworkServer;
import com.jsonde.profiler.network.NetworkServerException;
import com.jsonde.profiler.network.NetworkServerImpl;
import junit.framework.TestCase;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerTest extends TestCase {

    private static class TestMessage extends Message {

        private int field;

        private TestMessage(int field) {
            this.field = field;
        }

        public int getField() {
            return field;
        }

        public void setField(int field) {
            this.field = field;
        }
    }

    public void testServerStartStop() throws Exception {

        final NetworkServer networkServer = new NetworkServerImpl(4444, new DaemonThreadFactory());

        new Thread(new Runnable() {

            public void run() {
                try {

                    networkServer.start();

                    networkServer.sendMessage(new TestMessage(2));
                    networkServer.addMessageListener(new MessageListener() {

                        public void onMessage(Message message) {
                            assertEquals(TestMessage.class, message.getClass());
                            TestMessage tm = (TestMessage) message;
                            assertEquals(1, tm.getField());
                        }

                    });
                } catch (NetworkServerException e) {
                    e.printStackTrace();
                    fail();
                }
            }

        }).start();

        Socket clientSocket = new Socket();

        try {
            Thread.sleep(100);
            clientSocket.connect(new InetSocketAddress(4444));
        } catch (IOException e) {
            Thread.sleep(900);
            clientSocket.connect(new InetSocketAddress(4444));
        }

        OutputStream os = clientSocket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        oos.writeObject(new TestMessage(1));

        oos.flush();

        InputStream is = clientSocket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        TestMessage tm = (TestMessage) ois.readObject();

        assertEquals(2, tm.getField());

        networkServer.stop();


    }

}
