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
    private String datasetManager;
    private String datasetUrl;
    private boolean downloaded;
    private String dateCreated;
    private String dateModified;
    private String metadataDateModified;
    private String format;
    private String text;
    private boolean addDetailsExtracted;
    private boolean textExtracted;
    private boolean textTooLarge;
}
