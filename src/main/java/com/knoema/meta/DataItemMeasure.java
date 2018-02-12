package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
@JsonIgnoreProperties
public class DataItemMeasure extends DataItemValue {

    public Object value;
    public String unit;

    public DataItemMeasure() {
        super(DataItemType.Measure);
    }
}
