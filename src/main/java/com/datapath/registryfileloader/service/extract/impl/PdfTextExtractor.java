package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import com.itextpdf.text.pdf.PdfReader;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.PDF_FORMATS;
import static com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage;

@Getter
@Service
public class PdfTextExtractor implements FileTextExtractable {

    private final List<String> formats = PDF_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws IOException {
        PdfReader reader = new PdfReader(is);
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            builder.append(getTextFromPage(reader, i));
        }
        reader.close();
        return builder.toString().trim();
    }
}
