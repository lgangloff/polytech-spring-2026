package org.polytech.spring.patient;

/**
 * Étape 4 — pattern ServiceLocator.
 *
 * Un annuaire central fournit une implémentation pour un type donné. Les
 * problèmes des étapes précédentes disparaissent : PatientService ne référence
 * plus PatientDataBase par son nom et ne contrôle plus sa construction.
 *
 * Une limite subsiste : le besoin d'un PatientStore n'est plus visible depuis
 * l'extérieur de la classe.
 *
 *   public class PatientService {
 *       private PatientStore store = StoreServiceLocator.getPatientStore();
 *       // la signature de la classe ne mentionne pas cette dépendance
 *   }
 *
 * Une dépendance masquée reste une dépendance, et rend la classe difficile à
 * tester. D'où l'étape 5, l'injection de dépendance — voir PatientService.
 *
 * Cette classe n'est plus utilisée. Elle est conservée à titre de comparaison.
 */
public final class StoreServiceLocator {

    private static final PatientStore INSTANCE = new PatientDataBase();

    private StoreServiceLocator() {
    }

    public static PatientStore getPatientStore() {
        return INSTANCE;
    }
}
