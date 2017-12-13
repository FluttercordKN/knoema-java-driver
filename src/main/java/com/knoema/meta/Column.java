package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@JsonIgnoreProperties
public class Column {
    public String name;
    public int order;
    public String type;

    public Column() {
        order = 0;
    }
}
