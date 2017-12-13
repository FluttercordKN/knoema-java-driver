package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DimensionField {
    public int key;
    public String name;
    public String displayName;
    public DimensionFieldType type;
    public String locale;
    public int baseKey;
    public boolean isSystemField;

    public DimensionField() {
        key = 0;
        baseKey = 0;
        isSystemField = false;
    }
}
