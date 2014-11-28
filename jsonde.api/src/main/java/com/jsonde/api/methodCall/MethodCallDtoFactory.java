package com.jsonde.api.methodCall;

import com.jsonde.util.pool.ObjectPool;
import com.jsonde.util.pool.ObjectPoolException;

public class MethodCallDtoFactory extends ObjectPool<MethodCallDto> {

    private static final MethodCallDtoFactory instance = new MethodCallDtoFactory();

    @Override
    protected MethodCallDto create() throws ObjectPoolException {
        return new MethodCallDto();
    }

    @Override
    protected void activate(MethodCallDto element) {
        super.activate(element);
        element.flags = 0;
    }

    public static MethodCallDto getMethodCallDtoFromPool() {
        try {
            return instance.takeFromPool();
        } catch (ObjectPoolException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void returnMethodCallDtoToPool(MethodCallDto methodCallDto) {
        instance.returnToPool(methodCallDto);
    }

}
