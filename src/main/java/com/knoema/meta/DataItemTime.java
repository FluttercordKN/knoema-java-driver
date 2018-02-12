package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

@JsonDeserialize
@JsonIgnoreProperties
public class DataItemTime extends DataItemValue {

    public Date date;
    public String frequency;

    public DataItemTime() {
        super(DataItemType.Time);
    }
}
