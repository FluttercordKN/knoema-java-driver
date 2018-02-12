package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoema.core.PivotMemberListDeserializer;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class PivotDimensionItem {
    public String dimensionId;
    @JsonDeserialize(using = PivotMemberListDeserializer.class)
    public ArrayList<Object> members;

    public PivotDimensionItem() {
        members = new ArrayList<>();
    }
}
