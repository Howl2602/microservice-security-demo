package com.dien.inventoryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    TopicExchange orderExchange(
            @Value("${microshield.rabbit.order-exchange}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    Queue inventoryOrderQueue(
            @Value("${microshield.rabbit.inventory-queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    Binding inventoryOrderBinding(
            Queue inventoryOrderQueue,
            TopicExchange orderExchange,
            @Value("${microshield.rabbit.order-routing-key}") String routingKey) {
        return BindingBuilder
                .bind(inventoryOrderQueue)
                .to(orderExchange)
                .with(routingKey);
    }
}
