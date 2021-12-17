package com.datapath.registryfileloader.service;

import com.datapath.registryfileloader.dao.DaoService;
import com.datapath.registryfileloader.dao.ResourceEntity;
import com.datapath.registryfileloader.domain.AddDetails;
import com.datapath.registryfileloader.domain.Dataset;
import com.datapath.registryfileloader.domain.Resource;
import com.datapath.registryfileloader.service.extract.DocumentDataExtractService;
import com.datapath.registryfileloader.service.extract.FileTextExtractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonMaximumSizeExceededException;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.join;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileHandleService {

    private static final String FILES_DIRECTORY = "files";

    @Value("${registry.search.url}")
    private String url;

    private final WebConnectionService connectionService;
    private final DocumentDataExtractService extractService;
    private final DaoService daoService;
    private final FileTextExtractService textExtractService;

    private final MongoTemplate template;

    public void load() {
        log.info("Started");

        int page = 1;

        Document pageDocument;

        do {
            String pageUrl = url + page;

            pageDocument = connectionService.getDocument(pageUrl);

            if (extractService.datasetIsNotPresent(pageDocument)) break;

            List<Dataset> pageDataset = extractService.extract(pageDocument);

            pageDataset.forEach(this::process);

            page++;
        } while (extractService.datasetIsPresent(pageDocument));


        log.info("Finished");
    }

    private void process(Dataset d) {
        try {
            Document document = connectionService.getDocument(d.getUrl());
            List<Resource> resources = extractService.extractResources(document);
            resources.forEach(r -> process(r, d));
        } catch (Exception e) {
            log.error("error dataset fetching", e);
        }
    }

    private void process(Resource r, Dataset d) {
        ResourceEntity entity = daoService.getById(r.getDataId());
        if (nonNull(entity) && entity.isDownloaded()) return;

        entity = new ResourceEntity();
        entity.setId(r.getDataId());
        entity.setTitle(r.getTitle());
        entity.setUrl(r.getUrl());
        entity.setDatasetTitle(d.getTitle());
        entity.setDatasetUrl(d.getUrl());

        try {
            log.info("Downloading {}", r.getUrl());
            String fileName = join("/", FILES_DIRECTORY, extractService.getResourceName(r));
            InputStream in = new URL(r.getUrl()).openStream();
            copy(in, Paths.get(fileName), REPLACE_EXISTING);
            entity.setFilePath(fileName);
            entity.setDownloaded(true);
        } catch (IOException e) {
            log.error("error file downloading", e);
            entity.setDownloaded(false);
        }

        daoService.save(entity);
    }

    public void update() {
        updateDate();
        extractText();
    }

    private void updateDate() {
        log.info("Updating dates started");

        List<ResourceEntity> documents;

        while (!isEmpty(documents = daoService.getWithEmptyDate())) {
            documents.forEach(d -> {
                try {
                    String url = d.getUrl().replaceAll("/download/.+", "");
                    Document document = connectionService.getDocument(url);
                    AddDetails addDetails = extractService.extractAddDetails(document);
                    d.setDateCreated(addDetails.getDateCreated());
                    d.setDateModified(addDetails.getDateModified());
                    d.setMetadataDateModified(addDetails.getMetadataDateModified());
                    d.setFormat(addDetails.getFormat());
                } catch (Exception e) {
                    log.warn("Page not fetched", e);
                }
                d.setAddDetailsExtracted(true);
                daoService.save(d);
            });
        }

        log.info("Updating dates finished");
    }

    private void extractText() {
        log.info("Extracting text started");

        List<ResourceEntity> documents;

        while (!isEmpty(documents = daoService.getWithEmptyText())) {
            documents.forEach(d -> {
                log.info("Extracting in {}", d.getId());
                try {
                    d.setText(textExtractService.extract(d));
                } catch (Exception e) {
                    log.warn("Text not extracted. Id {}", d.getId());
                    log.warn("Reason:", e);
                }

                d.setTextExtracted(true);

                try {
                    daoService.save(d);
                } catch (BsonMaximumSizeExceededException e) {
                    d.setTextTooLarge(true);
                    d.setText(null);
                    daoService.save(d);
                }
            });
        }

        log.info("Extracting text finished");
    }
}
