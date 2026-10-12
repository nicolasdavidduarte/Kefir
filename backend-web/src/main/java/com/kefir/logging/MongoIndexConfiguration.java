package com.kefir.logging;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoIndexConfiguration {

    @Bean
    public ApplicationRunner createApplicationLogIndexes(MongoTemplate mongoTemplate){
        return args -> mongoTemplate.indexOps(ApplicationLog.class)
                .createIndex(new Index().on("timestamp", Sort.Direction.DESC).named("application_log_timestamp_idx"));
    }
}
