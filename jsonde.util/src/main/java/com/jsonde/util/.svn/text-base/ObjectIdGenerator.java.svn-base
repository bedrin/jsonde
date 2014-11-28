package com.jsonde.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectIdGenerator<T> {

    private AtomicLong sequence = new AtomicLong();
    private Map<ObjectWrapper<T>, Long> objectIds = new HashMap<ObjectWrapper<T>, Long>();

    public synchronized long getId(T object) {

        ObjectWrapper<T> objectWrapper = wrap(object);

        if (!objectIds.containsKey(objectWrapper)) {
            objectIds.put(objectWrapper, sequence.getAndIncrement());
        }

        return objectIds.get(objectWrapper);

    }

    public synchronized long pollId(T object) throws ObjectIsAbsentException {

        ObjectWrapper<T> objectWrapper = wrap(object);

        if (objectIds.containsKey(objectWrapper)) {
            return objectIds.get(objectWrapper);
        } else {
            throw new ObjectIsAbsentException();
        }

    }

    @SuppressWarnings("unchecked")
    private ObjectWrapper<T> wrap(T object) {
        if (object instanceof ObjectWrapper) {
            return (ObjectWrapper<T>) object;
        } else {
            return new ObjectWrapper<T>(object);
        }
    }

    public static <M, N> Pair<M, N> pair(M m, N n) {
        return new Pair<M, N>(m, n);
    }

    public static class Pair<M, N> extends ObjectWrapper<Pair<M, N>> {

        private final M m;
        private final N n;

        private Pair(M m, N n) {
            this.m = m;
            this.n = n;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (m != null ? m != pair.m : pair.m != null) return false;
            if (n != null ? n != pair.n : pair.n != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = m != null ? System.identityHashCode(m) : 0;
            result = 31 * result + (n != null ? System.identityHashCode(n) : 0);
            return result;
        }

    }

    private static class ObjectWrapper<T> {

        private final T object;

        private ObjectWrapper(T object) {
            this.object = object;
        }

        @SuppressWarnings("unchecked")
        protected ObjectWrapper() {
            object = (T) this;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(object);
        }

        @Override
        public boolean equals(Object obj) {

            if (null == object) {
                return null == obj;
            } else {
                return object == obj;
            }

        }
    }

}
