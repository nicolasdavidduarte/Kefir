package com.kefir.infrastructure.messaging;

import com.kefir.exceptions.SNSMessageSendingException;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Slf4j
@Component
@EnableAsync
public class SnsPublisher {

  private final SnsClient snsClient;
  private final String topicArn;

  public SnsPublisher(SnsClient snsClient, @Value("${sns.topic.arn}") String topicArn) {
    this.snsClient = snsClient;
    this.topicArn = topicArn;
  }

  @Async
  public void publishLoanCreated(Long loanId, BigDecimal amount) {
    log.info("publishLoanCreated called with id={} amount={}", loanId, amount);

    final String message = String.format("New loan created: ID=%d, amount=%.2f", loanId, amount);

    final PublishRequest request =
        PublishRequest.builder().message(message).topicArn(topicArn).build();

    try {
      if (log.isDebugEnabled()) {
        log.info("Sending message to SNS: {}", message);
      }
      final PublishResponse response = snsClient.publish(request);
      if (log.isInfoEnabled()) {
        log.info("Message sent, ID: {}", response.messageId());
      }
    } catch (SNSMessageSendingException e) {
      if (log.isErrorEnabled()) {
        log.error("Error when sending message SNS: {}", e.getMessage());
      }
      throw e;
    }
  }
}
