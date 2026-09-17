package org.polytech.spring.patient;

public class DocteurNotFoundException extends RuntimeException {

    public DocteurNotFoundException(Long id) {
        super("Aucun docteur d'identifiant " + id);
    }
}
