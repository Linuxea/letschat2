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

    public Map<String, SocketIOClient> getClientMap() {
        return clientMap;
    }


}
