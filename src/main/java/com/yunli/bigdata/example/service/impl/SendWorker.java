package com.yunli.bigdata.example.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.yunli.bigdata.example.config.KafkaConfiguration;
import com.yunli.bigdata.example.domain.TopicMessage;
import com.yunli.bigdata.example.util.DateUtil;
import com.yunli.bigdata.example.util.JsonUtil;
import com.yunli.bigdata.example.util.ZstdCompressionProcessor;


/**
 * @author david
 * @date 2020/7/29 10:44 上午
 */
public class SendWorker implements Runnable {

  private final Logger logger = LoggerFactory.getLogger(SendWorker.class);


  private final KafkaConfiguration kafkaConfiguration;

  private final KafkaProducer kafkaProducer;

  private final ZstdCompressionProcessor compressionProcessor;

  private AtomicInteger index = new AtomicInteger(0);

  public SendWorker(KafkaTemplate<String, String> kafkaTemplate,
      KafkaConfiguration kafkaConfiguration) {
    this.kafkaConfiguration = kafkaConfiguration;
    this.kafkaProducer = buildKafkaSink();
    compressionProcessor = new ZstdCompressionProcessor();
  }

  @Override
  public void run() {
    logger.info("消息发送开始" + kafkaConfiguration.getSendTimes());
    for (int i = 0; i < kafkaConfiguration.getSendTimes(); i++) {
      kafkaProducer
          .send(new ProducerRecord<>(kafkaConfiguration.getSourceTopic(), "test", generateMessage()), new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
              if (e != null) {
                logger.debug(e.getMessage());
              } else {
                logger.debug(String.format("receive the result: %s", recordMetadata));
              }
            }
          });
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    logger.info("消息发送已完成" + kafkaConfiguration.getSendTimes());
  }


  private KafkaProducer buildKafkaSink() {
    Properties props = new Properties();
    props.put("bootstrap.servers", this.kafkaConfiguration.getServer());
    props.put("allow.auto.create.topics", "false");
    props.put("request.required.acks", "0");
    props.put("producer.type", "async");
    props.put("key.serializer", StringSerializer.class.getName());
    props.put("value.serializer", ByteArraySerializer.class.getName());
    props.setProperty("client.id", UUID.randomUUID().toString());
    return new KafkaProducer<>(props);
  }

  private byte[] generateMessage() {
    TopicMessage topicMessage = new TopicMessage();
    topicMessage.setType("rain");
    List<Map<String, Object>> data = new ArrayList<>();
    Map<String, Object> item1 = new HashMap<>();
    item1.put("stcd", "1000001");
    // item1.put("tm", DateUtil.toStandardString(new Date()));
    item1.put("tm", new Date());
    double min = 3.0;
    double max = 11.0;
    double drp = min + new Random().nextDouble() * (max - min);
    item1.put("drp", drp);
    item1.put("dyp", new Random().nextDouble());
    data.add(item1);
    topicMessage.setData(data);
    String message = JsonUtil.writeValueAsString(topicMessage);
     return compressionProcessor.compress(message.getBytes(StandardCharsets.UTF_8));
  }

  private byte[] getRandomMessage() {
    // id name sex age birthday
    Double dInfo = Math.floor(Math.random() * (100 - 1)) + 1;
//    String strMessage = String.format("%d,\"%s\",\"%s\",%d,\"%s\"", index.incrementAndGet(), "张三" + index.get(), "男",
//        dInfo.intValue(), DateUtil.toSimpleString(new Date()));
    // String strMessage = String.format("\"%s\"", "张三" + index.incrementAndGet());
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("stcd", String.format("100000%s", new Random().nextInt(3)));
    map.put("tm", new Date());
    map.put("drp", new Random().nextInt(25));
    map.put("dyp", 20);
    List<Map<String, Object>> listData = new ArrayList<>();
    listData.add(map);
    Map<String, Object> dataObject = new HashMap<>();
    dataObject.put("type", "rain");
    dataObject.put("data", listData);
    String json = JsonUtil.writeValueAsString(dataObject);
    System.out.println(json);
    return json.getBytes(StandardCharsets.UTF_8);
    // return json;
    // String strMessage = String.format("\"%s\"", "acfun");
    // System.out.println(strMessage);
    // return strMessage;
  }

  private String getRandomJson() {
    String template = "{\n"
        + "\t\"name\": \"zhangsan\",\n"
        + "\t\"age\": %d,\n"
        + "\t\"class\": \"yuwen\"\n"
        + "}";
    return String.format(template, new Random().nextInt(20));
  }
}
