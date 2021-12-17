package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import lombok.Getter;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.ODF_FORMATS;

@Getter
//@Service
public class OdfTextExtractor implements FileTextExtractable {

    private final List<String> formats = ODF_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws Exception {
        return null;
    }
}
