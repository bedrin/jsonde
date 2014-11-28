package com.jsonde.profiler.heap;

import com.jsonde.profiler.DaemonThreadFactory;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HeapAnalyzerTest extends TestCase {

    private static class MyClass {
    }

    public void testCreateObject() throws Exception {

        HeapAnalyzer heapAnalyzer = new HeapAnalyzer(new DaemonThreadFactory());

        heapAnalyzer.start();

        assertEquals(null, heapAnalyzer.getHeapData().get(1L));

        MyClass obj = new MyClass();

        heapAnalyzer.createObject(obj, 1, 1);

        assertEquals(1, heapAnalyzer.getHeapData().get(1L).getCreateCounter());
        assertEquals(0, heapAnalyzer.getHeapData().get(1L).getCollectCounter());

        obj = null;

        gc();

        assertEquals(1, heapAnalyzer.getHeapData().get(1L).getCreateCounter());
        //assertEquals(1,heapAnalyzer.getHeapData().get(1L).getCollectCounter());

        heapAnalyzer.stop();

    }

    private void gc() {
        for (int i = 0; i < 10; i++) {
            System.gc();
        }
    }

    public void testThreading() throws Exception {

        final HeapAnalyzer heapAnalyzer = new HeapAnalyzer(new DaemonThreadFactory());

        heapAnalyzer.start();

        final Random random = new Random();

        Runnable r = new Runnable() {

            public void run() {

                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    fail();
                }

                for (int i = 0; i < 100; i++) {
                    MyClass o = new MyClass();
                    heapAnalyzer.createObject(o, random.nextInt(100), random.nextInt(100));
                    o = null;
                }

            }

        };

        List<Thread> threads = new ArrayList<Thread>(100);


        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(r);
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }

        gc();

        heapAnalyzer.stop();

        for (Map.Entry<Long, ClassHeapData> mapEntry : heapAnalyzer.getHeapData().entrySet()) {
            assertNotNull(mapEntry.getKey());
            ClassHeapData data = mapEntry.getValue();
            assertNotNull(data);
            assertTrue(data.getCreateCounter() >= data.getCollectCounter());
            assertTrue(data.getCreateCounter() > 0);
        }


    }

}
