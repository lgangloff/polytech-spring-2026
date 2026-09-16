package org.polytech.spring.rest;

import org.polytech.spring.patient.Patient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController = @Controller + @ResponseBody : la valeur de retour de chaque
 * méthode est sérialisée directement dans le corps de la réponse HTTP, sans
 * moteur de template ni résolution de vue.
 *
 * Comme tout composant Spring, un contrôleur est un singleton : une seule
 * instance sert toutes les requêtes. Un contrôleur ne doit donc pas porter
 * d'état mutable, celui-ci serait partagé entre tous les utilisateurs.
 */
@RestController
public class HelloController {

    /** Étape 4 : la méthode renvoie une chaîne, Spring l'écrit telle quelle. */
    @GetMapping("/hello")
    public String hello() {
        return "Hello Polytech";
    }

    /** Étape 5 : la méthode renvoie un objet Java, Jackson le sérialise en JSON. */
    @GetMapping("/hello/patient")
    public Patient patient() {
        return new Patient("Ada", "Lovelace", "ada.lovelace@polytech.fr");
    }
}
