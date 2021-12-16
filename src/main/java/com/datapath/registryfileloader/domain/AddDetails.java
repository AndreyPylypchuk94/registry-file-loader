package com.datapath.registryfileloader.domain;

import lombok.Data;

@Data
public class AddDetails {
    private String dateCreated;
    private String dateModified;
    private String metadataDateModified;
    private String format;
}
