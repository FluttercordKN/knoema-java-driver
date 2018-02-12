package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DimensionMember {
    public int key;
    public String name;
    public int level;
    public boolean hasData;
    public Map<String, Object> fields;

    public DimensionMember() {
        key = 0;
        level = 0;
        hasData = false;
    }
}
