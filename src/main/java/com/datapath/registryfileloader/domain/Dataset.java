package com.datapath.registryfileloader.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dataset {
    private String url;
    private String manager;
}
