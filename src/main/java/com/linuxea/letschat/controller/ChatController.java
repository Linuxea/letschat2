package com.linuxea.letschat.controller;

import com.linuxea.letschat.controller.domain.Friends;
import com.linuxea.letschat.util.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    private final SessionService sessionService;

    @GetMapping("/online-friends")
    public List<Friends> getOnlineFriends(@RequestParam String sessionId) {
        List<String> onlineFriends = sessionService.getOnlineFriends();
        return onlineFriends.stream().map(friend -> {
            Friends friends = new Friends();
            friends.setId(friend);
            friends.setName(Objects.equals(sessionId, friend) ? "我" : friend);
            return friends;
        }).toList();
    }

}
