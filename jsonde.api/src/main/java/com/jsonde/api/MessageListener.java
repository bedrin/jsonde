package com.jsonde.api;

import java.util.EventListener;

public interface MessageListener extends EventListener {

    void onMessage(Message message);

}
