package com.yunli.bigdata.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yunli.bigdata.example.service.EventService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author david
 * @date 2020/7/28 7:37 下午
 */
@Api(tags = "event-kafka-sample", value = "消息测试")
@RequestMapping(value = "/kafka/test")
@RestController
@CrossOrigin
public class EventController {

  private final EventService eventService;

  @Autowired
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @ApiOperation(value = "发送测试消息", notes = "发送测试消息", nickname = "sendKafkaMessage")
  @GetMapping(value = "")
  public ResponseEntity<String> sendKafkaMessage() {
    try {
      eventService.sendMessage();
      return ResponseEntity.ok().body("success");
    } catch (Exception ex) {
      ex.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ex.getMessage());
    }
  }


  @ApiOperation(value = "测试收消息", notes = "测试收消息", nickname = "testReceiveMessage")
  @GetMapping(value = "receive")
  public ResponseEntity<String> testReceiveMessage() {
    try {
      eventService.sendMessage();
      return ResponseEntity.ok().body("success");
    } catch (Exception ex) {
      ex.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ex.getMessage());
    }
  }
}
