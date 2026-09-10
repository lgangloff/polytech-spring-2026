# polytech-spring

Code de référence du cours **Développement Fullstack — Java / Spring** (Polytech 1.1).

Une branche par atelier « À vos claviers ». Chaque branche correspond à l'**état d'arrivée**
de son atelier et contient son propre `README.md` : les étapes, les points à observer dans
les traces, et les réponses aux questions posées en séance.

| Branche | Atelier | Séance |
|---|---|---|
| `main` | point de départ : le problème du couplage, avant Spring | 1 — chapitre « Inversion de contrôle » |
| `02-ioc-spring` | conteneur IoC, cycle de vie et portées | 1 |
| `03-composants` | `@ComponentScan`, stéréotypes, `@Primary` / `@Qualifier`, `@Value` | 1 |
| `04-spring-boot` | JAR autonome, Tomcat embarqué, premier `@RestController` | 1 |
| `05-jpa-entites` | entités JPA, relations, PostgreSQL | 2 |
| `06-spring-data` | repositories dérivés, `@Query` / `JOIN FETCH` | 2 |
| `07-dto` | la frontière DTO, et CORS | 2 |

Chaque branche part de la précédente : `git diff main..02-ioc-spring` montre les
modifications apportées par l'atelier, et elles seules.

## Prérequis

| Outil | Version |
|---|---|
| JDK | **26** — Spring Boot 4 exige 17 au minimum, et 26 est la version la plus haute supportée |
| Gradle | **aucune installation requise** : le wrapper du dépôt fournit Gradle 9.7.1 |
| PostgreSQL | à partir de la branche `05-jpa-entites`, base `covid-db` |
| Client HTTP | l'extension VSCode [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) — les requêtes sont versionnées dans `http/` |

Socle technique : Spring Boot 4.1.1, Spring Framework 7, Java 26.

### Utiliser `./gradlew`, et non `gradle`

Le wrapper fige la version de Gradle pour tous les postes. Un Gradle installé localement,
plus ancien que la JVM qui le lance, échoue sur le message suivant :

```
BUG! exception in phase 'semantic analysis' in source unit '_BuildScript_'
Unsupported class file major version 66
```

La « major version 66 » désigne du bytecode Java 22 : ce Gradle ne sait pas lire la JVM sur
laquelle il s'exécute. Table de correspondance :
**65 = Java 21, 66 = Java 22, 67 = Java 23, 68 = Java 24, 69 = Java 25, 70 = Java 26.**
Gradle 9.7.1 accepte les JVM 17 à 26.

Le `build.gradle` déclare par ailleurs une **toolchain Java 26** : la compilation cible 26
quelle que soit la JVM qui lance Gradle, et le plugin foojay télécharge un JDK 26 si le poste
n'en dispose pas. Le build est ainsi reproductible d'un poste à l'autre.

## Démarrer

```bash
git clone <url-du-depot>
cd polytech-spring
git switch 02-ioc-spring
./gradlew run          # branches main à 03
./gradlew bootRun      # branches 04 à 07
```

## Fil rouge

L'exemple utilisé dans l'ensemble du cours est un service d'enregistrement de patients :

1. **`main`** — `PatientService` mélange logique métier et code technique. La persistance
   est extraite, une interface est posée, le `ServiceLocator` est évalué, puis l'injection
   par constructeur est retenue. Reste la question de savoir qui construit quoi.
2. **`02` → `04`** — Spring répond à cette question, puis l'application est exposée en HTTP.
3. **`05` → `07`** — les données passent en base, un contrat d'API se distingue du schéma
   relationnel, et le code technique de persistance disparaît.

Le TD porte sur une bibliothèque de films : même architecture, domaine différent.
