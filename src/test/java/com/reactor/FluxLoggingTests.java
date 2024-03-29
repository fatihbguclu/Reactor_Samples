package com.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxLoggingTests {
    @Test
    public void logSimple() {
        Flux<String> beltColors = Flux.just("white", "yellow", "orange", "green", "purple", "blue")
                .log();

        beltColors.subscribe();
    }

    @Test
    public void logMapping(){
        Flux<String> beltColors = Flux.just("white", "yellow", "orange", "green", "purple", "blue")
                .map(String::toUpperCase)
                .log();

        beltColors.subscribe();
    }
    @Test
    public void logFlatMapping(){
        Flux<String> beltColors = Flux.just("white", "yellow", "orange", "green", "purple", "blue")
                .flatMap(m -> Mono.just(m)
                        .map(String::toUpperCase)
                )
                .log();
        beltColors.subscribe();
    }
}
