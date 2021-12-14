package com.datapath.registryfileloader.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@AllArgsConstructor
public class DaoService {

    private static final String COLLECTION_NAME = "registry-files";

    private final MongoTemplate template;
    private final ObjectMapper mapper;

    public ResourceEntity getById(String id) {
        return template.findOne(new Query(where("_id").is(id)), ResourceEntity.class, COLLECTION_NAME);
    }

    @SneakyThrows
    public void save(ResourceEntity entity) {
        template.save(mapper.writeValueAsString(entity), COLLECTION_NAME);
    }
}
