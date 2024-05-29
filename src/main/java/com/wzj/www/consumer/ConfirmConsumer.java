package com.wzj.www.consumer;

import com.wzj.www.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: 确认消费者
 */
@Slf4j
@Component
public class ConfirmConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void queueDelayed(Message msg, String msgStr) {
        log.info("confirm消费者：当前时间：{},消费confirm队列信息：{}",
                new Date(),
                msgStr);
    }
}
