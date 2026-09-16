package org.polytech.spring.patient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Implémentation « base de données » du contrat PatientStore.
 * Le code JDBC réel est hors sujet ici : les appels sont tracés sur la console.
 */
public class PatientDataBase implements PatientStore {

    @Override
    public void save(Patient patient) {
        System.out.println("PatientDataBase  - INSERT INTO patient ... " + patient);
    }

    /**
     * Appelée après la construction et après l'injection des dépendances.
     * C'est l'emplacement prévu pour ouvrir un pool de connexions.
     */
    @PostConstruct
    public void init() {
        System.out.println("PatientDataBase  - @PostConstruct : ouverture de la connexion");
    }

    /**
     * Appelée à la fermeture du contexte, d'où le try-with-resources dans App.
     */
    @PreDestroy
    public void close() {
        System.out.println("PatientDataBase  - @PreDestroy   : fermeture de la connexion");
    }
}
