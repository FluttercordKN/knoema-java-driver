package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DimensionFieldType {
    Boolean(0),
    Integer(1),
    Float(2),
    Double(3),
    DateTime(4),
    Char(5),
    String(6),
    Latitude(7),
    Longitude(8);

    private final int value;

    DimensionFieldType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
