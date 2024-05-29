package com.wzj.www.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @Description: 交换机、队列配置类，只在此处配置没有消费者并不会生成对应的交换机和队列
 */
@Configuration
public class TtlQueueConfig {

    public final static String EXCHANGE_NORMAL = "exchange_normal";
    public final static String QUEUE_NORMAL_A = "queue_normal_a";
    public final static String QUEUE_NORMAL_B = "queue_normal_b";
    public final static String QUEUE_NORMAL_C = "queue_normal_c";
    public final static String ROUTING_KEY_NORMAL_A = "routing_key_normal_a";
    public final static String ROUTING_KEY_NORMAL_B = "routing_key_normal_b";
    public final static String ROUTING_KEY_NORMAL_C = "routing_key_normal_c";

    public final static String EXCHANGE_DEAD_LETTER = "exchange_dead_letter";
    public final static String QUEUE_DEAD_LETTER = "queue_dead_letter";
    public final static String ROUTING_KEY_DEAD_LETTER = "routing_key_dead_letter";

    public final static String EXCHANGE_DELAYED = "exchange_delayed";
    public final static String QUEUE_DELAYED = "queue_delayed";
    public final static String ROUTING_KEY_DELAYED = "routing_key_delayed";


    /**
     * 声明正常交换机
     */
    @Bean("exchangeNormal")
    public DirectExchange exchangeNormal() {
        return new DirectExchange(EXCHANGE_NORMAL);
    }

    /**
     * 声明正常队列A，并绑定死信交换机
     */
    @Bean("queueNormalA")
    public Queue queueNormalA() {
        //正常队列a对应的死信参数
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", EXCHANGE_DEAD_LETTER);
        args.put("x-dead-letter-routing-key", ROUTING_KEY_DEAD_LETTER);
        //声明队列的TTL为1000毫秒
        args.put("x-message-ttl", 1000);

        return QueueBuilder.durable(QUEUE_NORMAL_A).withArguments(args).build();
    }

    /**
     * 正常队列A绑定正常交换机
     */
    @Bean
    public Binding queueBindingNormalA(@Qualifier("queueNormalA") Queue queueNormalA,
                                       @Qualifier("exchangeNormal") DirectExchange exchangeNormal){
        return BindingBuilder.bind(queueNormalA).to(exchangeNormal).with(ROUTING_KEY_NORMAL_A);
    }

    /**
     * 声明正常队列B，并绑定死信交换机
     */
    @Bean("queueNormalB")
    public Queue queueNormalB() {
        //正常队列b对应的死信参数
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", EXCHANGE_DEAD_LETTER);
        args.put("x-dead-letter-routing-key", ROUTING_KEY_DEAD_LETTER);
        //声明队列的TTL为4000毫秒
        args.put("x-message-ttl", 4000);

        return QueueBuilder.durable(QUEUE_NORMAL_B).withArguments(args).build();
    }

    /**
     * 正常队列B绑定正常交换机
     */
    @Bean
    public Binding queueBindingNormalB(@Qualifier("queueNormalB") Queue queueNormalB,
                                       @Qualifier("exchangeNormal") DirectExchange exchangeNormal){
        return BindingBuilder.bind(queueNormalB).to(exchangeNormal).with(ROUTING_KEY_NORMAL_B);
    }

    /**
     * 声明正常队列C，不配置TTL
     */
    @Bean("queueNormalC")
    public Queue queueNormalC(){
        //配置死信参数
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", EXCHANGE_DEAD_LETTER);
        args.put("x-dead-letter-routing-key", ROUTING_KEY_DEAD_LETTER);
        return QueueBuilder.durable(QUEUE_NORMAL_C).withArguments(args).build();
    }

    /**
     * 正常队列C绑定正常交换机
     */
    @Bean
    public Binding queueBindingNormalC(@Qualifier("queueNormalC") Queue queueNormalC,
                                  @Qualifier("exchangeNormal") DirectExchange exchangeNormal){
        return BindingBuilder.bind(queueNormalC).to(exchangeNormal).with(ROUTING_KEY_NORMAL_C);
    }

    /**
     * 声明死信交换机
     */
    @Bean("exchangeDeadLetter")
    public DirectExchange exchangeDeadLetter() {
        return new DirectExchange(EXCHANGE_DEAD_LETTER);
    }

    /**
     * 声明死信队列
     */
    @Bean("queueDeadLetter")
    public Queue queueDeadLetter() {
        return QueueBuilder.durable(QUEUE_DEAD_LETTER).build();
    }

    /**
     * 死信队列绑定死信交换机
     */
    @Bean
    public Binding queueBindingDeadLetter(@Qualifier("queueDeadLetter") Queue queueDeadLetter,
                                       @Qualifier("exchangeDeadLetter") DirectExchange exchangeDeadLetter){
        return BindingBuilder.bind(queueDeadLetter).to(exchangeDeadLetter).with(ROUTING_KEY_DEAD_LETTER);
    }

    /**
     * 延迟交换机
     */
    @Bean("exchangeDelayed")
    public DirectExchange directExchangeDelayed(){
        //配置为“延迟”类型
        return ExchangeBuilder.directExchange(EXCHANGE_DELAYED).delayed().build();
    }

    /**
     * 延迟队列
     */
    @Bean("queueDelayed")
    public Queue queueDelayed(){
        return QueueBuilder.durable(QUEUE_DELAYED).build();
    }

    /**
     * 延迟队列绑定交换机
     */
    @Bean
    public Binding queueBindingDelayed(@Qualifier("queueDelayed") Queue queueDelayed,
                                       @Qualifier("exchangeDelayed") DirectExchange exchangeDelayed){
        return BindingBuilder.bind(queueDelayed).to(exchangeDelayed).with(ROUTING_KEY_DELAYED);
    }

}
