package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
@JsonIgnoreProperties
public class DataItemDetail extends DataItemValue {
    public String value;

    public DataItemDetail() {
        super(DataItemType.Detail);
    }
}
