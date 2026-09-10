package org.polytech.spring.patient;

/**
 * Contrat de persistance. Le code métier ne dépend que de cette interface,
 * et non d'une implémentation particulière.
 */
public interface PatientStore {

    void save(Patient patient);
}
