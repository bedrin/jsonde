package com.jsonde.profiler.network;

import com.jsonde.api.Message;
import com.jsonde.util.io.IO;
import com.jsonde.util.log.Log;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerInputWorker implements Runnable, Closeable {

    private final static Log log = Log.getLog(ServerInputWorker.class);

    private NetworkServerImpl server;
    private final Socket socket;
    private InputStream inputStream;

    public ServerInputWorker(NetworkServerImpl server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        final String METHOD_NAME = "run()";

        ObjectInputStream objectInputStream = null;

        try {

            objectInputStream = new ObjectInputStream(inputStream);

            server.setInputWorkerReady(true);

            log.info("[ServerInputWorker] ready");

            while (server.isRunning()) {

                log.info("[ServerInputWorker] running");

                log.info("[ServerInputWorker] reading message from socket");

                Object object = objectInputStream.readObject();

                log.info("[ServerInputWorker] message recieved");

                Message message = (Message) object;

                server.processMessage(message);

                log.info("[ServerInputWorker] running");

            }

            log.info("[ServerInputWorker] stopped");

        } catch (SocketException e) {
            log.info("[ServerInputWorker] SocketException catched");
            if (server.isRunning()) {
                log.error(METHOD_NAME, e);
            } else {
                log.trace(METHOD_NAME, e);
            }
        } catch (EOFException e) {
            log.info("[ServerInputWorker] EOFException catched");
            if (server.isRunning()) {
                log.error(METHOD_NAME, e);
            } else {
                log.trace(METHOD_NAME, e);
            }
        } catch (IOException e) {
            log.error(METHOD_NAME, e);
        } catch (ClassNotFoundException e) {
            log.error(METHOD_NAME, e);
        } finally {
            IO.close(objectInputStream);
            IO.close(inputStream);
        }

    }

    public void close() throws IOException {
        inputStream.close();
    }

}
