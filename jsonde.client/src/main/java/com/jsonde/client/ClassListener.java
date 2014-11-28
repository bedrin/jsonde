package com.jsonde.client;

import com.jsonde.client.domain.Clazz;

import java.util.EventListener;

public interface ClassListener extends EventListener {

    void onRegisterClass(Clazz clazz);

}