package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.ODS_FORMATS;

@Getter
//@Service
public class OdsTextExtractor implements FileTextExtractable {

    private final List<String> formats = ODS_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws IOException {
        return null;
    }
}
