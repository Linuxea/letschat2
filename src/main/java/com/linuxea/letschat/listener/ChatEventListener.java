package com.linuxea.letschat.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import jakarta.websocket.OnError;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class ChatEventListener implements InitializingBean {

    // 客户端连接时触发
    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("Client connected: " + client.getSessionId());
    }

    // 客户端断开连接时触发
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    // 监听 "sendMessage" 事件，并处理收到的消息
    @OnEvent("sendMessage")
    public void onChatMessage(SocketIOClient client, String data) {
        // 广播消息到所有客户端
        client.getNamespace().getBroadcastOperations().sendEvent("receiveMessage", data);
    }

    @OnError
    public void onError(SocketIOClient client, Throwable throwable) {
        System.out.println("Error: " + throwable.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("ChatEventListener initialized");
    }
}
