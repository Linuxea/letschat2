package com.linuxea.letschat.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.linuxea.letschat.configuration.RabbitMQConfiguration;
import com.linuxea.letschat.message.IMMessage;
import com.linuxea.letschat.service.ImMessageSendService;
import com.linuxea.letschat.util.JsonUtil;
import com.linuxea.letschat.util.SessionService;
import com.linuxea.letschat.util.SocketIOClientService;
import jakarta.websocket.OnError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn({"sessionService", "socketIOClientService", "imMessageSendService", "jsonUtil", "rabbitMQConfiguration"})
public class ChatEventListener implements InitializingBean, DisposableBean {

    private final SessionService sessionService;
    private final SocketIOClientService socketIOClientService;
    private final ImMessageSendService imMessageSendService;
    private final JsonUtil jsonUtil;
    private final RabbitMQConfiguration rabbitMQConfiguration;

    // 客户端连接时触发
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Client connected: {}", client.getSessionId());
        socketIOClientService.addClient(client.getSessionId().toString(), client);
        sessionService.setSession2RouteKey(client.getSessionId().toString(), rabbitMQConfiguration.routingKey());
        client.sendEvent("receiveMessage", "Welcome to Let's Chat! Your sessionId is: " + client.getSessionId());
    }

    // 客户端断开连接时触发
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
        socketIOClientService.removeClient(client.getSessionId().toString());
        sessionService.removeSession(client.getSessionId().toString());
    }

    // 监听 "sendMessage" 事件，并处理收到的消息
    @OnEvent("sendMessage")
    public void onChatMessage(SocketIOClient client, String data) {
        log.info("Received message: {}", data);
        IMMessage imMessage = jsonUtil.toObject(data, IMMessage.class);
        if (imMessage != null) {
            imMessage.setFromSessionId(client.getSessionId().toString());
            String routeKeyBySession = sessionService.getRouteKeyBySession(imMessage.getToSessionId());
            imMessageSendService.send(routeKeyBySession, imMessage);
        }
    }

    @OnError
    public void onError(SocketIOClient client, Throwable throwable) {
        log.error("{} Error occurred", client.getSessionId(), throwable);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("ChatEventListener initialized");
    }

    @Override
    public void destroy() {
        log.info("ChatEventListener destroyed");
        Map<String, SocketIOClient> clientMap = socketIOClientService.getClientMap();
        for (Map.Entry<String, SocketIOClient> entry : clientMap.entrySet()) {
            try {
                entry.getValue().disconnect();
            } finally {
                sessionService.removeSession(entry.getValue().getSessionId().toString());
            }
        }
    }
}
