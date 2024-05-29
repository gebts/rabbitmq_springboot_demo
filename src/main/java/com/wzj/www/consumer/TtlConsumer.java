package com.wzj.www.consumer;

import com.wzj.www.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TtlConsumer {

    @RabbitListener(queues = TtlQueueConfig.QUEUE_DEAD_LETTER)
    public void queueDeadLetter(Message msg, String msgStr) {
        log.info("当前时间： {},收到死信队列信息:{}", new Date(), msgStr);
    }

    @RabbitListener(queues = TtlQueueConfig.QUEUE_DELAYED)
    public void queueDelayed(Message msg, String msgStr) {
        log.info("当前时间： {},收到delayed队列信息:{}", new Date(), msgStr);
    }
}
