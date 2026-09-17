# polytech-spring — branche `07-dto`

Code de référence de l'atelier **« Poser la frontière DTO »** (Java / Spring, partie 2/2).
C'est l'état d'arrivée du cours.

## Ce qui change par rapport à `06-spring-data`

| Avant | Après |
|---|---|
| `PatientController` renvoie des entités, et échoue | il ne manipule plus que des **DTO** |
| — | `dto/PatientDto`, `dto/PatientCreationDto`, `dto/PatientDetailDto`, `dto/PatientMapper` |
| — | `rest/PatientEntityDemoController` : l'ancien contrôleur, conservé sous profil `demo` |
| — | `config/CorsConfig` : la politique CORS, globale et externalisée |

## Lancer

```bash
./gradlew bootRun
```

Les requêtes sont dans `http/patients.http`, à exécuter avec l'extension REST Client.

## 1. Reproduire les deux échecs

L'ancien contrôleur est conservé sous profil `demo` :

```bash
./gradlew bootRun --args='--spring.profiles.active=demo'
curl -i http://localhost:8080/demo/patients
```

**Échec obtenu : `LazyInitializationException`.** La transaction est fermée quand le
contrôleur rend la main ; Jackson tente de sérialiser une relation `LAZY` jamais chargée.

Le `-i` est nécessaire : l'échec se lit dans la **réponse HTTP**, un `500` en
`application/problem+json`. Côté console, `ApiExceptionHandler` a *résolu* l'exception —
`HttpMessageNotWritableException` fait partie de ce que `ResponseEntityExceptionHandler`
traite par défaut — et Spring ne trace une exception résolue qu'en `DEBUG`. C'est le rôle de
`spring.mvc.log-resolved-exception: true`, héritée de la branche précédente : elle fait
passer ces exceptions en `WARN`. Sans elle, rien n'apparaît.

**Pour obtenir le second échec**, passer `Patient.medecinTraitant` en
`fetch = FetchType.EAGER` : `StackOverflowError`, par récursion infinie sur la relation
bidirectionnelle.

## 2. Les solutions apparentes

| Réflexe | Ce que cela règle | Ce que cela coûte |
|---|---|---|
| `@JsonIgnore` | la récursion | le format de sortie de l'API est décidé par des annotations posées sur le modèle de base : le contrat d'API devient un effet de bord du schéma |
| passer en `EAGER` | la `LazyInitializationException` | chaque lecture d'un patient charge son docteur, sa patientèle et leurs adresses : une requête en devient dix |
| `open-in-view: true` | la `LazyInitializationException` | les requêtes SQL sont déclenchées par le sérialiseur, hors de tout contrôle — la moins bonne des trois |

## 3. La frontière

Une entité JPA modélise un schéma relationnel. Un DTO modélise un contrat d'API. Ce ne sont
pas les mêmes objets, et ils ne changent pas pour les mêmes raisons.

| Couche | Ce qu'elle manipule |
|---|---|
| `PatientController` | **uniquement** des DTO — la classe `Patient` n'apparaît dans aucun de ses imports |
| `PatientService` | des DTO côté contrôleur, des entités côté persistance : la conversion a lieu ici |
| `PatientRepository` | **uniquement** des entités — les DTO n'y sont pas référencés |

Après la bascule, le JSON est plat et les traces SQL ne montrent plus qu'une seule requête.

La dissymétrie entrée / sortie est volontaire : `PatientCreationDto` n'a **pas**
d'identifiant, celui-ci étant généré par le serveur ; `PatientDto` le renvoie. Et
`PatientDetailDto` ajoute le nom du médecin traitant : deux vues du même objet, ce qu'une
classe unique ne permet pas.

## Les endpoints

| Requête | Réponse |
|---|---|
| `GET /patients` | `200` — la liste, en `PatientDto` |
| `GET /patients?nom=love` | `200` — la recherche par convention de nommage |
| `GET /patients/1` | `200` — `PatientDetailDto`, patient **et** médecin traitant |
| `GET /patients/9999` | `404` — `ProblemDetail` |
| `POST /patients` | `201` + en-tête `Location` |
| `PUT /patients/1/medecin/1` | `200` — affecte le médecin traitant |
| `DELETE /patients/2` | `204` |

## CORS

`config/CorsConfig` autorise `http://localhost:4200`, l'origine du front Angular de la séance
suivante. La liste est une propriété (`app.cors.allowed-origins`), et non une constante.

Un blocage CORS n'est **pas** une panne de l'API : celle-ci a répondu correctement. C'est le
navigateur qui refuse de remettre la réponse au code JavaScript. Depuis REST Client ou cURL,
la même requête aboutit, aucun navigateur n'étant impliqué.

## Les branches du cours

| Branche | Atelier |
|---|---|
| `main` | point de départ : le problème du couplage, avant Spring |
| `02-ioc-spring` | conteneur IoC, cycle de vie et portées |
| `03-composants` | `@ComponentScan`, stéréotypes, `@Primary` / `@Qualifier`, `@Value` |
| `04-spring-boot` | JAR autonome, Tomcat embarqué, premier `@RestController` |
| `05-jpa-entites` | entités JPA, relations, PostgreSQL |
| `06-spring-data` | repositories dérivés, `@Query` / `JOIN FETCH` |
| `07-dto` | la frontière DTO, et CORS |
