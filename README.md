# polytech-spring — branche `03-composants`

Code de référence de l'atelier **« À vos claviers — Composants, ambiguïté et configuration
externalisée »** (Java / Spring, partie 1/2).

## Ce qui change par rapport à `02-ioc-spring`

| Avant | Après |
|---|---|
| `AppConfig` déclare chaque bean avec `@Bean` | `AppConfig` active `@ComponentScan` et ne déclare plus rien |
| classes ordinaires | `@Repository` sur les stores, `@Service` sur le métier, `@Component` sur `Consultation` |
| une seule implémentation de `PatientStore` | deux : `PatientDataBase` et `PatientSerialization` |
| — | `@Primary` pour lever l'ambiguïté, `@Value` + `application.properties` pour la configuration |

## Lancer

```bash
./gradlew run
```

## Les trois manipulations de l'atelier

1. **Le scan.** Supprimer les `@Bean`, ce qui est déjà fait ici, et vérifier que
   l'application fonctionne toujours : Spring détecte les beans à partir des annotations.
2. **L'ambiguïté.** Commenter le `@Primary` de `PatientDataBase` et relancer. Le démarrage
   échoue sur `NoUniqueBeanDefinitionException: expected single matching bean but found 2:
   patientDataBase, patientSerialization`.
   Deux moyens de lever l'ambiguïté :
   - `@Primary` sur l'implémentation par défaut ;
   - `@Qualifier("patientSerialization")` au point d'injection dans `PatientService`, pour
     l'exception. Le code correspondant est en commentaire dans `PatientService`.
3. **La configuration externalisée.** `@PropertySource` désigne le fichier,
   `@Value("${patient.store.url}")` injecte la propriété. La valeur est affichée au démarrage
   par le `@PostConstruct` de `PatientDataBase`.
   Avec Spring Boot, au chapitre suivant, `application.properties` est chargé
   automatiquement et `@PropertySource` devient inutile.

## Suite

Branche `04-spring-boot` : JAR autonome, Tomcat embarqué, premier `@RestController`.
