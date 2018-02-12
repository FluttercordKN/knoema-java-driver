package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataItemType {
    Detail(0),
    Time(1),
    Measure(2);

    private final int value;

    DataItemType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
