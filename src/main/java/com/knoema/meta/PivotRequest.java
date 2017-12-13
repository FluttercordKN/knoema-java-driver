package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class PivotRequest {
    public List<PivotRequestItem> header;
    public List<PivotRequestItem> stub;
    public List<PivotRequestItem> filter;
    public List<PivotRequestTimeSeriesAttributes> timeSeriesAttributes;
    public String dataset;
    public List<String> frequencies;

    public PivotRequest()
    {
        header = new ArrayList<>();
        stub = new ArrayList<>();
        filter = new ArrayList<>();
    }
}
