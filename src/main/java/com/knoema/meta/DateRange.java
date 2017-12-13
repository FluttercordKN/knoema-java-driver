package com.knoema.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class DateRange {
    public int calendar;
    public Date startDate;
    public Date endDate;
    public ArrayList<String> frequencies;

    public DateRange() {
        frequencies = new ArrayList<>();
    }
}
