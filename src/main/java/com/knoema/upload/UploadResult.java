package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class UploadResult {
    public int id;
    public String datasetId;
    public String status;
    public ArrayList<String> errors;
    public String url;
    public DatasetUploadReport report;

    public UploadResult() {
        errors = new ArrayList<>();
    }
}
