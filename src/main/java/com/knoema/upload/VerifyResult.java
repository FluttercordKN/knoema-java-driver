package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class VerifyResult {
    public Boolean successful;
    public String filePath;
    public ArrayList<String> errorList;
    public Object uploadFormatType;
    public ArrayList<Object> columns;
    public DatasetUploadDetails metadataDetails;
    public FlatDatasetUpdateOptions flatDSUpdateOptions;
    public DatasetUploadReport advanceReport;
}
