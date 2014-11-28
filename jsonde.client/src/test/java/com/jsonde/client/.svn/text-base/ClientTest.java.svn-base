package com.jsonde.client;

import com.jsonde.api.function.echo.EchoFunctionRequest;
import com.jsonde.api.function.echo.EchoFunctionResponse;
import com.jsonde.client.network.NetworkClient;
import com.jsonde.client.network.NetworkClientException;
import com.jsonde.client.network.NetworkClientImpl;
import com.jsonde.profiler.Profiler;
import junit.framework.TestCase;

public class ClientTest extends TestCase {

    public void testClient() throws Exception {

        final Profiler profiler = Profiler.initializeProfiler(null, 60000);
        new Thread(new Runnable() {
            public void run() {
                try {
                    profiler.start();
                } catch (Exception e) {
                    fail();
                }
            }
        }).start();

        NetworkClient networkClient = new NetworkClientImpl("127.0.0.1", 60000);
        try {
            networkClient.start();
        } catch (NetworkClientException e) {
            Thread.sleep(300);
            networkClient.start();
        }

        EchoFunctionResponse echoFunctionResponse = networkClient.invokeFunction(
                new EchoFunctionRequest("test"));

        assertEquals("test", echoFunctionResponse.getMessage());

        profiler.stop();
        networkClient.stop();

    }

}
