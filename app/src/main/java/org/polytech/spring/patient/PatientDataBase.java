package org.polytech.spring.patient;

/**
 * Étape 1 — Single Responsibility.
 *
 * La persistance est extraite de PatientService. Cette classe est responsable
 * de la persistance de l'objet Patient, et d'elle seule.
 */
public class PatientDataBase implements PatientStore {

    @Override
    public void save(Patient patient) {
        // ouverture d'une connexion JDBC, INSERT, commit
        System.out.println("PatientDataBase  - INSERT INTO patient ... " + patient);
    }
}
