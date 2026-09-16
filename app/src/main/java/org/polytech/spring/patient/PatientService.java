package org.polytech.spring.patient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Service;

/**
 * @Service : stéréotype des classes portant les règles métier.
 *
 * Depuis Spring 4.3, @Autowired est facultative sur le constructeur lorsqu'il
 * n'y en a qu'un seul : Spring l'utilise automatiquement.
 *
 * Étape 6 de l'exercice — lever l'ambiguïté avec @Qualifier plutôt qu'avec
 * @Primary, en nommant le bean voulu au point d'injection :
 *
 *   public PatientService(@Qualifier("patientSerialization") PatientStore store) {
 */
@Service
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
