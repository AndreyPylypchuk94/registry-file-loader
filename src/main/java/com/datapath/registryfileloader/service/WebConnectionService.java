package com.datapath.registryfileloader.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import static org.jsoup.Connection.Method.GET;
import static org.jsoup.Jsoup.parse;

@Slf4j
@Service
public class WebConnectionService {

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36";

    public Response get(String url) {
        log.info("Fetching {}", url);
        try {
            return Jsoup.connect(url)
                    .maxBodySize(0)
                    .ignoreContentType(true)
                    .timeout(0)
                    .followRedirects(true)
                    .userAgent(USER_AGENT)
                    .method(GET)
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Document getDocument(String url) {
        return parse(get(url).body(), url);
    }
}
