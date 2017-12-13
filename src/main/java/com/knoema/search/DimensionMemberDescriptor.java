package com.knoema.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DimensionMemberDescriptor {
    public String dimension;
    public int key;
    public String name;
}
