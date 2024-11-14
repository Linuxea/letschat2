package com.linuxea.letschat.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private Long fromUserId;

    private Long toUserId;

    private List<String> message;

}
