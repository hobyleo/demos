package com.hoby.controller;

import com.hoby.constant.KafkaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaozh
 * @since 2024-07-19
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/send")
    public void send() {
        kafkaTemplate.send(KafkaConstant.MY_TOPIC_NAME,  "test", "测试kafka消息");
    }
}
