package com.knoema.series;

import com.knoema.Frequency;
import com.knoema.core.Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class TimeSeriesFrame implements Map<TimeSeriesId, TimeSeriesValues> {

    private final Map<String, Integer> dimensionIdsMap;
    private final String[] dimensions;
    private final Map<String, Integer>[] dimensionKeyMaps;
    private final String[] attributes;
    private final Map<TimeSeriesId, TimeSeriesValues> values;

    public TimeSeriesFrame(Map<String, Integer> dimensionIdsMap, String[] dimensions, Map<String, Integer>[] dimensionKeyMaps, String[] attributes, Map<TimeSeriesId, TimeSeriesValues> values) {
        this.dimensionIdsMap = dimensionIdsMap;
        this.dimensions = dimensions;
        this.attributes = attributes;
        this.dimensionKeyMaps = dimensionKeyMaps;
        this.values = values;
    }

    public String[] getDimensions() {
        return dimensions;
    }

    public TimeSeriesId makeId(String frequency, Integer[] keys, Object[] attributes) {
        return makeId(Frequency.parse(frequency), keys, attributes);
    }

    public TimeSeriesId makeId(Frequency frequency, Integer[] keys, Object[] attributes) {
        if (attributes == null) {
            attributes = Arrays.copyOf(keys, this.attributes.length);
        }

        return new TimeSeriesId(frequency,
                new AttributesMap<>(this.dimensions, keys),
                new AttributesMap<>(this.attributes, attributes));
    }

    public TimeSeriesId makeId(Map<String, Object> properties) {
        Frequency frequency = Frequency.Annual;

        Integer[] dims = new Integer[this.dimensions.length];
        String[] attrs = new String[this.attributes.length];
        for (Map.Entry<String, Object> pair : properties.entrySet()) {
            String name = pair.getKey();
            Object value = pair.getValue();
            if ("frequency".equalsIgnoreCase(name)) {
                String val = Utils.toString(value);
                frequency = Frequency.parse(val);
            }
            else {
                Integer dimIndex = this.dimensionIdsMap.get(name);
                if (dimIndex == null)
                    dimIndex = -1;
                int attrIndex = dimIndex >= 0 ? dimIndex : Utils.indexOf(this.attributes, name, this.dimensions.length);
                if (dimIndex >= 0) {
                    if (value instanceof Integer) {
                        Integer dimKey = (Integer)value;
                        dims[dimIndex] = dimKey;
                        if (attrIndex >= 0)
                            attrs[attrIndex] = Integer.toString(dimKey);
                    }
                    else {
                        String val = Utils.toString(value);
                        Integer dimKey = (this.dimensionKeyMaps[dimIndex]).get(val);
                        if (dimKey != null)
                            dims[dimIndex] = dimKey;
                        attrs[attrIndex] = val;
                    }
                }
                else {
                    if (attrIndex >= 0)
                        attrs[attrIndex] = Utils.toString(value);
                }
            }
        }

        return makeId(frequency, dims, attrs);
    }

    private TimeSeriesId toKey(Object o) {
        if (o instanceof TimeSeriesId)
            return (TimeSeriesId)o;
        return makeId(Utils.toPropertySet(o));
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return values.containsKey(toKey(o));
    }

    @Override
    public boolean containsValue(Object o) {
        return values.containsValue(o);
    }

    @Override
    public TimeSeriesValues get(Object o) {
        return values.get(toKey(o));
    }

    @Override
    public TimeSeriesValues put(TimeSeriesId timeSeriesId, TimeSeriesValues timeSeriesValues) {
        return values.put(timeSeriesId, timeSeriesValues);
    }

    @Override
    public TimeSeriesValues remove(Object o) {
        return values.remove(toKey(o));
    }

    @Override
    public void putAll(Map<? extends TimeSeriesId, ? extends TimeSeriesValues> map) {
        values.putAll(map);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public Set<TimeSeriesId> keySet() {
        return values.keySet();
    }

    @Override
    public Collection<TimeSeriesValues> values() {
        return values.values();
    }

    @Override
    public Set<Entry<TimeSeriesId, TimeSeriesValues>> entrySet() {
        return values.entrySet();
    }
}
