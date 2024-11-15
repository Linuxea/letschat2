package com.linuxea.letschat.service;

import com.linuxea.letschat.configuration.RabbitMQConfiguration;
import com.linuxea.letschat.message.IMMessage;
import com.linuxea.letschat.mqlistener.IMMqMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImMessageSendService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfiguration rabbitMQConfiguration;

    public void send(String toRoutingKey, IMMessage message) {
        if (toRoutingKey != null) {
            IMMqMessage mqMessage = new IMMqMessage();
            mqMessage.setImMessage(message);
            rabbitTemplate.convertAndSend(rabbitMQConfiguration.exchangeName(), toRoutingKey, mqMessage);
        }
    }

}
