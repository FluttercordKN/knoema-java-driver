package com.knoema.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DatasetDescriptor {
    public String id;
    public String title;
    public String source;
    public String dataUrl;
    public String accessedOn;
    public String sourceUrl;
    public String sourceId;
    public Date updatedOn;
    public String datasetType;
}
