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
 * @date 2020/7/28 7:54 下午
 */
@Component
public class KafkaReceiver {

  private final Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

  private final KafkaConfiguration kafkaConfiguration;

  private final ZstdCompressionProcessor compressionProcessor;


  @Autowired
  public KafkaReceiver(KafkaConfiguration kafkaConfiguration) {
    this.kafkaConfiguration = kafkaConfiguration;
    this.compressionProcessor = new ZstdCompressionProcessor();
  }

  @KafkaListener(topics = "${targetTopicName}", groupId = "${targetTopicGroup}")
  public void listenTopic(ConsumerRecord<String, byte[]> record) {
//    byte[] decompressData = this.decompress(record.value());
//     byte[] decompressData = compressionProcessor.decompress(record.value());
    byte[] decompressData = record.value();
    String strInfo = new String(decompressData, StandardCharsets.UTF_8);
    logger.info("received message :" + strInfo);
//    TopicMessage topicMessage = JsonUtil.readValue(strInfo, TopicMessage.class);
//    if (topicMessage.getData().size() > 0) {
//      for (Map<String, Object> item : topicMessage.getData()) {
//        Long tm = (Long) item.get("tm");
//        Date dtTime = new Date(tm);
//        System.out.println(dtTime);
//      }
//    }
  }

  private byte[] decompress(byte[] input) {
    if (input == null || input.length == 0) {
      return input;
    }
    long len = Zstd.decompressedSize(input);
    return Zstd.decompress(input, (int) len);
  }
}
