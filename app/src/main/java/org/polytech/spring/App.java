package org.polytech.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication combine trois annotations :
 *   - @Configuration           : cette classe est une classe de configuration ;
 *   - @ComponentScan           : les beans sont cherchés dans ce package et ses sous-packages ;
 *   - @EnableAutoConfiguration : Boot configure ce qu'il détecte dans le classpath.
 *
 * La classe doit donc être placée à la racine du package, le scan en dépend.
 * AppConfig devient inutile.
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
