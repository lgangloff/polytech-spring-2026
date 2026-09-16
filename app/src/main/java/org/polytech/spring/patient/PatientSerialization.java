package org.polytech.spring.patient;

import org.springframework.stereotype.Repository;

/**
 * Seconde implémentation de PatientStore, à l'origine de l'ambiguïté.
 *
 * Sans @Primary sur PatientDataBase, ou sans @Qualifier au point d'injection,
 * le démarrage échoue sur :
 *
 *   NoUniqueBeanDefinitionException: expected single matching bean but found 2:
 *   patientDataBase, patientSerialization
 */
@Repository
public class PatientSerialization implements PatientStore {

    @Override
    public void save(Patient patient) {
        System.out.println("PatientSerialization - sérialisation de " + patient + " sur disque");
    }
}
