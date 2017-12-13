package com.knoema.core;

import java.util.HashMap;

public class PropSet extends HashMap<String, Object> {
    public PropSet add(String key, Object value) {
        put(key, value);
        return this;
    }
}
