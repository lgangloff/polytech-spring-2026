package org.polytech.spring.dto;

import java.time.LocalDate;

/**
 * Contrat d'entrée : les données que le client est autorisé à envoyer.
 *
 * Aucun identifiant : il est généré par le serveur et ne doit pas être imposé
 * par le client.
 */
public record PatientCreationDto(
        String prenom,
        String nom,
        String email,
        LocalDate dateNaissance) {
}
