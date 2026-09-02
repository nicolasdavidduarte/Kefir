package com.kefir.infrastructure.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfig {

  @Bean
  public KafkaAdmin.NewTopics securityEventsTopic() {
    return new KafkaAdmin.NewTopics(
        TopicBuilder.name("bank.loan-security.events").partitions(3).replicas(1).build(),
        TopicBuilder.name("bank.account-security.events").partitions(3).replicas(1).build());
  }
}
