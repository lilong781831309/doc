package org.xinhua.example.spring.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TopicConfig {

    public static final String EXCHANGE_TOPIC_MSG = "exchange.topic.msg";

    public static final String QUEUE_TOPIC_MSG_ACCURATE = "queue.topic.msg.accurate";
    public static final String QUEUE_TOPIC_MSG_SINGLE = "queue.topic.msg.single";
    public static final String QUEUE_TOPIC_MSG_ANY = "queue.topic.msg.any";

    public static final String ROUTING_KEY_MSG_ACCURATE = "routing.key.msg.accurate";
    public static final String ROUTING_KEY_MSG_SINGLE = "routing.key.msg.*";
    public static final String ROUTING_KEY_MSG_ANY = "routing.key.msg.#";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_TOPIC_MSG);
    }

    @Bean
    public Queue topicQueueAccurate() {
        return new Queue(QUEUE_TOPIC_MSG_ACCURATE);
    }

    @Bean
    public Queue topicQueueSingle() {
        return new Queue(QUEUE_TOPIC_MSG_SINGLE);
    }

    @Bean
    public Queue topicQueueAny() {
        return new Queue(QUEUE_TOPIC_MSG_ANY);
    }

    @Bean
    public Binding bindTopicQueueAccurate() {
        return BindingBuilder.bind(topicQueueAccurate()).to(topicExchange()).with(ROUTING_KEY_MSG_ACCURATE);
    }

    @Bean
    public Binding bindTopicQueueSingle() {
        return BindingBuilder.bind(topicQueueSingle()).to(topicExchange()).with(ROUTING_KEY_MSG_SINGLE);
    }

    @Bean
    public Binding bindTopicQueueAny() {
        return BindingBuilder.bind(topicQueueAny()).to(topicExchange()).with(ROUTING_KEY_MSG_ANY);
    }

}
