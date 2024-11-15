package com.linuxea.letschat.mqlistener;

import com.corundumstudio.socketio.SocketIOClient;
import com.linuxea.letschat.message.IMMessage;
import com.linuxea.letschat.service.ImMessageSendService;
import com.linuxea.letschat.util.JsonUtil;
import com.linuxea.letschat.util.SessionService;
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
    private final ImMessageSendService imMessageSendService;
    private final SessionService sessionService;
    private final JsonUtil jsonUtil;

    @RabbitHandler
    public void handler(@Payload IMMqMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel rabbitChannel) throws IOException {
        try {
            log.info("Receive message: {}", message);
            String toSessionId = message.getImMessage().getToSessionId();
            boolean hasClient = socketIOClientService.hasClient(toSessionId);
            if (!hasClient) {
                IMMessage imMessage = message.getImMessage();
                // 发送离线消息给发送者
                IMMessage returnIMMsg = new IMMessage();
                returnIMMsg.setFromSessionId(imMessage.getToSessionId());
                returnIMMsg.setToSessionId(imMessage.getFromSessionId());
                returnIMMsg.setPayload("I am offline, please leave a message.");
                IMMqMessage returnIMMQMsg = new IMMqMessage();
                returnIMMQMsg.setImMessage(returnIMMsg);
                returnIMMQMsg.setReturnMsg(true);
                imMessageSendService.send(sessionService.getRouteKeyBySession(imMessage.getFromSessionId()), returnIMMsg);
                return;
            }

            // 发送消息给客户端
            SocketIOClient client = socketIOClientService.getClient(toSessionId);
            client.sendEvent("receiveMessage", jsonUtil.toJson(message.getImMessage()));
        } catch (Exception e) {
            log.error("ImMQListener.handler error: {}", e.getMessage());
        } finally {
            rabbitChannel.basicAck(deliveryTag, false);
        }
    }

}
