package com.message.bidirectional.controller;

import com.message.bidirectional.model.Message;
import com.message.bidirectional.model.OutputMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Slf4j
public class WebSocketController {
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    /**MessageMapping indica il mapping del metodo,
     *sendTo indica l'indirizzo su cui verrà inviata la risposta
     * */
    public OutputMessage send(Message message){
        String time = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(new Date());
        return new OutputMessage(message.getFrom(), message.getMessage(), time);
    }

    @MessageMapping("/chat/{idRequest}/{idOperator}")
    /**MessageMapping indica il mapping del metodo,
     * */
    @SendTo("/topic/response/{idOperator}")
    public OutputMessage sendToUser(@DestinationVariable int idRequest,@DestinationVariable int idOperator){
        String time = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(new Date());
        log.info("idOperator: {} ,idRequest: {}",idOperator,idRequest);
        return new OutputMessage("server","request done "+idRequest,time);
    }

    @MessageMapping("/sngl-chat")
    public void sendSpecific(
            @Payload Message msg,
            Principal user,
            @Header("simpSessionId") String sessionId){
        OutputMessage out = new OutputMessage(
                msg.getFrom(),
                msg.getMessage(),
                new SimpleDateFormat("HH:mm").format(new Date()));
        simpMessagingTemplate.convertAndSendToUser(
                msg.getFrom(), "/specific-user"+"/"+sessionId, out);
        //costruisce la url nel formato /{endpoint per user in WebSocketConfig}/{username}/specific-user/{sessionId}
    }
}
