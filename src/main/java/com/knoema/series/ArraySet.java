package com.knoema.series;

import com.knoema.core.Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ArraySet<T> implements Set<T> {

    private final T[] values;

    public ArraySet(T[] values) {
        this.values = values;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public boolean isEmpty() {
        return values.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o != null) {
            for (T v : values) {
                if (o.equals(v))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.asList(values).iterator();
    }

    @Override
    public Object[] toArray() {
        return values.clone();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return Utils.toArray(values, t1s);
    }

    @Override
    public boolean add(T t) {
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
    public boolean addAll(Collection<? extends T> collection) {
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
