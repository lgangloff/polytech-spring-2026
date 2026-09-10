package org.polytech.spring.patient;

/**
 * Étape 5 — injection de dépendance.
 *
 * PatientService requiert un PatientStore ; cette dépendance est déclarée dans
 * le constructeur. La création de l'instance est déplacée hors de la classe.
 *
 * Conséquences :
 *   - la dépendance apparaît dans la signature ;
 *   - l'attribut peut être final, l'objet est immuable ;
 *   - en test, l'implémentation est choisie par l'appelant ;
 *   - un objet incomplet ne peut pas être construit.
 *
 * Reste à déterminer qui appelle ce constructeur, et avec quel argument ; c'est
 * l'objet de la branche suivante. Ici, c'est la méthode main.
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
}
