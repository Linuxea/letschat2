package com.linuxea.letschat.mqlistener;

import com.corundumstudio.socketio.SocketIOClient;
import com.linuxea.letschat.util.SocketIOClientService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RabbitListener(queues = "#{rabbitMQConfiguration.queueName()}")
@Component
@Slf4j
@RequiredArgsConstructor
public class ImMQListener {

    private final SocketIOClientService socketIOClientService;

    @RabbitHandler
    public void handler(@Payload IMMqMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel rabbitChannel) throws IOException {
        System.out.println("Received message: " + message);
        if (!socketIOClientService.hasClient(message.getSessionId())) {
            log.error("Client not found: {}", message.getSessionId());
            return;
        }
        SocketIOClient client = socketIOClientService.getClient(message.getSessionId());
        client.sendEvent("receiveMessage", message.getImMessage());
        rabbitChannel.basicAck(deliveryTag, false);
    }

}
