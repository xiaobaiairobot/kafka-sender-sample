package com.yunli.bigdata.example.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yunli.bigdata.example.config.KafkaConfiguration;
import com.yunli.bigdata.example.service.EventService;

/**
 * @author david
 * @date 2020/7/28 7:41 下午
 */
@Service
public class EventServiceImpl implements EventService {

  private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final KafkaConfiguration kafkaConfiguration;

  private final ExecutorService fixedExecutor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<Runnable>(1024), new ThreadFactoryBuilder()
      .setNameFormat("worker_pool").build(), new ThreadPoolExecutor.AbortPolicy());


  @Autowired
  public EventServiceImpl(KafkaTemplate<String, String> kafkaTemplate, KafkaConfiguration kafkaConfiguration) {
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaConfiguration = kafkaConfiguration;
  }

  @Override
  public void sendMessage() {
    SendWorker sendWorker = new SendWorker(this.kafkaTemplate, this.kafkaConfiguration);
    fixedExecutor.execute(sendWorker);
  }
}
