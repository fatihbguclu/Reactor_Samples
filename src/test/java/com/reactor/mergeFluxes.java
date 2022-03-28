package com.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class mergeFluxes {
    @Test
    public void mergeFluxes(){
        Flux<String> characters = Flux.just("Garfield","Kojak","Barbarossa")
                .delayElements(Duration.ofMillis(500));
        Flux<String> food = Flux.just("Lasagna","Lollipops","Apple")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characters.mergeWith(food);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbarossa")
                .expectNext("Apple")
                .verifyComplete();
    }

    @Test
    public void zipFluxes(){
        Flux<String> characters = Flux.just("Garfield","Kojak","Barbarossa");
        Flux<String> food = Flux.just("Lasagna","Lollipops","Apple");

        Flux<Tuple2<String,String >> zippedFlux = Flux.zip(characters,food);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p ->
                    p.getT1().equals("Garfield") &&
                    p.getT2().equals("Lasagna")
                )
                .expectNextMatches(p ->
                    p.getT1().equals("Kojak") &&
                    p.getT2().equals("Lollipops")
                )
                .expectNextMatches(p ->
                    p.getT1().equals("Barbarossa") &&
                    p.getT2().equals("Apple")
                )
                .verifyComplete();
    }

    @Test
    public void zipFluxesToObject(){
        Flux<String> characters = Flux.just("Garfield","Kojak","Barbarossa");
        Flux<String> food = Flux.just("Lasagna","Lollipops","Apple");

        Flux<String> zippedFlux = Flux.zip(characters,food,
                (c,f) -> c + " eats " + f
                );

        StepVerifier.create(zippedFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbarossa eats Apple")
                .verifyComplete();
    }

    @Test
    public void firstWithSignalFlux() {
        Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));

        Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

        Flux<String> firstFlux = Flux.firstWithSignal(slowFlux,fastFlux);

        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();

    }
}
