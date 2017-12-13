package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class RegularTimeSeriesRawData extends TimeSeriesRawData {
    public Date startDate;
    public Date endDate;
    public String frequency;
    public String unit;
    public float scale;
    public String mnemonics;
    public Double[] values;

    public RegularTimeSeriesRawData() {
        super();
        scale = 0f;
    }
}
