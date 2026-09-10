package org.polytech.spring.patient;

/**
 * Étape 2 — découplage par interface.
 *
 * L'extraction d'une interface rend possibles d'autres implémentations
 * — fichier, mémoire, API distante — sans modification du code métier.
 */
public class PatientSerialization implements PatientStore {

    @Override
    public void save(Patient patient) {
        System.out.println("PatientSerialization - sérialisation de " + patient + " sur disque");
    }
}
