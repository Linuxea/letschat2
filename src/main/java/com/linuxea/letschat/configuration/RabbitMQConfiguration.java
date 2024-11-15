package com.linuxea.letschat.configuration;

import com.linuxea.letschat.config.IMRabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConfiguration {

    private final IMRabbitMQConfig imRabbitMQConfig;

    public String exchangeName() {
        return "imExchange";
    }

    public String queueName() {
        return String.format("im_queue_%s", imRabbitMQConfig.getServer());
    }

    public String routingKey() {
        return String.format("im_routing_key_%s", imRabbitMQConfig.getServer());
    }

    @Bean
    public Queue imQueue() {
        String queueName = queueName();
        log.info("queueName: {}", queueName);
        return new Queue(queueName);
    }

    @Bean
    public DirectExchange imExchange() {
        String exchangeName = exchangeName();
        log.info("exchangeName: {}", exchangeName);
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding binding(@Qualifier("imQueue") Queue queue, @Qualifier("imExchange") DirectExchange exchange) {
        String routingKey = routingKey();
        log.info("routingKey: {}", routingKey);
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }


}
