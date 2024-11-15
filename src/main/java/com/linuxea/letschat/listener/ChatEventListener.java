package com.linuxea.letschat.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.linuxea.letschat.message.IMMessage;
import com.linuxea.letschat.service.ImMessageSendService;
import com.linuxea.letschat.util.JsonUtil;
import com.linuxea.letschat.util.SocketIOClientService;
import jakarta.websocket.OnError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatEventListener implements InitializingBean {

    private final SocketIOClientService socketIOClientService;
    private final ImMessageSendService imMessageSendService;
    private final JsonUtil jsonUtil;

    // 客户端连接时触发
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Client connected: {}", client.getSessionId());
        socketIOClientService.addClient(client.getSessionId().toString(), client);
    }

    // 客户端断开连接时触发
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
        socketIOClientService.removeClient(client.getSessionId().toString());
    }

    // 监听 "sendMessage" 事件，并处理收到的消息
    @OnEvent("sendMessage")
    public void onChatMessage(SocketIOClient client, String data) {
        log.info("Received message: {}", data);
        imMessageSendService.send(client.getSessionId().toString(), jsonUtil.toObject(data, IMMessage.class));
    }

    @OnError
    public void onError(SocketIOClient client, Throwable throwable) {
        log.error("{} Error occurred", client.getSessionId(), throwable);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ChatEventListener initialized");
    }
}
