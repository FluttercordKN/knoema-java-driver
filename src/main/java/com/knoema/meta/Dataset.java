package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class Dataset {
    public String id;
    public String name;
    public String description;
    public String ref;
    public String type;
    public String hasGeoDimension;
    public String regionDimensionId;
    public Date publicationDate;
    public Dimension[] dimensions;
    public Column[] columns;
    public TimeSeriesAttribute[] timeSeriesAttributes;

    public Dataset() {
    }
}
