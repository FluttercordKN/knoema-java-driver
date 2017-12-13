package com.knoema.series;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AttributesMap<T> implements Map<String, T> {
    public final String[] keys;
    public final T[] values;

    public AttributesMap(String[] keys, T[] values) {
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
    public boolean containsKey(Object o) {
        if (o != null) {
            for (String k : keys) {
                if (o.equals(k))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        if (o != null) {
            for (T v : values) {
                if (o.equals(v))
                    return true;
            }
        }
        return false;
    }

    @Override
    public T get(Object o) {
        if (o != null) {
            for (int i = 0; i < keys.length; i++) {
                if (o.equals(keys[i]))
                    return values[i];
            }
        }
        return null;
    }

    @Override
    public T put(String s, T t) {
        if (s != null) {
            for (int i = 0; i < keys.length; i++) {
                if (s.equals(keys[i])) {
                    return values[i] = t;
                }
            }
        }
        return null;
    }

    @Override
    public T remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return new ArraySet<>(keys);
    }

    @Override
    public Collection<T> values() {
        return Arrays.asList(values);
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return new EntrySet<>(keys, values);
    }
}
