package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoema.core.PivotMemberListDeserializer;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class PivotRequestItem {
    public String dimensionId;
    @JsonDeserialize(using = PivotMemberListDeserializer.class)
    public List<Object> members;
    public String uiMode;

    public PivotRequestItem() {
        members = new ArrayList<>();
    }
}
