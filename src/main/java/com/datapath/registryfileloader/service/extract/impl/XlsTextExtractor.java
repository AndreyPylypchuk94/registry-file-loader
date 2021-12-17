package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import lombok.Getter;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.XLS_FORMATS;

@Getter
@Service
public class XlsTextExtractor implements FileTextExtractable {

    private final List<String> formats = XLS_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws IOException {
        ExcelExtractor wd = new ExcelExtractor(new HSSFWorkbook(is));
        String text = wd.getText();
        wd.close();
        return text;
    }
}
