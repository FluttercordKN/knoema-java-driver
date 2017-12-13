package com.knoema.series;

import com.knoema.Frequency;
import com.knoema.TimeFormat;
import com.knoema.core.Triplet;
import com.knoema.meta.*;

import java.util.*;

public class TimeSeriesFrameBuilder {

    private final Map<String, Integer>[] dimensionKeyMaps;
    private final String[] dimensions;
    private final Map<String, Integer> dimensionIdsMap;
    private final String[] attributes;
    private final Map<String, Integer> attributeIds;
    private final Map<TimeSeriesId, TimeSeriesValues> values;
    private final Map<Triplet<Date, Date, Frequency>, Date[]> timeRangeCache;

    @SuppressWarnings("unchecked")
    public TimeSeriesFrameBuilder(Dataset dataset, Map<String, String> dimensionIdsMap, Map<String, Map<String, Integer>> dimensionMembersMapping) {
        List<String> dims = new ArrayList<>();
        for (Dimension dim : dataset.dimensions)
            dims.add(dim.id);
        this.dimensions = dims.toArray(new String[dims.size()]);

        Map<String, Integer> dimensionIds = new HashMap<>();
        for (int i = 0; i < dimensions.length; i++) {
            dimensionIds.put(dimensions[i], i);
        }
        if (dimensionIdsMap != null) {
            for (Map.Entry<String, String> p : dimensionIdsMap.entrySet()) {
                if (!dimensionIds.containsKey(p.getKey())) {
                    Integer dimIndex = dimensionIds.get(p.getValue());
                    if (dimIndex != null) {
                        dimensionIds.put(p.getKey(), dimIndex);
                    }
                }
            }
        }
        this.dimensionIdsMap = dimensionIds;

        List<String> attrs = new ArrayList<>(Arrays.asList(dimensions));

        if (dataset.timeSeriesAttributes != null) {
            for (TimeSeriesAttribute attr : dataset.timeSeriesAttributes) {
                attrs.add(attr.name);
            }
        }
        this.attributes = attrs.toArray(new String[attrs.size()]);

        Map<String, Integer> attributeIds = new HashMap<>(dimensionIds);
        for (int i = dimensions.length; i < attributes.length; i++) {
            attributeIds.put(attributes[i], i);
        }
        this.attributeIds = attributeIds;

        this.dimensionKeyMaps = (Map<String, Integer>[])new Map[dimensions.length];

        for (int i = 0; i < dimensionKeyMaps.length; i++) {
            dimensionKeyMaps[i] = dimensionMembersMapping.get(dimensions[i]);
            if (dimensionKeyMaps[i] == null) {
                dimensionKeyMaps[i] = new HashMap<>();
            }
        }

        this.values = new HashMap<>();
        this.timeRangeCache = new HashMap<>();
    }

    public void addRange(Iterable<RegularTimeSeriesRawData> seriesEnum) {
        for (RegularTimeSeriesRawData seriesItem : seriesEnum)
            add(seriesItem);
    }

    public void add(RegularTimeSeriesRawData item) {
        Integer[] dimensions = new Integer[this.dimensions.length];
        Object[] attributes = new Object[this.attributes.length];
        for (DimensionItem dim : item.dimensions) {
            Integer dimIndex = this.dimensionIdsMap.get(dim.dimensionId);
            dimensions[dimIndex] = dim.key;
            attributes[dimIndex] = dim.name;
            this.dimensionKeyMaps[dimIndex].put(dim.name, dim.key);
        }

        if (item.timeSeriesAttributes != null) {
            for (Map.Entry<String, String> attr : item.timeSeriesAttributes.entrySet()) {
                Integer attrIndex = attributeIds.get(attr.getKey());
                attributes[attrIndex] = attr.getValue();
            }
        }

        Frequency frequency = Frequency.parse(item.frequency);
        Triplet<Date, Date, Frequency> rangeTuple = new Triplet<>(item.startDate, item.endDate, frequency);
        Date[] timeRange = timeRangeCache.get(rangeTuple);
        if (timeRange == null) {
            List<Date> expandedRange = TimeFormat.INVARIANT_TIME_FORMAT.expandRangeSelection(item.startDate, item.endDate, frequency);
            timeRange = expandedRange.toArray(new Date[expandedRange.size()]);
            timeRangeCache.put(rangeTuple, timeRange);
        }


        Series series = new Series(timeRange, item.values);

        TimeSeriesValues values = new TimeSeriesValues(
                item,
                frequency,
                new AttributesMap<>(this.dimensions, dimensions),
                new AttributesMap<>(this.attributes, attributes),
                series);

        this.values.put(values, values);
    }

    public TimeSeriesFrame getResult() {
        return new TimeSeriesFrame(dimensionIdsMap, dimensions, dimensionKeyMaps, attributes, values);
    }
}
