package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import lombok.Getter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.DOC_FORMATS;

@Getter
@Service
public class DocTextExtractor implements FileTextExtractable {

    private final List<String> formats = DOC_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws IOException {
        WordExtractor wd = new WordExtractor(is);
        String text = wd.getText();
        wd.close();
        return text;
    }
}
