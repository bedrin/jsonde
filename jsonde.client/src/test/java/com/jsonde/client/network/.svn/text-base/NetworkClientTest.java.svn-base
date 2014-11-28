package com.jsonde.client.network;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.profiler.DaemonThreadFactory;
import com.jsonde.profiler.network.NetworkServer;
import com.jsonde.profiler.network.NetworkServerImpl;
import junit.framework.TestCase;

public class NetworkClientTest extends TestCase {

    private static class InvocationCounterMessageListener implements MessageListener {

        public int invocationCount;

        public void onMessage(Message message) {
            invocationCount++;
        }

    }

    private static class TestMessage extends Message {

    }

    public void testNetworkClient() throws Exception {

        final NetworkServer networkServer = new NetworkServerImpl(60000, new DaemonThreadFactory());
        new Thread(new Runnable() {
            public void run() {
                try {
                    networkServer.start();
                } catch (Exception e) {
                    fail();
                }
            }
        }).start();

        InvocationCounterMessageListener serverInvocationCounterMessageListener =
                new InvocationCounterMessageListener();
        networkServer.addMessageListener(serverInvocationCounterMessageListener);

        NetworkClient networkClient = new NetworkClientImpl("127.0.0.1", 60000);
        try {
            networkClient.start();
        } catch (NetworkClientException e) {
            Thread.sleep(300);
            networkClient.start();
        }

        InvocationCounterMessageListener clientInvocationCounterMessageListener =
                new InvocationCounterMessageListener();
        networkClient.addMessageListener(clientInvocationCounterMessageListener);

        networkServer.sendMessage(new TestMessage());
        networkClient.sendMessage(new TestMessage());

        if (0 == clientInvocationCounterMessageListener.invocationCount ||
                0 == serverInvocationCounterMessageListener.invocationCount) {
            Thread.sleep(50);
            if (0 == clientInvocationCounterMessageListener.invocationCount ||
                    0 == serverInvocationCounterMessageListener.invocationCount) {
                Thread.sleep(200);
            }
        }

        networkServer.stop();
        networkClient.stop();

        assertEquals(1, clientInvocationCounterMessageListener.invocationCount);
        assertEquals(1, serverInvocationCounterMessageListener.invocationCount);

    }

}
