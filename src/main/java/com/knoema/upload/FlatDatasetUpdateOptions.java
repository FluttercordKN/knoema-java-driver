package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FlatDatasetUpdateOptions {
    Overwrite(0),
    Append(1);

    private final int value;

    FlatDatasetUpdateOptions(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
