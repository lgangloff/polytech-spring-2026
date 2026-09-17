package org.polytech.spring.patient;

/** Donnée invalide envoyée par le client : erreur 400, et non erreur serveur. */
public class PatientException extends RuntimeException {

    public PatientException(String message) {
        super(message);
    }
}
