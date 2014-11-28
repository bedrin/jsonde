package com.jsonde.profiler.network;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;

public interface NetworkServer {
    void start() throws NetworkServerException;

    void stop() throws NetworkServerException;

    void sendMessage(Message message);

    void addMessageListener(MessageListener messageListener);

    void removeMessageListener(MessageListener messageListener);
}
