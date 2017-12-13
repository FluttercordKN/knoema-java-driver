package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@SuppressWarnings("CanBeFinal")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class Dimension {
    @SuppressWarnings("WeakerAccess")
    public int key;
    public String id;
    public String name;
    public String description;
    public Boolean isGeo;
    public String datasetId;
    public List<DimensionMember> items;
    public DimensionField[] fields;

    public Dimension() {
        key = 0;
    }
}
