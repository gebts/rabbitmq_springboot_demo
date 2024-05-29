package com.wzj.www.controller;

import com.wzj.www.callback.ProducerCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import static com.wzj.www.config.ConfirmConfig.CONFIRM_EXCHANGE;
import static com.wzj.www.config.ConfirmConfig.CONFIRM_ROUTING_KEY;

@Slf4j
@RequestMapping("confirm")
@RestController
public class ConfirmController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 引入回调实例并设置进rabbitTemplate
     */
    @Autowired
    private ProducerCallback producerCallback;


    @PostConstruct
    public void init(){
        this.rabbitTemplate.setConfirmCallback(producerCallback);
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setReturnsCallback(producerCallback);
    }

    @GetMapping("/sendmsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("confirm生产者发送给confirm队列的消息：{}", msg);

        //配置correlationData信息
        CorrelationData successData = new CorrelationData();
        successData.setId("1");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE, CONFIRM_ROUTING_KEY, msg,successData);

        //设置错误的交换机，测试投递失败情况
        CorrelationData failData = new CorrelationData();
        failData.setId("2");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE+"2", CONFIRM_ROUTING_KEY, msg,failData);
    }

    @GetMapping("/sendmsg/return/{msg}")
    public void sendMsgReturn(@PathVariable String msg) {
        //配置correlationData信息
        CorrelationData successData = new CorrelationData();
        successData.setId("return1");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE, CONFIRM_ROUTING_KEY, msg+"_return1",successData);
        log.info("confirm生产者：发送给confirm队列的消息：{}", msg+"_return1");


        //设置错误的路由键，测试投递到队列失败情况
        CorrelationData failData = new CorrelationData();
        failData.setId("return2");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE, CONFIRM_ROUTING_KEY+"return2", msg+"_return2",failData);
        log.info("confirm生产者：发送给confirm队列的消息：{}", msg+"_return2");
    }
}
