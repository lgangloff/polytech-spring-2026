package org.polytech.spring.patient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @Repository : stéréotype des classes responsables de la persistance.
 * @Primary    : deux beans satisfont le type PatientStore ; celui-ci est
 *               retenu par défaut en cas d'ambiguïté.
 */
@Repository
@Primary
public class PatientDataBase implements PatientStore {

    /** L'URL n'est pas écrite dans le code : elle vient d'application.properties. */
    @Value("${patient.store.url}")
    private String url;

    @Override
    public void save(Patient patient) {
        System.out.println("PatientDataBase  - INSERT INTO patient ... " + patient);
    }

    @PostConstruct
    public void init() {
        System.out.println("PatientDataBase  - @PostConstruct : connexion à " + url);
    }

    @PreDestroy
    public void close() {
        System.out.println("PatientDataBase  - @PreDestroy   : fermeture de la connexion");
    }
}
