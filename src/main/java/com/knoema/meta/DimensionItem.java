package com.knoema.meta;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DimensionItem {
    @JsonIgnore
    public String dimensionId;
    public int key;
    public String name;
    @JsonIgnore
    public FieldValues metadataFields;

    public DimensionItem() {
        key = 0;
        metadataFields = new FieldValues();
    }
    @JsonAnyGetter
    Map<String, Object> getFields() {
        return metadataFields;
    }
    @JsonAnySetter
    public void setField(String key, Object value) {
        metadataFields.put(key, value);
    }
}
