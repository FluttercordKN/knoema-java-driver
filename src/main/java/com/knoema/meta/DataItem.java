package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataItem {

    @JsonIgnore
    public final ArrayList<DataItemValue> values;

    public DataItem() {
        values = new ArrayList<>();
    }

    @JsonAnyGetter
    Map<String, Object> getFields() {
        HashMap<String, Object> result = new HashMap<>();
        for (DataItemValue value : values) {
            if (value instanceof DataItemDetail)
                result.put(value.name, ((DataItemDetail)value).value);
            else
                result.put(value.name, value);
        }
        return result;
    }

    @JsonAnySetter
    public void setField(String key, DataItemValue value) {
        if (value == null)
            value = new DataItemDetail();
        value.name = key;
        values.add(value);
    }
}
