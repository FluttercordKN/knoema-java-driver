package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DimensionRequestItem {
    public String dimensionId;
    public ArrayList<Integer> members;

    public DimensionRequestItem() {
        members = new ArrayList<>();
    }
}
