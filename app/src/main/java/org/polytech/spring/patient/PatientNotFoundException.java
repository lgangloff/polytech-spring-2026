package org.polytech.spring.patient;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(Long id) {
        super("Aucun patient d'identifiant " + id);
    }
}
