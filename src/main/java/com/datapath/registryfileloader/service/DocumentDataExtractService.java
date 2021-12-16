package com.datapath.registryfileloader.service;

import com.datapath.registryfileloader.domain.AddDetails;
import com.datapath.registryfileloader.domain.Dataset;
import com.datapath.registryfileloader.domain.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.time.LocalTime.MIN;
import static java.time.format.DateTimeFormatter.*;
import static java.util.Locale.forLanguageTag;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Service
public class DocumentDataExtractService {

    private final static String DATASET_LIST_SELECTOR = "#all-results > div.search-container__item";
    private final static String DATASET_URL_SELECTOR = "h4 > a";
    private final static String DATASET_TITLE_SELECTOR = "p.search-container__item-description";

    private final static String RESOURCE_LIST_SELECTOR = "li.resource-list__item";
    private final static String RESOURCE_TITLE_SELECTOR = "div.resource-list__item-container-title a";
    private final static String RESOURCE_URL_SELECTOR = "div.resource-list__item-download > button > a:contains(Завантажити)";

    private final static String DATE_CREATED_SELECTOR = ".additional-info-container__item .dataset-label:matches(Створено) + .dataset-details";
    private final static String DATE_MODIFIED_SELECTOR = ".additional-info-container__item .dataset-label:matches(Востаннє оновлено) + .dataset-details";
    private final static String METADATA_DATE_MODIFIED_SELECTOR = ".additional-info-container__item .dataset-label:matches(Мета-дані востаннє оновлено) + .dataset-details";
    private final static String FORMAT_SELECTOR = ".additional-info-container__item .dataset-label:matches(Формат) + .dataset-details";

    private final static List<DateTimeFormatter> DATE_CREATED_FORMATS = List.of(
            ofPattern("MMM dd, yyyy", forLanguageTag("uk-UA")),
            ofPattern("MMM d, yyyy", forLanguageTag("uk-UA"))
    );

    private final static List<DateTimeFormatter> DATE_MODIFIED_FORMATS = List.of(
            ofPattern("MMM dd, yyyy, HH:mm (zzz)", forLanguageTag("uk-UA")),
            ofPattern("MMM d, yyyy, HH:mm (zzz)", forLanguageTag("uk-UA"))
    );

    private static final Map<String, String> UKR_MONTH_MAPPINGS = new HashMap<>() {{
        put("Січень", "січ.");
        put("Лютий", "лют.");
        put("Березень", "бер.");
        put("Квітень", "квіт.");
        put("Травень", "трав.");
        put("Червень", "черв.");
        put("Липень", "лип.");
        put("Серпень", "серп.");
        put("Вересень", "вер.");
        put("Жовтень", "жовт.");
        put("Листопад", "лист.");
        put("Грудень", "груд.");
    }};

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

    public AddDetails extractAddDetails(Document document) {
        AddDetails details = new AddDetails();

        String dateCreatedText = replaceMonth(document.select(DATE_CREATED_SELECTOR).text());
        String dateModifiedText = replaceMonth(document.select(DATE_MODIFIED_SELECTOR).text());
        String metadataDateModifiedText = replaceMonth(document.select(METADATA_DATE_MODIFIED_SELECTOR).text());
        String formatText = document.select(FORMAT_SELECTOR).text();

        String dateCreated = handleDate(dateCreatedText, DATE_CREATED_FORMATS,
                (d, f) -> LocalDateTime.of(LocalDate.parse(d, f), MIN).format(ISO_DATE_TIME));
        String dateModified = handleDate(dateModifiedText, DATE_MODIFIED_FORMATS,
                (d, f) -> ZonedDateTime.parse(d, f).format(ISO_OFFSET_DATE_TIME));
        String metadataDateModified = handleDate(metadataDateModifiedText, DATE_MODIFIED_FORMATS,
                (d, f) -> ZonedDateTime.parse(d, f).format(ISO_OFFSET_DATE_TIME));

        details.setFormat(formatText);
        details.setDateCreated(dateCreated);
        details.setDateModified(dateModified);
        details.setMetadataDateModified(metadataDateModified);

        return details;
    }

    private String replaceMonth(String date) {
        String month = substringBefore(date, " ");
        return date.replace(month, UKR_MONTH_MAPPINGS.get(month));
    }

    private String handleDate(String data, List<DateTimeFormatter> formats, BiFunction<String, DateTimeFormatter, String> s) {
        String result = null;

        for (DateTimeFormatter f : formats) {
            try {
                result = s.apply(data, f);
                break;
            } catch (DateTimeParseException ignored) {
            }
        }

        return result;
    }
}
