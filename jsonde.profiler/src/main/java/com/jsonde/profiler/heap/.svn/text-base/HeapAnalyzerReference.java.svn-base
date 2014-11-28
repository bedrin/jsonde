package com.jsonde.profiler.heap;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class HeapAnalyzerReference extends WeakReference<Object> {

    private final long constructorId;
    private final long objectSize;

    public HeapAnalyzerReference(Object referent, ReferenceQueue<Object> q, long constructorId, long objectSize) {
        super(referent, q);
        this.constructorId = constructorId;
        this.objectSize = objectSize;
    }

    public long getConstructorId() {
        return constructorId;
    }

    public long getObjectSize() {
        return objectSize;
    }

}
