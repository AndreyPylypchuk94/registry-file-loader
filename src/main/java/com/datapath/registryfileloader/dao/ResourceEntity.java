package com.datapath.registryfileloader.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResourceEntity {
    @JsonProperty("_id")
    private String id;
    private String title;
    private String url;
    private String filePath;
    private String datasetTitle;
    private String datasetUrl;
    private boolean downloaded;
}
