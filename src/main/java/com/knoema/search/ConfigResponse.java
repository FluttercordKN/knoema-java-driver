package com.knoema.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class ConfigResponse {
    public String searchHost;
    public String communityId;
    public String accessToken;
}
