package org.polytech.spring;

import org.polytech.spring.patient.Consultation;
import org.polytech.spring.patient.Patient;
import org.polytech.spring.patient.PatientService;
import org.polytech.spring.patient.PatientStore;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ObjectUtils;

public class App {

    public static void main(String[] args) {

        // try-with-resources : le contexte est fermé en sortie de bloc,
        // ce qui déclenche les @PreDestroy des beans singleton.
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // getBean() renvoie une instance dont les dépendances
            // ont déjà été résolues par le conteneur.
            PatientService service = ctx.getBean(PatientService.class);
            service.savePatient(new Patient("Ada", "Lovelace", "ada.lovelace@polytech.fr"));

            System.out.println();
            System.out.println("--- scope singleton : deux appels à getBean(PatientStore.class) ---");
            System.out.println(ObjectUtils.identityToString(ctx.getBean(PatientStore.class)));
            System.out.println(ObjectUtils.identityToString(ctx.getBean(PatientStore.class)));

            System.out.println();
            System.out.println("--- scope prototype : deux appels à getBean(Consultation.class) ---");
            System.out.println(ObjectUtils.identityToString(ctx.getBean(Consultation.class)));
            System.out.println(ObjectUtils.identityToString(ctx.getBean(Consultation.class)));

            System.out.println();
            System.out.println("--- fermeture du contexte ---");
        }
    }
}
