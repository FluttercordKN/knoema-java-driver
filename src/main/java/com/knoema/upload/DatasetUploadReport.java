package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DatasetUploadReport {
    public int addedDataPoints;
    public int updatedDataPoints;
    public int deletedDataPoints;
    public int addedMetadataElements;

    public MetadataChanges metadataChanges;

    public int totalRecords;
    public boolean isFlatDataset;
    public ArrayList<String> uploadComments;
    public boolean isKnoxDataOverwritten;
}
