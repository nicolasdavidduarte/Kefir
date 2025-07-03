package org.kefir.infrastructure.messaging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Component
public class SnsPublisher {

  private static final Logger log = LogManager.getLogger(SnsPublisher.class);
  private final SnsClient snsClient;
  private final String topicArn;

  public SnsPublisher(SnsClient snsClient, @Value("${sns.topic.arn}") String topicArn) {
    this.snsClient = snsClient;
    this.topicArn = topicArn;
  }

  public void publishLoanCreated(Integer loanId, Double amount) {
    log.info("publishLoanCreated called with id={} amount={}", loanId, amount);

    String message = String.format("New loan created: ID=%d, amount=%.2f", loanId, amount);

    PublishRequest request = PublishRequest.builder().message(message).topicArn(topicArn).build();

    try {
      log.info("Sending message to SNS: {}", message);
      PublishResponse response = snsClient.publish(request);
      log.info("Message sent, ID: {}", response.messageId());
    } catch (Exception e) {
      log.error("Error when sending message SNS", e);
    }
  }
}
