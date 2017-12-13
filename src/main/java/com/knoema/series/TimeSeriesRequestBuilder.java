package com.knoema.series;

import com.knoema.Client;
import com.knoema.core.Utils;
import com.knoema.meta.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TimeSeriesRequestBuilder {

    final private Client client;
    final private Dataset dataset;
    final private Map<String, String> dimensionIdsMap;
    final private Map<String, Map<String, Integer>> dimensions;

    private String timeRange;
    private List<String> frequencies;

    public TimeSeriesRequestBuilder(Client client, Dataset dataset, Map<String, Object> request) {

        this.client = client;
        this.dataset = dataset;
        dimensions = new HashMap<>();
        dimensionIdsMap = new HashMap<>();
        if (dataset.dimensions != null) {
            for (Dimension dim : dataset.dimensions) {
                dimensionIdsMap.put(dim.name.replace(' ', '_').replace('-', '_'), dim.id);
            }
            for (Dimension dim : dataset.dimensions) {
                dimensionIdsMap.put(dim.name, dim.id);
            }
            for (Dimension dim : dataset.dimensions) {
                dimensionIdsMap.put(dim.id.replace('-', '_'), dim.id);
            }
            for (Dimension dim : dataset.dimensions) {
                dimensionIdsMap.put(dim.id, dim.id);
            }
        }
        init(request);
    }

    public Map<String, Map<String, Integer>> getDimensionMembersMapping() {
        return Collections.unmodifiableMap(dimensions);
    }

    public Map<String, String> getDimensionIdsMap() {
        return Collections.unmodifiableMap(dimensionIdsMap);
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String value) {
        timeRange = value;
    }

    public List<String> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(List<String> value) {
        frequencies = value;
    }

    private String getDimensionId(String dimension) {
        return dimensionIdsMap.get(dimension);
    }

    public Iterable<String> get(String dimension) {
        String dimId = getDimensionId(dimension);
        if (!Utils.isEmpty(dimId)) {
            Map<String, Integer> keys = dimensions.get(dimId);
            if (keys != null)
                return Collections.unmodifiableSet(keys.keySet());
        }
        return null;
    }

    public void put(String dimension, Iterable<String> value) {
        String dimId = getDimensionId(dimension);
        if (!Utils.isEmpty(dimId))
        {
            if (value == null)
                dimensions.remove(dimId);
            else
            {
                Map<String, Integer> keys = dimensions.get(dimId);
                if (keys == null) {
                    keys = new HashMap<>();
                    dimensions.put(dimId, keys);
                }
                for (String key : value) {
                    keys.put(key, 0);
                }
            }
        }
    }

    private void init(Map<String, Object> request) {
        for (Map.Entry<String, Object> pair : request.entrySet()) {
            String key = pair.getKey();
            Object val = pair.getValue();
            if ("timeRange".equalsIgnoreCase(key))
                setTimeRange(Utils.toString(val));
            else {
                List<String> strings = Utils.toStringList(val);
                if ("frequency".equalsIgnoreCase(key)) {
                    setFrequencies(strings);
                }
                else {
                    put(key, strings);
                }
            }
        }
    }
    public PivotRequest getRequest() throws InterruptedException, ExecutionException {

        PivotRequest request = new PivotRequest();
        request.dataset = dataset.id;
        if (frequencies != null)
            request.frequencies = frequencies;

        PivotRequestItem timeItem = new PivotRequestItem();
        timeItem.dimensionId = "Time";
        if (Utils.isEmpty(timeRange)) {
            timeItem.uiMode = "allData";
            timeItem.members = null;
        }
        else {
            timeItem.uiMode = "range";
            timeItem.members.add(timeRange);
        }
        request.header.add(timeItem);

        for (Map.Entry<String, Map<String, Integer>> dimPair : dimensions.entrySet()) {
            String dimId = dimPair.getKey();
            Map<String, Integer> dimKeys = dimPair.getValue();
            if (dimKeys == null || dimKeys.isEmpty())
                continue;

            Dimension dimMeta = client.getDatasetDimension(dataset.id, dimId).get();
            Object idVal;
            String keyVal;
            for (DimensionMember member : dimMeta.items) {
                if (member.fields != null && (idVal = member.fields.get("id")) != null && dimKeys.containsKey(Utils.toString(idVal)))
                    keyVal = Utils.toString(idVal);
                else if (dimKeys.containsKey(member.name))
                    keyVal = member.name;
                else if (dimKeys.containsKey(Integer.toString(member.key)))
                    keyVal = Integer.toString(member.key);
                else
                    keyVal = null;

                if (!Utils.isEmpty(keyVal))
                    dimKeys.put(keyVal, member.key);
            }

            PivotRequestItem dimItem = null;
            for (Integer memberKey : dimKeys.values()) {
                if (memberKey != 0) {
                    if (dimItem == null) {
                        dimItem = new PivotRequestItem();
                        dimItem.dimensionId = dimId;
                        request.stub.add(dimItem);
                    }
                    dimItem.members.add(memberKey);
                }
            }
        }

        return request;
    }
}
