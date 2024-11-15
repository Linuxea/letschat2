package com.linuxea.letschat.mqlistener;

import com.linuxea.letschat.message.IMMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMMqMessage {

    private String sessionId;

    private IMMessage imMessage;


}
