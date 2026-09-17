package org.polytech.spring.patient;

import java.util.List;

import org.polytech.spring.dto.PatientCreationDto;
import org.polytech.spring.dto.PatientDetailDto;
import org.polytech.spring.dto.PatientDto;
import org.polytech.spring.dto.PatientMapper;
import org.polytech.spring.repository.DocteurRepository;
import org.polytech.spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Le DAO écrit à la main a disparu : le service dépend désormais du repository.
 *
 * La transaction est portée par le service, jamais par le repository : une
 * opération métier peut enchaîner plusieurs appels de persistance qui doivent
 * réussir ou échouer ensemble. Les méthodes de JpaRepository sont déjà
 * transactionnelles individuellement, ce qui ne couvre pas ce cas.
 *
 * @Transactional(readOnly = true) sur les lectures : Hibernate n'effectue pas
 * le contrôle des modifications.
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
    public List<PatientDto> findAll() {
        return repository.findAll().stream().map(PatientMapper::toDto).toList();
    }

    /** Recherche par convention de nommage, exposée via un @RequestParam optionnel. */
    @Transactional(readOnly = true)
    public List<PatientDto> findByNom(String nom) {
        return repository.findByNomContainingIgnoreCase(nom).stream()
                .map(PatientMapper::toDto)
                .toList();
    }

    /**
     * Une seule requête SQL, JOIN FETCH compris : le patient et son médecin
     * traitant sont chargés ensemble. Sans le JOIN FETCH, l'accès au médecin
     * dans le mapper déclencherait une seconde requête, ou une
     * LazyInitializationException hors transaction.
     */
    @Transactional(readOnly = true)
    public PatientDetailDto findById(Long id) {
        return repository.findByIdWithDocteur(id)
                .map(PatientMapper::toDetailDto)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    public PatientDto create(PatientCreationDto body) {
        if (body.email() == null || body.email().isBlank()) {
            throw new PatientException("email obligatoire");
        }
        Patient saved = repository.save(PatientMapper.toEntity(body));
        return PatientMapper.toDto(saved);
    }

    /**
     * Deux lectures et une écriture dans la même unité de travail : ce cas
     * justifie @Transactional sur le service plutôt que sur le repository.
     * Aucun save() n'est nécessaire, le patient étant géré par la session ;
     * Hibernate écrira la modification au commit.
     */
    @Transactional
    public PatientDto affecterMedecin(Long pid, Long did) {
        Patient p = repository.findById(pid)
                .orElseThrow(() -> new PatientNotFoundException(pid));
        Docteur d = docteurRepository.findById(did)
                .orElseThrow(() -> new DocteurNotFoundException(did));
        p.setMedecinTraitant(d);
        return PatientMapper.toDto(p);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new PatientNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
