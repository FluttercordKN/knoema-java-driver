package com.knoema.series;

import com.knoema.Frequency;

import java.util.Map;

public class TimeSeriesId {
    public final Frequency freq;
    public final AttributesMap<Integer> keys;
    public final AttributesMap<Object> attributes;

    public TimeSeriesId(Frequency frequency, AttributesMap<Integer> keys, AttributesMap<Object> attributes) {
        this.freq = frequency;
        this.keys = keys;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Frequency.toString(freq));
        sb.append(": ");
        boolean was = false;
        for (Map.Entry<String, Object> a : this.attributes.entrySet()) {
            if (was)
                sb.append(", ");
            else
                was = true;
            sb.append(a.getKey());
            sb.append("=");
            sb.append(a.getValue());
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = freq.getValue();
        for (Integer d : keys.values)
            result = result * 17 + d;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        TimeSeriesId tsId = (obj instanceof TimeSeriesId) ? ((TimeSeriesId)obj) : null;
        if (tsId == null)
            return false;

        if (freq != tsId.freq)
            return false;

        Integer[] d1 = tsId.keys.values;
        Integer[] d2 = keys.values;
        if (d1.length != d2.length)
            return false;

        for (int i = 0; i < d1.length; i++) {
            if (!d1[i].equals(d2[i]))
                return false;
        }

        return true;
    }
}
