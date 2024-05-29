package com.wzj.www.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description: 消息回调处理类
 */
@Slf4j
@Component
public class ProducerCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //correlationData信息需要生产者手动设置
        if (ack) {
            log.info("消息确认：交换机成功收到消息，消息id为：{}", correlationData.getId());
        } else {
            log.info("消息确认：交换机未收到消息，消息id为：{}，失败原因为：{}", correlationData.getId(), cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息回执：交换机{}使用路由键{}路由到队列失败，消息：{}，原因：{}",
                returned.getExchange(),
                returned.getRoutingKey(),
                new String(returned.getMessage().getBody())
                , returned.getReplyText());
    }
}
