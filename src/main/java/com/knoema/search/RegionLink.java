package com.knoema.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class RegionLink {
    public int key;
    public String id;
    public String name;
    public String idFromName;
    public String[] parents;
}
