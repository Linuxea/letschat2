package com.linuxea.letschat.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMMessage {

    private String fromSessionId;

    private String toSessionId;

    private String payload;

}
