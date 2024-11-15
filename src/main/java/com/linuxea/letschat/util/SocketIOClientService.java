package com.linuxea.letschat.util;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketIOClientService {

    private final Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    public void addClient(String sessionId, SocketIOClient socketIOClient) {
        clientMap.put(sessionId, socketIOClient);
    }

    public void removeClient(String sessionId) {
        clientMap.remove(sessionId);
    }

    public SocketIOClient getClient(String sessionId) {
        return clientMap.get(sessionId);
    }

    public boolean hasClient(String sessionId) {
        return clientMap.containsKey(sessionId);
    }

    public void sendToClient(String sessionId, String event, Object data) {
        SocketIOClient client = clientMap.get(sessionId);
        if (client != null) {
            client.sendEvent(event, data);
        }
    }

    public void sendToAllClient(String event, Object data) {
        clientMap.forEach((sessionId, client) -> client.sendEvent(event, data));
    }

    public void sendToAllClientExceptOne(String sessionId, String event, Object data) {
        clientMap.forEach((key, client) -> {
            if (!key.equals(sessionId)) {
                client.sendEvent(event, data);
            }
        });
    }

    public void sendToAllClientExceptSome(String[] sessionId, String event, Object data) {
        clientMap.forEach((key, client) -> {
            for (String userId : sessionId) {
                if (!key.equals(userId)) {
                    client.sendEvent(event, data);
                }
            }
        });
    }

    public void sendToSomeClient(String[] sessionId, String event, Object data) {
        for (String userId : sessionId) {
            SocketIOClient client = clientMap.get(userId);
            if (client != null) {
                client.sendEvent(event, data);
            }
        }
    }


}
