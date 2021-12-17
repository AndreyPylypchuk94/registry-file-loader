package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import lombok.Getter;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.XLSX_FORMATS;

@Getter
@Service
public class XlsxTextExtractor implements FileTextExtractable {

    private final List<String> formats = XLSX_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(is);
        XSSFExcelExtractor we = new XSSFExcelExtractor(wb);
        String text = we.getText();
        we.close();
        return text;
    }
}
