package com.thoughtmechanix.organization.events.source;

import com.thoughtmechanix.organization.events.model.OrganizationChangeModel;
import com.thoughtmechanix.organization.utils.UserContext;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {
  private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);
  private final StreamBridge streamBridge;


  public SimpleSourceBean(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public void publishOrganizationChange(String action, String organizationId) {
    System.err.println("kafka " + action + ", " + organizationId);
    logger.debug("Sending Kafka message {} for Organiztion Id: {}", action, organizationId);
    OrganizationChangeModel change = new OrganizationChangeModel(
        OrganizationChangeModel.class.getTypeName(),
        action,
        organizationId,
        UserContext.getCorrelationId()
    );
    System.err.println("kafka end");
    streamBridge.send(KafkaBindingNames.SUPPLIER, change);
  }
}
