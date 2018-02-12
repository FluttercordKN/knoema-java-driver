package com.knoema.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
@JsonTypeName("TimeSeries")
public class TimeSeriesDescriptor extends SearchItem {
    public DatasetDescriptor dataset;
    public ArrayList<DimensionMemberDescriptor> dimensions;
    public ArrayList<RegionLink> regionLinks;
    public int timeSeriesKey;
    public String owner;
    public String startDate;
    public String endDate;
    public boolean hasForecasting;
    public boolean isLongTimeSeries;
    public String title;
    public String type;
    public String frequency;
}
