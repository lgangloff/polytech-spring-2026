package org.polytech.spring;

import org.polytech.spring.patient.Patient;
import org.polytech.spring.patient.PatientDataBase;
import org.polytech.spring.patient.PatientService;

/**
 * Câblage manuel des dépendances.
 *
 * Sur trois classes, le coût est négligeable. À l'échelle d'une application
 * complète — plusieurs dizaines de services, chacun avec ses dépendances, ses
 * paramètres de connexion et son ordre de construction — cette méthode devient
 * difficile à maintenir.
 *
 * Le conteneur IoC de Spring prend ce travail en charge : résolution des
 * dépendances, instanciation, injection.
 */
public class App {

    public static void main(String[] args) {

        // seul endroit du code où l'implémentation est choisie
        PatientService service = new PatientService(new PatientDataBase());

        service.savePatient(new Patient("Ada", "Lovelace", "ada.lovelace@polytech.fr"));
    }
}
