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
    public SseEmitter getSseEmitterResult() throws IOException, InterruptedException {
        long sseEmitterTimeoutsInMillis = 5 * 60;
        SseEmitter sseEmitter = new SseEmitter(sseEmitterTimeoutsInMillis);


        Stream aa = IntStream.rangeClosed(1, 10)
                .mapToObj(value -> {
                    try {
                        sseEmitter.send(value);
                        System.out.println(value);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return sseEmitter;
                });

        System.out.println("11111");

        aa.forEach(o -> {});

        System.out.println("22222");
        sseEmitter.complete();


        return sseEmitter;
    }
}
