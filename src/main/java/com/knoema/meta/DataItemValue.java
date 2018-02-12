package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoema.core.DataItemValueDeserializer;

import java.util.Map;

@JsonDeserialize(using = DataItemValueDeserializer.class)
@JsonIgnoreProperties
public abstract class DataItemValue {

    @JsonIgnore
    public FieldValues fields;
    @JsonIgnore
    public String name;
    @JsonIgnore
    public final DataItemType type;

    public  DataItemValue(DataItemType type) {
        this.type = type;
    }

    @JsonAnyGetter
    public Map<String, Object> getFields() {
        return fields;
    }

    @JsonAnySetter
    public void setField(String name, Object value) {
        if (fields == null)
            fields = new FieldValues();
        fields.put(name, value);
    }
}
