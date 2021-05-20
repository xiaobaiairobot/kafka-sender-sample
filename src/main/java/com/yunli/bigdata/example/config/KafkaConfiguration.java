package com.yunli.bigdata.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author david
 * @date 2020/7/28 7:43 下午
 */
@Configuration
public class KafkaConfiguration {

  @Value("${spring.kafka.bootstrap-servers}")
  private String server;

  @Value("${spring.kafka.source-topic}")
  private String sourceTopic;

  @Value("${spring.kafka.target-topic}")
  private String targetTopic;

  @Value("${spring.kafka.consumer.group-id}")
  private String targetGroupId;

  @Value("${spring.kafka.send-times}")
  private Integer sendTimes;

  public String getSourceTopic() {
    return sourceTopic;
  }

  public void setSourceTopic(String sourceTopic) {
    this.sourceTopic = sourceTopic;
  }

  public String getTargetTopic() {
    return targetTopic;
  }

  public void setTargetTopic(String targetTopic) {
    this.targetTopic = targetTopic;
  }

  public Integer getSendTimes() {
    return sendTimes;
  }

  public void setSendTimes(Integer sendTimes) {
    this.sendTimes = sendTimes;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getTargetGroupId() {
    return targetGroupId;
  }

  public void setTargetGroupId(String targetGroupId) {
    this.targetGroupId = targetGroupId;
  }
}
