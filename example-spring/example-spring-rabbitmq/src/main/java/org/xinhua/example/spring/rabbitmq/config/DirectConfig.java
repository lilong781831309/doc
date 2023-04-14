package org.xinhua.example.spring.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {

    public static final String EXCHANGE_DIRECT_MSG = "exchange.direct.msg";

    public static final String QUEUE_DIRECT_MSG1 = "queue.direct.msg1";
    public static final String QUEUE_DIRECT_MSG2 = "queue.direct.msg2";
    public static final String QUEUE_DIRECT_MSG3 = "queue.direct.msg3";

    public static final String ROUTING_KEY_MSG1 = "routing.key.msg1";
    public static final String ROUTING_KEY_MSG2 = "routing.key.msg2";
    public static final String ROUTING_KEY_MSG3 = "routing.key.msg3";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_DIRECT_MSG);
    }

    @Bean
    public Queue directQueue1() {
        return new Queue(QUEUE_DIRECT_MSG1);
    }

    @Bean
    public Queue directQueue2() {
        return new Queue(QUEUE_DIRECT_MSG2);
    }

    @Bean
    public Queue directQueue3() {
        return new Queue(QUEUE_DIRECT_MSG3);
    }

    @Bean
    public Binding bindDirectQueue1() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with(ROUTING_KEY_MSG1);
    }

    @Bean
    public Binding bindDirectQueue2() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with(ROUTING_KEY_MSG2);
    }

    @Bean
    public Binding bindDirectQueue3() {
        return BindingBuilder.bind(directQueue3()).to(directExchange()).with(ROUTING_KEY_MSG3);
    }

}
