package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import lombok.Getter;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.DOCX_FORMATS;

@Getter
@Service
public class DocxTextExtractor implements FileTextExtractable {

    private final List<String> formats = DOCX_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws IOException {
        XWPFDocument doc = new XWPFDocument(is);
        XWPFWordExtractor we = new XWPFWordExtractor(doc);
        String text = we.getText();
        we.close();
        return text;
    }
}
