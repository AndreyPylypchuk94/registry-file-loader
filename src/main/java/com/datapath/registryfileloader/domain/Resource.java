package com.datapath.registryfileloader.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Resource {
    private String datasetTitle;
    private String title;
    private String url;
    private String dataId;
}
