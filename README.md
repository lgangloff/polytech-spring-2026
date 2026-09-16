# polytech-spring — branche `02-ioc-spring`

Code de référence de l'atelier **« À vos claviers — Conteneur, cycle de vie et portées »**
(Java / Spring, partie 1/2).

## Contenu de la branche

| Fichier | Rôle |
|---|---|
| `app/build.gradle` | le *build.gradle de référence* du cours, avec `spring-context` et `jakarta.annotation-api` |
| `patient/PatientStore` | le contrat de persistance |
| `patient/PatientDataBase` | l'implémentation, avec `@PostConstruct` / `@PreDestroy` |
| `patient/PatientService` | la logique métier, dépendance injectée par le **constructeur** |
| `patient/Consultation` | le bean déclaré en scope `prototype` |
| `AppConfig` | la classe `@Configuration` qui déclare les beans |
| `App` | le `main`, contexte ouvert en try-with-resources |

## Lancer

```bash
./gradlew run
```

## Points à observer

1. **L'ordre du cycle de vie.** Les `@PostConstruct` sont appelées après l'injection des
   dépendances, dans l'ordre de construction du graphe : `PatientDataBase` d'abord, aucun
   bean n'étant requis pour le construire, `PatientService` ensuite. Les `@PreDestroy` sont
   appelées à la fermeture du contexte, dans l'ordre inverse.
2. **Singleton et prototype.** `ObjectUtils.identityToString` affiche l'identité de l'objet :
   deux appels à `getBean(PatientStore.class)` renvoient la **même** référence, deux appels à
   `getBean(Consultation.class)` en renvoient **deux différentes**.
3. **Question 9 — `@PreDestroy` sur un prototype ?** Non, elle n'est jamais appelée. Spring
   ne conserve aucune référence sur un bean prototype après l'avoir livré : il ne peut pas
   déterminer le moment où l'objet cesse d'être utilisé, donc pas davantage le détruire.
   `@PostConstruct` est en revanche appelée à chaque création.

## Suite

Branche `03-composants` : `@ComponentScan`, stéréotypes, `@Primary` / `@Qualifier`, `@Value`.
