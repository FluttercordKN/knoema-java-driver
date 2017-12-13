package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class TimeSeriesItem {
    public String datasetId;
    public String frequency;
    public Date startDate;
    public Date endDate;
    public int timeseriesKey;
    public ArrayList<DimensionItemForTimeSeriesItem> metadata;

    public TimeSeriesItem() {
        metadata = new ArrayList<>();
    }
}
