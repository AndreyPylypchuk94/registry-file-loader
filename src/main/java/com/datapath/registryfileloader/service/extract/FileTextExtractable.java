package com.datapath.registryfileloader.service.extract;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface FileTextExtractable {
    String extract(InputStream is, Path filePath) throws Exception;

    List<String> getFormats();

    default boolean processable(String format) {
        return getFormats().contains(format);
    }
}
