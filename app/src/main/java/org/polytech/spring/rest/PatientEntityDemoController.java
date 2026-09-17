package org.polytech.spring.rest;

import java.util.List;

import org.polytech.spring.repository.PatientRepository;
import org.polytech.spring.patient.Patient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur de démonstration du chapitre : il illustre la pratique à éviter.
 *
 * Il expose l'entité directement et n'est actif que sous le profil « demo »,
 * afin de ne pas interférer avec l'API :
 *
 *   ./gradlew bootRun --args='--spring.profiles.active=demo'
 *   GET http://localhost:8080/demo/patients
 *
 * Deux échecs peuvent se produire, de natures différentes :
 *
 *  1. LazyInitializationException — la transaction est fermée quand le
 *     contrôleur rend la main ; Jackson tente ensuite de sérialiser une
 *     relation LAZY jamais chargée. C'est le cas obtenu ici, open-in-view étant
 *     désactivé.
 *
 *  2. StackOverflowError — si le premier échec est corrigé en passant les
 *     relations en EAGER : le patient référence son docteur, qui référence sa
 *     patientèle, qui contient le patient. Jackson suit les références sans
 *     terminer. C'est une conséquence de la relation bidirectionnelle.
 *
 * Deux décisions de conception correctes produisent donc deux pannes : le
 * problème est ailleurs. Une entité JPA modélise un schéma relationnel, un DTO
 * modélise un contrat d'API ; ce ne sont pas les mêmes objets.
 */
@Profile("demo")
@RestController
public class PatientEntityDemoController {

    private final PatientRepository repository;

    public PatientEntityDemoController(PatientRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/demo/patients")
    public List<Patient> getAll() {
        return repository.findAll(); // l'entité, avec ses proxies et ses cycles
    }
}
