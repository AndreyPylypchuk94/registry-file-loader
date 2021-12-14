package com.datapath.registryfileloader.service;

import com.datapath.registryfileloader.dao.DaoService;
import com.datapath.registryfileloader.dao.ResourceEntity;
import com.datapath.registryfileloader.domain.Dataset;
import com.datapath.registryfileloader.domain.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class FileLoadService {

    private static final String FILES_DIRECTORY = "files";

    @Value("${registry.search.url}")
    private String url;

    private final WebConnectionService service;
    private final DocumentDataExtractService extractService;
    private final DaoService daoService;

    public void load() {
        log.info("Started");

        int page = 1;

        Document pageDocument;

        do {
            String pageUrl = url + page;

            pageDocument = service.getDocument(pageUrl);

            if (extractService.datasetIsNotPresent(pageDocument)) break;

            List<Dataset> pageDataset = extractService.extract(pageDocument);

            pageDataset.forEach(this::process);

            page++;
        } while (extractService.datasetIsPresent(pageDocument));


        log.info("Finished");
    }

    private void process(Dataset d) {
        try {
            Document document = service.getDocument(d.getUrl());
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
}
