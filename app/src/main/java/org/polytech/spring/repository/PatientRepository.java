package org.polytech.spring.repository;

import java.util.List;
import java.util.Optional;

import org.polytech.spring.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * L'interface est déclarée, Spring en fournit l'implémentation au démarrage.
 *
 * L'héritage de JpaRepository donne accès à findAll(), findById(), save(),
 * saveAll(), deleteById(), count(), existsById(), findAll(Sort),
 * findAll(Pageable), sans code supplémentaire.
 *
 * Cette interface remplace l'intégralité du contenu de dao/PatientDao.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Requête par convention de nommage : Spring analyse le nom de la méthode
     * et génère la requête JPQL correspondante. Aucune implémentation à écrire.
     *
     * Mots-clés disponibles : And, Or, Between, LessThan, GreaterThan, Like,
     * IgnoreCase, OrderBy, Top, First, Containing.
     */
    List<Patient> findByNomContainingIgnoreCase(String extrait);

    /**
     * Lorsque le nom de méthode deviendrait trop long, ou qu'une jointure
     * explicite est nécessaire, la requête est écrite avec @Query.
     *
     * JOIN FETCH répond au chargement LAZY : le médecin traitant est chargé en
     * même temps que le patient, en une seule requête, au lieu d'une requête
     * par patient (problème N+1).
     */
    @Query("""
           select p from Patient p
           left join fetch p.medecinTraitant
           where p.id = :id
           """)
    Optional<Patient> findByIdWithDocteur(@Param("id") Long id);
}
