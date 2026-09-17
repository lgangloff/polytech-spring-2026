# polytech-spring — branche `06-spring-data`

Code de référence de l'atelier **« Du repository au endpoint »** (Java / Spring, partie 2/2).

## Ce qui change par rapport à `05-jpa-entites`

| Avant | Après |
|---|---|
| `PatientStore`, `PatientDataBase`, `PatientSerialization` — le stockage écrit à la main | **supprimés** |
| — | `repository/PatientRepository extends JpaRepository<Patient, Long>` : une interface, aucune implémentation |
| — | une requête par convention de nommage : `findByNomContainingIgnoreCase` |
| — | une requête `@Query` avec `JOIN FETCH` : `findByIdWithDocteur`, et `findByIdWithPatients` côté docteur |
| — | `rest/PatientController`, qui expose l'**entité** ; les conséquences sont traitées au chapitre suivant |
| — | `rest/ApiExceptionHandler` : les erreurs au format `ProblemDetail` (RFC 9457) |

## Lancer

```bash
./gradlew bootRun
```

## Étape 3 — l'échec de GET /patients

```bash
curl -i http://localhost:8080/patients
```

**`500` avec un corps `application/problem+json`.** Le `-i` est nécessaire : l'échec se lit
dans la **réponse HTTP**, non dans la console.

**`LazyInitializationException: could not initialize proxy [Docteur#1] - no Session`.**

La transaction est fermée quand le contrôleur rend la main ; Jackson tente ensuite de
sérialiser le médecin traitant, une relation `LAZY` jamais chargée.

> **Une réponse `200 []`** indique que l'échec ne peut pas se produire : la table est vide,
> il n'y a aucun proxy à sérialiser. Les traces montrent alors un `select … from patient` et
> rien d'autre, sans second select sur `docteur`. Vérifier que `data.sql` s'est exécuté :
> `spring.sql.init.mode: always` **et** `defer-datasource-initialization: true` sont tous
> deux requis, le second parce que sans lui le script s'exécute avant la création des tables.
> En base, `select count(*) from patient;` doit renvoyer 2.

### Pourquoi la console reste silencieuse

Aucune stack trace n'apparaît au moment de l'échec.

La stack trace habituelle n'est pas écrite par Spring mais par Tomcat, lorsque l'exception
lui remonte. Ici, elle ne remonte pas :

1. Jackson échoue sur le proxy et encapsule la `LazyInitializationException` dans une
   `JsonMappingException` ;
2. le convertisseur de message la retransforme en `HttpMessageNotWritableException` ;
3. `ApiExceptionHandler extends ResponseEntityExceptionHandler`, et
   `HttpMessageNotWritableException` fait partie des exceptions que cette classe traite
   **par défaut**. L'exception est donc *résolue* : un `500` est renvoyé au client, et rien
   ne remonte au conteneur.

Spring ne trace une exception résolue qu'en **DEBUG**. Le niveau par défaut de Spring Boot
étant INFO, la console reste silencieuse. D'où la propriété ajoutée dans `application.yaml` :

```yaml
spring:
  mvc:
    log-resolved-exception: true
```

Elle branche un *warn logger* sur les `HandlerExceptionResolver` : toute exception résolue
produit désormais une ligne **WARN** dans la console.

```
WARN  o.s.w.s.m.m.a.ExceptionHandlerExceptionResolver :
  Resolved [org.springframework.http.converter.HttpMessageNotWritableException:
  Could not write JSON: could not initialize proxy [Docteur#1] - no Session]
```

Une ligne, et non une stack trace : `logException` passe le message sans l'exception. La
chaîne des causes figure dans le message.

Le même effet, sans modifier le fichier, le temps d'un lancement :

```bash
./gradlew bootRun --args='--spring.mvc.log-resolved-exception=true'
```

Sans cette propriété, l'échec reste invisible côté serveur, ce qui illustre le coût d'un
`@RestControllerAdvice` trop large.

> **Niveau de log : la catégorie à viser.** Cibler
> `logging.level.org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver`
> ne produit **aucun effet** : la classe déclare `LogFactory.getLog(getClass())`, donc la
> catégorie réelle est celle de la classe concrète — ici
> `…mvc.method.annotation.ExceptionHandlerExceptionResolver`.

Corriger l'échec en passant la relation en `EAGER` produit l'autre panne :
`StackOverflowError`. Le patient référence son docteur, qui référence sa patientèle, qui
contient le patient.

**Ces deux échecs ne doivent pas être corrigés ici** : ils font l'objet du chapitre suivant.
Ils ne résultent pas d'une erreur, mais de deux décisions de conception correctes — relation
bidirectionnelle et chargement paresseux — appliquées à un objet qui n'est pas destiné à cet
usage.

Ce qui fonctionne à ce stade :

| Requête | Réponse |
|---|---|
| `POST /patients` | `201` + `Location` — un patient neuf n'a ni adresse ni docteur à sérialiser |
| `GET /patients/9999` | `404` — `ProblemDetail` |
| `DELETE /patients/2` | `204` |

## Étape 6 — nombre de requêtes SQL

`show-sql: true` est actif : les requêtes apparaissent dans la console.

- **Sans `JOIN FETCH`** (remplacer `findByIdWithDocteur(id)` par `findById(id)`) : une
  requête pour le patient, puis une **seconde** dès la lecture de son médecin traitant. Sur
  une liste de N patients, cela fait N+1 requêtes.
- **Avec `JOIN FETCH`** : **une seule** requête, avec la jointure.

Déclarer les relations en `LAZY` puis charger explicitement ce qui est nécessaire déplace la
décision : le chargement n'est plus une propriété du modèle, il est décidé requête par
requête.

`DocteurRepository.findByIdWithPatients` applique le même mécanisme à une **collection**,
cas où le coût du N+1 est le plus élevé.

## Suite

Branche `07-dto` : pourquoi une entité JPA ne doit pas franchir le contrôleur.
