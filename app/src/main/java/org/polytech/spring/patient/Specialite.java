package org.polytech.spring.patient;

/**
 * Stockée en base sous forme de chaîne grâce à @Enumerated(EnumType.STRING).
 * Avec ORDINAL, réordonner les constantes ci-dessous modifierait la
 * signification des données déjà enregistrées, sans erreur au démarrage.
 */
public enum Specialite {

    GENERALISTE,
    CARDIOLOGIE,
    PEDIATRIE,
    INFECTIOLOGIE
}
