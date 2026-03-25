package com.fitness.aiservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean  // declares a queue named "activity.queue" in rabbitMQ
    public Queue activityQueue(){
        return new Queue(queue,true); // "durable:true" mentions message stays even after rabbitMQ restarts
    }

    @Bean
    public DirectExchange activityExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding activityBinding(Queue activityQueue,DirectExchange activityExchange){
        return BindingBuilder.bind(activityQueue).to(activityExchange).with(routingKey);
    }

    @Bean  // "java objects to json" before sending them to rabbitMQ (default raw byte sends)
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}





//✅  understanding (corrected clean version)
//
//        In config, we create Queue, Exchange, and Binding
//→ This tells RabbitMQ:
//
//        “This queue exists”
//        “This exchange exists”
//        “This queue is connected to this exchange with this routing key”
//
//        ✔️ Perfect.
//
//✅ What happens when you send data
//
//You said:
//
//        “we send data to exchange with routing key → binding matches → data goes to queue”
//
//        ✔️ This is 100% correct — just making it sharper:
//
//        🔄 Exact flow
//1. You send message → Exchange
//2. Exchange checks routingKey
//3. Binding compares routingKey
//4. If match → message goes to Queue
//5. Queue stores the message
