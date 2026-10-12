package com.kefir.logging;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationLogRepository extends MongoRepository<ApplicationLog, String> {

    public List<ApplicationLog> findByLevelOrderByTimestampDesc(String level);
}
