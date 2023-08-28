package com.message.bidirectional.controller;
import com.message.bidirectional.util.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
@RequestMapping("/web")
public class WebController {

    private KeycloakService keycloakService;
    @Autowired
    public WebController(KeycloakService keycloakService){
        this.keycloakService=keycloakService;
    }

    @GetMapping("/home")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String home(){
        return "index";
    }
    @GetMapping("/user/addpage")
    public String addUser(){
        return "addUser";
    }
    @PostMapping("/user/add")
    public void add(@RequestParam("username") String username,
                    @RequestParam("password") String password,
                    @RequestParam("email") String email,
                    @RequestParam("firstName") String firstName,
                    @RequestParam("lastName") String lastName){
        keycloakService.addUser(username,email,firstName,lastName,password);
    }
    /**
     * Restituisce un flusso dati con nome .name(),
     * ed il flusso dati sarà in .data()
     * SseEmitter è il gestore del flusso dati,
     * mentre ExecutorService ci permetter di creare un flusso asincrono
     * */
    @GetMapping("/stream-sse-mvc")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now().toString())
                            .id(String.valueOf(i))
                            .name("message");
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
