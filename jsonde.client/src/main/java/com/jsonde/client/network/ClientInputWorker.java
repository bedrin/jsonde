package com.jsonde.client.network;

import com.jsonde.api.Message;
import com.jsonde.util.io.IO;
import com.jsonde.util.log.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientInputWorker implements Runnable {

    private final static Log log = Log.getLog(ClientInputWorker.class);

    private NetworkClientImpl client;
    private final Socket socket;

    public ClientInputWorker(NetworkClientImpl client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    public void run() {

        final String METHOD_NAME = "run()";

        InputStream inputStream = null;
        ObjectInputStream objectInput = null;

        try {

            inputStream = socket.getInputStream();
            objectInput = new ObjectInputStream(inputStream);

            client.setInputWorkerReady(true);

            log.info("[ClientInputWorker] ready");

            while (client.isRunning()) {

                log.info("[ClientInputWorker] running");

                log.info("[ClientInputWorker] reading message from socket");

                Object object = objectInput.readObject();

                log.info("[ClientInputWorker] message recieved");

                Message message = (Message) object;

                client.processMessage(message);

                log.info("[ClientInputWorker] running");

            }

            log.info("[ClientInputWorker] stopped");

        } catch (SocketException e) {
            log.info("[ClientInputWorker] SocketException catched");
            if (client.isRunning()) {
                log.error(METHOD_NAME, e);
            } else {
                log.trace(METHOD_NAME, e);
            }
        } catch (EOFException e) {
            log.info("[ClientInputWorker] EOFException catched");
            if (client.isRunning()) {
                log.error(METHOD_NAME, e);
            } else {
                log.trace(METHOD_NAME, e);
            }
        } catch (IOException e) {
            log.error(METHOD_NAME, e);
        } catch (ClassNotFoundException e) {
            log.error(METHOD_NAME, e);
        } finally {
            IO.close(objectInput);
            IO.close(inputStream);
        }

    }

}
