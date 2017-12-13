package com.knoema.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DimensionChanges {
    public ArrayList<String> addedFields;
    public int addedMembers;
    public int updatedMembers;
    public int totalMembersInUpdate;

    public DimensionChanges() {
        addedFields = new ArrayList<>();
    }
}
