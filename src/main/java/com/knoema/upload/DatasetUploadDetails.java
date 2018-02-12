package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DatasetUploadDetails {
    public String datasetId;
    public String datasetName;
    public String source;
    public String description;
    public String datasetRef;
    public Date publicationDate;
    public Date accessedOn;
}
