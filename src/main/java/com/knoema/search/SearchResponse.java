package com.knoema.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class SearchResponse {
    public ArrayList<SearchItem> items;
    public ArrayList<String> mainQueryRegionIds;
    public ArrayList<RegionLink> mainQueryRegionsList;
    public String queryRegions;
    public ArrayList<RegionLink> queryRegionsList;
}
