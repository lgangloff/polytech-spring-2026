package org.polytech.spring.repository;

import java.util.List;
import java.util.Optional;

import org.polytech.spring.patient.Docteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocteurRepository extends JpaRepository<Docteur, Long> {

    /**
     * JOIN FETCH appliqué à une collection : la patientèle est LAZY. Sans cette
     * requête, charger un docteur puis lire ses patients déclenche une seconde
     * requête — et, sur une liste de N docteurs, N+1 requêtes.
     */
    @Query("""
           select d from Docteur d
           left join fetch d.patients
           where d.id = :id
           """)
    Optional<Docteur> findByIdWithPatients(@Param("id") Long id);

    /** Les docteurs qui suivent au moins un patient portant ce nom. */
    @Query("""
           select distinct d from Docteur d join d.patients p
           where p.nom = :nom
           """)
    List<Docteur> findByPatientNom(@Param("nom") String nom);
}
