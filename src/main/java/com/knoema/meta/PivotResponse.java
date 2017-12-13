package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class PivotResponse {
    public ArrayList<PivotDimensionItem> header;
    public ArrayList<PivotDimensionItem> stub;
    public ArrayList<PivotDimensionItem> filter;

    @JsonProperty("data")
    public PivotDataTuples tuples;

    public PivotResponse() {
        header = new ArrayList<>();
        stub = new ArrayList<>();
        filter = new ArrayList<>();
        tuples = new PivotDataTuples();
    }
}
