package com.jsonde.profiler.network;

import com.jsonde.api.Message;
import com.jsonde.util.io.IO;
import com.jsonde.util.log.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerOutputWorker implements Runnable, Closeable {

    private final static Log log = Log.getLog(ServerOutputWorker.class);

    private NetworkServerImpl server;
    private final Socket socket;

    public ServerOutputWorker(NetworkServerImpl server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public void run() {

        final String METHOD_NAME = "run()";

        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            server.setOutputWorkerReady(true);

            log.info("[ServerOutputWorker] ready");

            while (server.isRunning() || server.isMessageInQueue()) {

                log.info("[ServerOutputWorker] running");

                while (server.isMessageInQueue()) {

                    log.info("[ServerOutputWorker] message is in queue");

                    Message message = server.takeMessageFromQueue();
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();

                    message.returnToPool();

                    log.info("[ServerOutputWorker] message was sent");

                }

                log.info("[ServerOutputWorker] running");

            }

            log.info("[ServerOutputWorker] stopped");

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

    public void close() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
