package com.yunli.bigdata.example.service.impl;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.luben.zstd.Zstd;
import com.yunli.bigdata.example.config.KafkaConfiguration;
import com.yunli.bigdata.example.util.ZstdCompressionProcessor;


/**
 * @author david
 * @date 2021/5/20 9:54 上午
 */
@Component
public class KafkaSentLogger {

  private final Logger logger = LoggerFactory.getLogger(KafkaSentLogger.class);

  private final KafkaConfiguration kafkaConfiguration;

  private final ZstdCompressionProcessor compressionProcessor;


  @Autowired
  public KafkaSentLogger(KafkaConfiguration kafkaConfiguration) {
    this.kafkaConfiguration = kafkaConfiguration;
    this.compressionProcessor = new ZstdCompressionProcessor();
  }

  @KafkaListener(topics = "${sourceTopicName}", groupId = "${targetTopicGroup}")
  public void listenTopic(ConsumerRecord<String, byte[]> record) {
    byte[] decompressData = this.decompress(record.value());
//     byte[] decompressData = compressionProcessor.decompress(record.value());
//    byte[] decompressData = record.value();
    String strInfo = new String(decompressData, StandardCharsets.UTF_8);
    logger.info("sent message :" + strInfo);
  }

  private byte[] decompress(byte[] input) {
    if (input == null || input.length == 0) {
      return input;
    }
    long len = Zstd.decompressedSize(input);
    return Zstd.decompress(input, (int) len);
  }
}
