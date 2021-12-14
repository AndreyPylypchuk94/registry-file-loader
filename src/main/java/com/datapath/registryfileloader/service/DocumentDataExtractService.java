package com.datapath.registryfileloader.service;

import com.datapath.registryfileloader.domain.Dataset;
import com.datapath.registryfileloader.domain.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DocumentDataExtractService {

    private final static String DATASET_LIST_SELECTOR = "#all-results > div.search-container__item";
    private final static String DATASET_URL_SELECTOR = "h4 > a";
    private final static String DATASET_TITLE_SELECTOR = "p.search-container__item-description";

    private final static String RESOURCE_LIST_SELECTOR = "li.resource-list__item";
    private final static String RESOURCE_TITLE_SELECTOR = "div.resource-list__item-container-title a";
    private final static String RESOURCE_URL_SELECTOR = "div.resource-list__item-download > button > a";

    public boolean datasetIsPresent(Document document) {
        return !datasetIsNotPresent(document);
    }

    public boolean datasetIsNotPresent(Document document) {
        return document.select(DATASET_LIST_SELECTOR).isEmpty();
    }

    public List<Dataset> extract(Document document) {
        return document.select(DATASET_LIST_SELECTOR)
                .stream()
                .map(e -> new Dataset(
                        e.select(DATASET_URL_SELECTOR).attr("abs:href"),
                        e.select(DATASET_TITLE_SELECTOR).text()
                )).collect(toList());
    }

    public List<Resource> extractResources(Document document) {
        return document.select(RESOURCE_LIST_SELECTOR).stream()
                .map(r -> new Resource(
                        r.select(RESOURCE_TITLE_SELECTOR).attr("title"),
                        r.select(RESOURCE_URL_SELECTOR).attr("abs:href"),
                        r.attr("data-id")
                )).collect(toList());
    }

    public String getResourceName(Resource resource) {
        return String.join("-", resource.getDataId(), StringUtils.substringAfterLast(resource.getUrl(), "/"));
    }
}
