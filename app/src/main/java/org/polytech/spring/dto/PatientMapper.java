package org.polytech.spring.dto;

import org.polytech.spring.patient.Patient;

/**
 * Mapping écrit à la main : explicite, lisible et débogable pas à pas.
 *
 * Règle : le mapper ne contient aucune règle métier. Il traduit, il ne décide
 * pas.
 *
 * MapStruct génère ce code à la compilation, ModelMapper procède par
 * introspection. Ces outils sont utiles sur de gros projets, mais hors du
 * périmètre du cours : à ce stade, écrire le mapping est plus formateur que le
 * générer.
 */
public final class PatientMapper {

    private PatientMapper() {
    }

    public static PatientDto toDto(Patient patient) {
        return new PatientDto(
                patient.getId(),
                patient.getPrenom(),
                patient.getNom(),
                patient.getEmail(),
                patient.getDateNaissance());
    }

    /**
     * Cette méthode accède à la relation medecinTraitant. Elle n'est donc
     * appelable que sur un patient chargé avec un JOIN FETCH, ou à l'intérieur
     * d'une transaction ; à défaut, LazyInitializationException.
     */
    public static PatientDetailDto toDetailDto(Patient patient) {
        String medecin = patient.getMedecinTraitant() == null
                ? null
                : "%s %s".formatted(patient.getMedecinTraitant().getPrenom(),
                                    patient.getMedecinTraitant().getNom());
        return new PatientDetailDto(
                patient.getId(),
                patient.getPrenom(),
                patient.getNom(),
                patient.getEmail(),
                patient.getDateNaissance(),
                medecin);
    }

    public static Patient toEntity(PatientCreationDto dto) {
        Patient patient = new Patient();
        patient.setPrenom(dto.prenom());
        patient.setNom(dto.nom());
        patient.setEmail(dto.email());
        patient.setDateNaissance(dto.dateNaissance());
        return patient;
    }
}
