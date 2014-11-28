package com.jsonde.client.network;

import com.jsonde.api.Message;
import com.jsonde.util.io.IO;
import com.jsonde.util.log.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientOutputWorker implements Runnable {

    private final static Log log = Log.getLog(ClientOutputWorker.class);

    private NetworkClientImpl client;
    private final Socket socket;

    public ClientOutputWorker(NetworkClientImpl client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    public void run() {

        final String METHOD_NAME = "run()";

        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            client.setOutputWorkerReady(true);

            log.info("[ClientOutputWorker] ready");

            while (client.isRunning() || client.isMessageInQueue()) {

                log.info("[ClientOutputWorker] running");

                while (client.isMessageInQueue()) {

                    log.info("[ClientOutputWorker] message is in queue");

                    Message message = client.takeMessageFromQueue();
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();

                    log.info("[ClientOutputWorker] message was sent");

                }

                log.info("[ClientOutputWorker] running");

            }

            log.info("[ClientOutputWorker] stopped");

        } catch (IOException e) {
            log.error(METHOD_NAME, e);
        } catch (InterruptedException e) {
            log.error(METHOD_NAME, e);
            Thread.currentThread().interrupt();
        } finally {
            IO.close(objectOutputStream);
            IO.close(outputStream);
        }

    }

}
