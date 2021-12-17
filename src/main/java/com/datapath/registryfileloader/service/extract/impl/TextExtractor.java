package com.datapath.registryfileloader.service.extract.impl;

import com.datapath.registryfileloader.service.extract.FileTextExtractable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.datapath.registryfileloader.Constants.TEXT_FORMATS;
import static java.lang.String.join;

@Slf4j
@Getter
@Service
public class TextExtractor implements FileTextExtractable {

    private final List<Charset> AVAILABLE_CHARSETS = List.of(
            Charset.forName("UTF-8"),
            Charset.forName("windows-1251")
    );

    private final List<String> formats = TEXT_FORMATS;

    @Override
    public String extract(InputStream is, Path filePath) throws IOException {
        for (Charset ch : AVAILABLE_CHARSETS) {
            try {
                List<String> strings = Files.readAllLines(filePath, ch);
                return join("\n", strings);
            } catch (Exception ignored) {
            }
        }
        throw new RuntimeException("Not found charset");
    }
}
