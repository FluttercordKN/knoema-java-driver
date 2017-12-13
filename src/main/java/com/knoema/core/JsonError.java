package com.knoema.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonError extends JsonProcessingException {
    public JsonError(String message, JsonParser jsonParser) {
        super(message, jsonParser.getCurrentLocation());
    }
}
