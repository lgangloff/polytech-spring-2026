package org.polytech.spring.patient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Les annotations applicables à une méthode @Bean restent applicables à un
 * composant : @Scope, @Primary, @Lazy.
 *
 * Sur un bean prototype, @PostConstruct est appelée à chaque création,
 * @PreDestroy ne l'est jamais.
 */
@Component
@Scope("prototype")
public class Consultation {

    @PostConstruct
    public void init() {
        System.out.println("Consultation     - @PostConstruct");
    }

    @PreDestroy
    public void close() {
        System.out.println("Consultation     - @PreDestroy");
    }
}
