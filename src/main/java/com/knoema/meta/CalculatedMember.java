package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class CalculatedMember {
    public String name;
    public int key;
    public ArrayList<String> formula;
    public String transform;

    public CalculatedMember() {
        key = 0;
        formula = new ArrayList<>();
    }
}
