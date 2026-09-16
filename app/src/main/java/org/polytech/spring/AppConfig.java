package org.polytech.spring;

import org.polytech.spring.patient.Consultation;
import org.polytech.spring.patient.PatientDataBase;
import org.polytech.spring.patient.PatientService;
import org.polytech.spring.patient.PatientStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Classe de configuration : elle décrit les objets gérés par le conteneur.
 *
 * L'ordre de déclaration est sans effet : Spring construit le graphe de
 * dépendances et instancie dans l'ordre requis.
 */
@Configuration
public class AppConfig {

    /** Par défaut, le nom du bean est celui de la méthode : « databaseStore ». */
    @Bean
    public PatientStore databaseStore() {
        return new PatientDataBase();
    }

    /**
     * Les paramètres d'une méthode @Bean sont des dépendances que Spring
     * résout depuis le conteneur.
     */
    @Bean
    public PatientService patientService(PatientStore store) {
        return new PatientService(store);
    }

    /** Étape 8 : un second bean, de scope prototype. */
    @Bean
    @Scope("prototype")
    public Consultation consultation() {
        return new Consultation();
    }
}
