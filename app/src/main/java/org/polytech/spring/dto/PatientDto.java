package org.polytech.spring.dto;

import java.time.LocalDate;

/**
 * Contrat de sortie de l'API.
 *
 * Un record ne porte pas de comportement : il transporte des données. Il
 * fournit le constructeur canonique, les accesseurs, equals/hashCode/toString
 * et l'immuabilité. Jackson sait le sérialiser nativement.
 *
 * Aucune relation n'y figure : l'objet est plat. Ni proxy Hibernate, ni cycle
 * de références — les deux pannes de la démonstration ne peuvent pas s'y
 * produire.
 */
public record PatientDto(
        Long id,
        String prenom,
        String nom,
        String email,
        LocalDate dateNaissance) {
}
