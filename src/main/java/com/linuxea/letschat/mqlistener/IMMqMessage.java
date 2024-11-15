package com.linuxea.letschat.mqlistener;

import com.linuxea.letschat.message.IMMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMMqMessage {

    private String id = UUID.randomUUID().toString();

    private boolean returnMsg;

    private int sendTimes;

    private IMMessage imMessage;


}
