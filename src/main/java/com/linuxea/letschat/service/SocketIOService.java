package com.linuxea.letschat.service;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketIOService implements CommandLineRunner {

    private final SocketIOServer socketIOServer;

    @Override
    public void run(String... args) {
        // 启动 WebSocket 服务
        socketIOServer.start(); // 启动 WebSocket 服务
    }

    @PreDestroy
    public void stopSocketIOServer() {
        socketIOServer.stop(); // 停止 WebSocket 服务
    }
}