package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSeriesRawData {
    @JsonIgnore
    public List<DimensionItem> dimensions;
    public AttributeValues timeSeriesAttributes;

    public TimeSeriesRawData() {
    }

    @JsonAnyGetter
    public Map<String, DimensionItem> getDimensions() {
        HashMap<String, DimensionItem> result = null;
        if (dimensions != null) {
            result = new HashMap<>();
            for (DimensionItem dimension : dimensions)
                result.put(dimension.dimensionId, dimension);
        }
        return result;
    }

    @JsonAnySetter
    public void setDimension(String dimensionId, DimensionItem dimension) {
        dimension.dimensionId = dimensionId;
        if (dimensions == null)
            dimensions = new ArrayList<>();
        dimensions.add(dimension);
    }

}
