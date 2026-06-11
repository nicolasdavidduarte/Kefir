package com.kefir.infrastructure.config.aws;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@ConditionalOnProperty(name = "aws.sns.enabled", havingValue = "true")
public class AwsConfig {

  @Bean
  public SnsClient snsClient() {
    return SnsClient.create();
  }
}
