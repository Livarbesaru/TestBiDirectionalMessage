package com.message.bidirectional.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/web")
public class WebController {
    @GetMapping("/home")
    public String home(){
        return "index";
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
