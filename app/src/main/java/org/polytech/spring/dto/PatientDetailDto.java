package org.polytech.spring.dto;

import java.time.LocalDate;

/**
 * Seconde vue du même objet : le détail, avec le nom du médecin traitant.
 *
 * PatientDto, sans le médecin, pour la liste ; PatientDetailDto, avec, pour le
 * détail. Une classe unique servant à la fois de schéma de base et de contrat
 * d'API ne permettrait pas cette distinction.
 */
public record PatientDetailDto(
        Long id,
        String prenom,
        String nom,
        String email,
        LocalDate dateNaissance,
        String medecinTraitant) {
}
