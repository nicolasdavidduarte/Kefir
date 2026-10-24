package com.kefir.logging;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class ApplicationLogService {

    private final ApplicationLogRepository applicationLogRepository;

    public ApplicationLogService(ApplicationLogRepository applicationLogRepository){
        this.applicationLogRepository = applicationLogRepository;
    }

    public void recordError(Exception ex) {

        ApplicationLog applicationLog = new ApplicationLog(
                null,
                Instant.now(),
                "ERROR",
                ex.getClass().getName(),
                "Unhandled exception",
                ex.getClass().getName()
        );

        ApplicationLog saved = applicationLogRepository.save(applicationLog);

    }

    public List<ApplicationLog> findErrors(ApplicationLog log){
        return applicationLogRepository.findByLevelOrderByTimestampDesc("ERROR");
    }
}
