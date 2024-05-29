package com.wzj.www.consumer;

import com.wzj.www.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: 报警消费者
 */
@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void warningConsume(Message msg, String msgStr) {
        log.info("warning消费者：发现不可路由消息：{}",
                msgStr);
    }
}
