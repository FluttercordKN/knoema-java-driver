package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class FullDimensionRequest {
    public ArrayList<DimensionRequestItem> dimensionRequest;
    public int calendar;
    public Date startDate;
    public Date endDate;
    public Integer dateColumn;
    public ArrayList<String> frequency;
    public ArrayList<TimeseriesAttributeModel> timeseriesAttributes;

    public FullDimensionRequest() {
        dimensionRequest = new ArrayList<>();
    }
}
