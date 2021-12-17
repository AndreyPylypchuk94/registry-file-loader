package com.datapath.registryfileloader.service.extract;

import com.datapath.registryfileloader.dao.ResourceEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Path.of;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

@Slf4j
@Service
@AllArgsConstructor
public class FileTextExtractService {

    private final List<FileTextExtractable> extractors;

    public String extract(ResourceEntity d) throws Exception {
        if (isNull(d.getFilePath())) return null;

        String format = substringAfterLast(d.getFilePath(), ".");

        if (isEmpty(format)) {
            log.warn("Not extracted path format [{} {}]. Use {}", d.getId(), d.getFilePath(), d.getFormat());
            format = d.getFormat();

            if (isEmpty(format)) {
                log.warn("Not found format [{}]", d.getId());
                return null;
            }
        }

        final String clearFormat = clearFormat(format);

        Optional<FileTextExtractable> extractor = extractors.stream()
                .filter(e -> e.processable(clearFormat))
                .findFirst();

        if (extractor.isEmpty()) {
            log.warn("Not found extractor for format {}", clearFormat);
            return null;
        }

        Path path = of(d.getFilePath());
        InputStream inputStream = newInputStream(path);

        String text;
        try {
            text = extractor.get().extract(inputStream, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            inputStream.close();
        }

        return text;
    }

    private String clearFormat(String format) {
        return format.replace(".", "").toUpperCase();
    }
}
