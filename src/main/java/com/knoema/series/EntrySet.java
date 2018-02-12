package com.knoema.series;

import com.knoema.core.Utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EntrySet<K, V> implements Set<Map.Entry<K, V>> {

    private class Entry implements Map.Entry<K, V> {

        private final K k;
        private final V v;
        public Entry(K k, V v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }

        @Override
        public V setValue(V v) {
            return null;
        }
    }

    private class IteratorImpl implements Iterator<Map.Entry<K, V>> {

        private final K[] keys;
        private final V[] values;
        private int current;

        public IteratorImpl(K[] keys, V[] values) {
            this.keys = keys;
            this.values = values;
            this.current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < keys.length;
        }

        @Override
        public Map.Entry<K, V> next() {
            int index = current++;
            return new Entry(keys[index], values[index]);
        }

        @Override
        public void remove() {

        }
    }

    private final K[] keys;
    private final V[] values;

    public EntrySet(K[] keys, V[] values) {
        this.keys = keys;
        this.values = values;
    }

    @Override
    public int size() {
        return keys.length;
    }

    @Override
    public boolean isEmpty() {
        return keys.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            return false;
        return false;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new IteratorImpl(keys, values);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[keys.length];
        for (int i = 0; i < keys.length; i++)
            result[i] = new Entry(keys[i], values[i]);
        return result;
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return Utils.toArray(toArray(), ts);
    }

    @Override
    public boolean add(Map.Entry<K, V> kvEntry) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }
}
