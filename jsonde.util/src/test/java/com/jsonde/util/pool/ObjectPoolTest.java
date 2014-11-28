package com.jsonde.util.pool;

import junit.framework.TestCase;

public class ObjectPoolTest extends TestCase {

    public static class Class1 {

    }

    public void testReturnToPool() throws Exception {

        ObjectPool<Class1> objectPool = new ObjectPool<Class1>(Class1.class);

        Class1 obj1 = objectPool.takeFromPool();

        objectPool.returnToPool(obj1);

        Class1 obj2 = objectPool.takeFromPool();

        assertEquals(obj1, obj2);

    }

}
