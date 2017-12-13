package com.knoema.series;

import java.util.*;

public class Series implements Map<Date, Double> {

    private final Date[] keys;
    private final Double[] values;

    public Series(Date[] keys, Double[] values) {
        this.keys = keys;
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
    public boolean containsKey(Object o) {
        int index = Arrays.binarySearch(keys, o);
        return index >= 0;
    }

    @Override
    public boolean containsValue(Object o) {
        if (o != null) {
            for (Double d : values) {
                if (o.equals(d))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Double get(Object o) {
        int index = Arrays.binarySearch(keys, o);
        if (index >= 0)
            return values[index];
        return null;
    }

    @Override
    public Double put(Date date, Double aDouble) {
        int index = Arrays.binarySearch(keys, date);
        if (index >= 0)
            return values[index] = aDouble;
        return null;
    }

    @Override
    public Double remove(Object o) {
        int index = Arrays.binarySearch(keys, o);
        if (index >= 0) {
            Double result = values[index];
            values[index] = null;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends Date, ? extends Double> map) {

    }

    @Override
    public void clear() {
        for (int i = 0; i < values.length; i++) {
            values[i] = null;
        }
    }

    @Override
    public Set<Date> keySet() {
        return new ArraySet<>(keys);
    }

    @Override
    public Collection<Double> values() {
        return Arrays.asList(values);
    }

    @Override
    public Set<Entry<Date, Double>> entrySet() {
        return new EntrySet<>(keys, values);
    }
}
