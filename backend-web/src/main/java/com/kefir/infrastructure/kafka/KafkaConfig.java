package com.kefir.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic securityEventsTopic() {
    return TopicBuilder.name("bank.loan-security.events").partitions(3).replicas(1).build();
  }
}
