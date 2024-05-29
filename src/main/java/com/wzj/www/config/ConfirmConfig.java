package com.wzj.www.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 确认机制配置
 */
@Configuration
public class ConfirmConfig {

    public final static String CONFIRM_EXCHANGE = "confirm_exchange";
    public final static String CONFIRM_QUEUE = "confirm_queue";
    public final static String CONFIRM_ROUTING_KEY = "confirm_routing_key";


    public final static String BACKUP_EXCHANGE = "backup_exchange";
    public final static String BACKUP_QUEUE = "backup_queue";
    public final static String WARNING_QUEUE = "warning_queue";

    /**
     * 确认交换机
     */
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE)
                //绑定备份交换机
                .withArgument("alternate-exchange",BACKUP_EXCHANGE)
                .build();
    }

    /**
     * 确认队列
     */
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    /**
     * 确认队列绑定交换机
     */
    @Bean
    public Binding confirmBinding(@Qualifier("confirmExchange") DirectExchange confirmExchange,
                                  @Qualifier("confirmQueue") Queue confirmQueue){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    /**
     * 备份交换机
     */
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE).build();
    }

    /**
     * 备份队列
     */
    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    /**
     * 备份队列绑定备份交换机
     */
    @Bean
    public Binding backupBinding(@Qualifier("backupExchange") FanoutExchange exchange,
                                 @Qualifier("backupQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange);
    }

    /**
     * 报警队列
     */
    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    /**
     * 报警队列绑定备份交换机
     */
    @Bean
    public Binding warningBinding(@Qualifier("backupExchange") FanoutExchange exchange,
                                 @Qualifier("warningQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange);
    }
}
