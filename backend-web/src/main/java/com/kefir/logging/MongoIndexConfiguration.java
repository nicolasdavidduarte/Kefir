package com.kefir.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Slf4j
@Configuration
public class MongoIndexConfiguration {

    @Bean
    public ApplicationRunner createApplicationLogIndexes(
            MongoTemplate mongoTemplate) {

        return args -> {
            try {
                mongoTemplate.indexOps(ApplicationLog.class)
                        .createIndex(
                                new Index()
                                        .on("timestamp", Sort.Direction.DESC)
                                        .named("application_log_timestamp_idx")
                        );

                log.info("MongoDB application log indexes created successfully");
            } catch (Exception ex) {
                log.warn(
                        "Could not create MongoDB application log indexes. "
                                + "Application will continue starting.",
                        ex
                );
            }
        };
    }
}