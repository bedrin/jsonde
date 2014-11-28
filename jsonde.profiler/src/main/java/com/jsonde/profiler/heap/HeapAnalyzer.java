package com.jsonde.profiler.heap;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.*;
import java.util.concurrent.ThreadFactory;

public class HeapAnalyzer extends ReferenceQueue<Object> implements Runnable {

    private Map<Long, ClassHeapData> heapData = Collections.synchronizedMap(
            new HashMap<Long, ClassHeapData>());

    private Set<Reference> references = Collections.synchronizedSet(
            new HashSet<Reference>());

    private Thread thread;

    public HeapAnalyzer(ThreadFactory threadFactory) {
        thread = threadFactory.newThread(this);
    }

    public void createObject(Object object, long constructorId, long objectSize) {
        references.add(new HeapAnalyzerReference(object, this, constructorId, objectSize));

        if (!heapData.containsKey(constructorId)) {
            heapData.put(constructorId, new ClassHeapData());
        }

        heapData.get(constructorId).newObject(objectSize);
    }

    private volatile boolean running;

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void run() {

        while (running) {

            try {
                Reference reference = remove(50);
                if (null != reference) {
                    HeapAnalyzerReference heapAnalyzerReference = (HeapAnalyzerReference) reference;
                    Long constructorId = heapAnalyzerReference.getConstructorId();
                    heapData.get(constructorId).collectObject(heapAnalyzerReference.getObjectSize());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

    }

    public Map<Long, ClassHeapData> getHeapData() {
        //return new HashMap<Long, ClassHeapData>(heapData);
        return heapData;
    }

}
