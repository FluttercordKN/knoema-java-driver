package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DimensionItemForTimeSeriesItem {
    public String dim;
    public int key;
    public String name;

    public DimensionItemForTimeSeriesItem() {
        key = 0;
    }
}
