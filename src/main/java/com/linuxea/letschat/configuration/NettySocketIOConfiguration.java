package com.linuxea.letschat.configuration;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class NettySocketIOConfiguration {

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(9092);  // WebSocket server port

        // Other configurations like authentication, max connections, etc.
        config.setAllowCustomRequests(true);
        config.setUpgradeTimeout(10000);  // Timeout for upgrading to WebSocket
        config.setPingTimeout(60000);     // Ping timeout
        config.setPingInterval(25000);    // Ping interval
        // Allow CORS (Cross-Origin Resource Sharing)
        config.setOrigin("*");
        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);
        // 使用自定义的 JSON 支持类
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

}
