package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class FileProperties {
    public int size;
    public String name;
    public String location;
    public String type;
}
