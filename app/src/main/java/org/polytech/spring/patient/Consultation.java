package org.polytech.spring.patient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Bean de scope « prototype » (étapes 8 et 9 de l'exercice).
 *
 * Une nouvelle instance est créée à chaque demande adressée au conteneur.
 * Spring ne conserve aucune référence sur un bean prototype une fois livré :
 * il ne peut donc pas déterminer le moment de sa destruction, et @PreDestroy
 * n'est jamais appelée. C'est la réponse à la question 9. @PostConstruct, en
 * revanche, est appelée à chaque création.
 */
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
