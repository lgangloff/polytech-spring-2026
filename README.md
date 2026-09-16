# polytech-spring — branche `04-spring-boot`

Code de référence de l'atelier **« À vos claviers — Premier démarrage Spring Boot »**
(Java / Spring, partie 1/2).

## Ce qui change par rapport à `03-composants`

| Avant | Après |
|---|---|
| `spring-context` | `spring-boot-starter-webmvc`, sans numéro de version |
| `AppConfig` avec `@ComponentScan` et `@PropertySource` | supprimée : `@SpringBootApplication` remplit les deux rôles |
| `main` qui ouvre un `AnnotationConfigApplicationContext` | `SpringApplication.run(App.class, args)` |
| application console | JAR autonome, Tomcat embarqué sur le port 8080 |

`application.properties` est désormais chargé **automatiquement** : `@PropertySource` n'est
plus nécessaire.

> **Le starter a changé de nom en Spring Boot 4.** `spring-boot-starter-web` est devenu
> `spring-boot-starter-webmvc`. L'ancien nom subsiste comme alias déprécié, ce qui explique
> que les deux circulent : les ressources antérieures à fin 2025 — tutoriels, réponses
> Stack Overflow, anciens projets — utilisent `-web`.


## Lancer

```bash
./gradlew bootRun
```

Dans les traces de démarrage, repérer :

```
Tomcat initialized with port 8080 (http)
Root WebApplicationContext: initialization completed in ... ms
Tomcat started on port 8080 (http) with context path '/'
Started App in 1.4 seconds
```

## Les requêtes

`http/hello.http` contient les trois appels de l'exercice. Ouvrir le fichier dans VSCode et
cliquer sur *Send Request* au-dessus de chaque requête (extension REST Client).

| Requête | Résultat attendu |
|---|---|
| `GET /hello` | `200` — `Hello Polytech`, `Content-Type: text/plain` |
| `GET /hello/patient` avec `Accept: application/json` | `200` — l'objet sérialisé en JSON par Jackson |
| `GET /hello/patient` avec `Accept: application/xml` | **`406 Not Acceptable`** |

### Étape 6 — origine du 406

Il s'agit de la **négociation de contenu**. Spring croise ce que le client accepte
(`Accept`) avec ce que l'application sait produire. L'application ne produit que du JSON,
aucun convertisseur XML n'étant présent dans le classpath. Sans format commun, la réponse
est `406`.

Obtenir du XML demande d'ajouter le module Jackson correspondant, et rien d'autre : le code
Java reste inchangé.

```gradle
implementation 'tools.jackson.dataformat:jackson-dataformat-xml'
```

Le même endpoint répond alors en XML ou en JSON selon l'en-tête `Accept`, ce qui correspond
à la slide « Négociation — la même requête, deux résultats ».

## Suite

Branche `05-jpa-entites` : entités JPA, relations, PostgreSQL.
