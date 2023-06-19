package com.message.bidirectional.controller;

import com.message.bidirectional.model.Message;
import com.message.bidirectional.model.OutputMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Slf4j
public class WebSocketController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    /**MessageMapping indica il mapping del metodo,
     *sendTo indica l'indirizzo su cui verrà inviata la risposta
     * */
    public OutputMessage send(Message message){
        String time = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(new Date());
        return new OutputMessage(message.getFrom(), message.getMessage(), time);
    }
}
