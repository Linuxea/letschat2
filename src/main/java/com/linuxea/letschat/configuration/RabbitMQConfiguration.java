package com.linuxea.letschat.configuration;

import com.linuxea.letschat.config.IMRabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
        return String.format("im_queue_%s", imRabbitMQConfig.getId());
    }

    public String routingKey() {
        return String.format("im_routing_key_%s", imRabbitMQConfig.getId());
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

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


}
