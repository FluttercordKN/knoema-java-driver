package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.knoema.meta.AttributeValues;
import com.knoema.meta.FileProperties;
import com.knoema.meta.PostResult;

import java.util.ArrayList;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DatasetUpload {

    public String datasetId;
    public String description;
    public String name;
    public Date pubDate;
    public Date accessedOn;
    public String source;
    public ArrayList<Object> columns;
    public FileProperties fileProperty;
    public Object uploadFormatType;
    public String url;
    public FlatDatasetUpdateOptions flatDSUpdateOptions;
    public AttributeValues metadataFieldValues;
    public Integer startAtRow;

    public DatasetUpload() {
    }

    public DatasetUpload(VerifyResult verifyResult, PostResult postResult) {

        this(verifyResult, postResult, null);
    }

    public DatasetUpload(VerifyResult verifyResult, PostResult postResult, String datasetId) {
        this.datasetId = datasetId;
        fileProperty = postResult.properties;
        uploadFormatType = verifyResult.uploadFormatType;
        columns = verifyResult.columns;
        flatDSUpdateOptions = verifyResult.flatDSUpdateOptions;
        if (verifyResult.metadataDetails != null)
        {
            name = verifyResult.metadataDetails.datasetName;
            description = verifyResult.metadataDetails.description;
            source = verifyResult.metadataDetails.source;
            url = verifyResult.metadataDetails.datasetRef;
            pubDate = verifyResult.metadataDetails.publicationDate;
            accessedOn = verifyResult.metadataDetails.accessedOn;
        }
    }
}
