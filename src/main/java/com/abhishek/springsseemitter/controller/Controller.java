package com.abhishek.springsseemitter.controller;

import com.abhishek.springsseemitter.entity.UserEntity;
import com.abhishek.springsseemitter.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@RestController(value = "/")
public class Controller {

    private final UserRepository userRepository;

    public Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    @GetMapping("/mono/{id}")
    private Mono<UserEntity> getEmployeeById(@PathVariable(name = "id") Long id) {
        return Mono.just(userRepository.findById(id).orElse(null));
    }

    @GetMapping("/flux")
    private Flux<UserEntity> getEmployees() {
        return Flux.fromIterable(userRepository.findAll());
    }

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        System.out.println("IN_PROGRESS");

        Flux<String> stringFlux =  Flux.interval(Duration.ofSeconds(5))
                .take(3)
                .map(sequence -> "Flux - " + LocalTime.now());

        System.out.println("SUCCESS");

        return stringFlux;
    }

    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .take(3)
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }
}
