package com.wzj.www.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("ttl")
@RestController
public class SendMsgController {

    private final static String EXCHANGE_NORMAL = "exchange_normal";
    private final static String ROUTING_KEY_NORMAL_A = "routing_key_normal_a";
    private final static String ROUTING_KEY_NORMAL_B = "routing_key_normal_b";
    private final static String ROUTING_KEY_NORMAL_C = "routing_key_normal_c";

    private final static String EXCHANGE_DELAYED = "exchange_delayed";
    private final static String ROUTING_KEY_DELAYED = "routing_key_delayed";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendmsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("ttl生产者发送给正常队列A、B的消息：{}", msg);
        rabbitTemplate.convertAndSend(EXCHANGE_NORMAL, ROUTING_KEY_NORMAL_A, msg);
        rabbitTemplate.convertAndSend(EXCHANGE_NORMAL, ROUTING_KEY_NORMAL_B, msg);
    }

    @GetMapping("/sendmsg/{msg}/{ttl}")
    public void sendMsgTtl(@PathVariable String msg, @PathVariable String ttl) {
        log.info("ttl生产者发送给正常队列C的消息：{},ttl：{}", msg, ttl);
        rabbitTemplate.convertAndSend(EXCHANGE_NORMAL, ROUTING_KEY_NORMAL_C, msg, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttl);
            return correlationData;
        });
    }

    /**
     * 延迟队列
     */
    @GetMapping("/sendmsg/delayed/{msg}/{time}")
    public void sendMsgDelayed(@PathVariable String msg, @PathVariable int time) {
        log.info("ttl生产者发送给delayed队列的消息：{},ttl：{}", msg, time);
        rabbitTemplate.convertAndSend(EXCHANGE_DELAYED, ROUTING_KEY_DELAYED, msg, correlationData -> {
            //设置延迟时间
            correlationData.getMessageProperties().setDelay(time);
            return correlationData;
        });
    }
}
