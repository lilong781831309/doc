package org.xinua.example.spring.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FanoutConfig {

	public static final String EXCHANGE_FANOUT_MSG = "exchange.fanout.msg";
	public static final String QUEUE_FANOUT_MSG1 = "queue.fanout.msg1";
	public static final String QUEUE_FANOUT_MSG2 = "queue.fanout.msg2";
	public static final String QUEUE_FANOUT_MSG3 = "queue.fanout.msg3";

	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(EXCHANGE_FANOUT_MSG);
	}

	@Bean
	public Queue fanoutQueue1() {
		return new Queue(QUEUE_FANOUT_MSG1);
	}

	@Bean
	public Queue fanoutQueue2() {
		return new Queue(QUEUE_FANOUT_MSG2);
	}

	@Bean
	public Queue fanoutQueue3() {
		return new Queue(QUEUE_FANOUT_MSG3);
	}

	@Bean
	public Binding bindFanoutQueue1() {
		return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
	}

	@Bean
	public Binding bindFanoutQueue2() {
		return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
	}

	@Bean
	public Binding bindFanoutQueue3() {
		return BindingBuilder.bind(fanoutQueue3()).to(fanoutExchange());
	}

}
