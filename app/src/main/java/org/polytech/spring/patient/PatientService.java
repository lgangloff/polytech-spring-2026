package org.polytech.spring.patient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Logique métier uniquement.
 * La dépendance vers le stockage est déclarée dans la signature du constructeur :
 * elle est visible, obligatoire, et l'attribut peut être final.
 */
public class PatientService {

    private final PatientStore store;

    public PatientService(PatientStore store) {
        this.store = store;
    }

    public void savePatient(Patient patient) {
        if (patient.getEmail() == null) {
            throw new IllegalArgumentException("email obligatoire");
        }
        System.out.println("PatientService   - validation de " + patient.getEmail());
        store.save(patient);
    }

    /** Étape 6 de l'exercice : vérifier qu'un PatientStore a bien été injecté. */
    @PostConstruct
    public void init() {
        System.out.println("PatientService   - @PostConstruct : store injecté = "
                + store.getClass().getSimpleName());
    }

    @PreDestroy
    public void close() {
        System.out.println("PatientService   - @PreDestroy");
    }
}
