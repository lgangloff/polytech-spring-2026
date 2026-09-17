package org.polytech.spring.patient;

import java.util.List;

import org.polytech.spring.repository.DocteurRepository;
import org.polytech.spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Le stockage écrit à la main est supprimé : PatientStore, PatientDataBase et
 * PatientSerialization ne sont plus utilisés. Une interface étendant
 * JpaRepository suffit, Spring en fournit l'implémentation au démarrage.
 *
 * La transaction est portée par le service, jamais par le repository : une
 * opération métier peut enchaîner plusieurs appels de persistance qui doivent
 * réussir ou échouer ensemble. Les méthodes de JpaRepository sont déjà
 * transactionnelles individuellement, ce qui ne couvre pas ce cas.
 *
 * À ce stade, le service renvoie des entités. Ce choix est délibéré ; ses
 * conséquences sont traitées au chapitre suivant.
 */
@Service
public class PatientService {

    private final PatientRepository repository;
    private final DocteurRepository docteurRepository;

    public PatientService(PatientRepository repository, DocteurRepository docteurRepository) {
        this.repository = repository;
        this.docteurRepository = docteurRepository;
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return repository.findAll();
    }

    /** Recherche par convention de nommage, exposée via un @RequestParam optionnel. */
    @Transactional(readOnly = true)
    public List<Patient> findByNom(String nom) {
        return repository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Une seule requête SQL, JOIN FETCH compris : le patient et son médecin
     * traitant sont chargés ensemble. Sans le JOIN FETCH, lire le médecin
     * déclencherait une seconde requête — et, sur une liste, N+1 requêtes.
     */
    @Transactional(readOnly = true)
    public Patient findById(Long id) {
        return repository.findByIdWithDocteur(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    public Patient create(Patient patient) {
        if (patient.getEmail() == null || patient.getEmail().isBlank()) {
            throw new PatientException("email obligatoire");
        }
        return repository.save(patient);
    }

    /**
     * Deux lectures et une écriture dans la même unité de travail : ce cas
     * justifie @Transactional sur le service. Aucun save() n'est nécessaire,
     * le patient étant géré par la session ; Hibernate écrira au commit.
     */
    @Transactional
    public Patient affecterMedecin(Long pid, Long did) {
        Patient p = repository.findById(pid)
                .orElseThrow(() -> new PatientNotFoundException(pid));
        Docteur d = docteurRepository.findById(did)
                .orElseThrow(() -> new DocteurNotFoundException(did));
        p.setMedecinTraitant(d);
        return p;
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new PatientNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
