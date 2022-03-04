package com.abhishek.springsseemitter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@RestController(value = "/")
public class Controller {

    @GetMapping
    public SseEmitter getSseEmitterResult() {
        long sseEmitterTimeoutsInMillis = 5 * 60L;
        SseEmitter sseEmitter = new SseEmitter(sseEmitterTimeoutsInMillis);

        try {
            Stream<SseEmitter> sseEmitterStream = IntStream.rangeClosed(1, 10)
                    .mapToObj(value -> {
                        try {
                            sseEmitter.send(value);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return sseEmitter;
                    });

            sseEmitterStream.forEach(System.out::println);

            sseEmitter.complete();
        } catch (Exception e) {
            e.printStackTrace();
            sseEmitter.completeWithError(e);
        }

        return sseEmitter;
    }
}
