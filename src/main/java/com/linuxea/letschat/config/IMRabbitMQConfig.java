package com.linuxea.letschat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "im.rabbitmq")
public class IMRabbitMQConfig {

    private String server;


}
