package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class MetadataChanges extends HashMap<String, DimensionChanges> {

    public MetadataChanges() {
        super();
    }
}
