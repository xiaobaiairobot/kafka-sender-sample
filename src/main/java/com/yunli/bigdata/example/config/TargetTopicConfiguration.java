package com.yunli.bigdata.example.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author david
 * @date 2020/7/29 11:10 上午
 */
@Configuration
public class TargetTopicConfiguration implements InitializingBean {

  private final KafkaConfiguration kafkaConfiguration;

  @Autowired
  public TargetTopicConfiguration(KafkaConfiguration kafkaConfiguration) {
    this.kafkaConfiguration = kafkaConfiguration;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    System.setProperty("targetTopicName", kafkaConfiguration.getTargetTopic());
    System.setProperty("targetTopicGroup", kafkaConfiguration.getTargetGroupId());

    System.setProperty("sourceTopicName", kafkaConfiguration.getSourceTopic());
  }
}
