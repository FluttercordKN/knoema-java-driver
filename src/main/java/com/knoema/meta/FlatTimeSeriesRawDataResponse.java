package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class FlatTimeSeriesRawDataResponse {
    public String continuationToken;
    public ArrayList<FlatTimeSeriesRawData> data;

    public FlatTimeSeriesRawDataResponse() {
        data = new ArrayList<>();
    }
}
