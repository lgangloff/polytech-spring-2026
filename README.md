# polytech-spring — branche `05-jpa-entites`

Code de référence de l'atelier **« À vos claviers — Entités et relations »**
(Java / Spring, partie 2/2).

## Prérequis

Une base PostgreSQL nommée `covid-db`, créée avec pgAdmin ou en ligne de commande :

```bash
createdb -U postgres covid-db
```

Les identifiants de connexion sont dans `app/src/main/resources/application.yaml`
(`postgres` / `postgres`) ; les adapter à l'installation locale.

## Ce qui change par rapport à `04-spring-boot`

| Avant | Après |
|---|---|
| `spring-boot-starter-webmvc` | + `spring-boot-starter-data-jpa` et le driver `runtimeOnly org.postgresql:postgresql` |
| `application.properties` | `application.yaml` : datasource, `ddl-auto: create-drop`, `show-sql`, `defer-datasource-initialization` |
| `Patient` objet Java ordinaire | trois entités : `Adresse`, `Patient`, `Docteur` |

## Lancer

```bash
./gradlew bootRun
```

Vérifier ensuite, dans pgAdmin, que les tables `adresse`, `patient` et `docteur` ont été
créées et que `data.sql` les a peuplées.

## Ce que `@Column` décrit, sur `Docteur`

`Docteur.rpps` porte les trois contraintes de la slide `@Column` : `nullable = false`,
`unique = true`, `length = 11`, soit le format du numéro RPPS d'un professionnel de santé.
`dateDiplome` illustre `@Column(name = "date_diplome")`, et `specialite` est une énumération
annotée `@Enumerated(EnumType.STRING)`.

Cette dernière annotation a une conséquence directe : avec `ORDINAL`, la base stocke le
**rang** de la constante. Réordonner l'énumération `Specialite` modifierait alors la
spécialité de tous les docteurs enregistrés, sans erreur au démarrage.

## Les relations mises en place

| Relation | Annotation | Ce que produit Hibernate |
|---|---|---|
| un patient habite à une adresse | `@OneToOne(cascade = ALL, orphanRemoval = true)` sur `Patient.adresse` | colonne `patient.id_adresse` |
| un docteur travaille à une adresse | `@ManyToOne` sur `Docteur.adresse` | colonne `docteur.id_adresse` |
| un patient a un médecin traitant | `@ManyToOne` sur `Patient.medecinTraitant` — **côté propriétaire** | colonne `patient.id_docteur` |
| un docteur a une patientèle | `@OneToMany(mappedBy = "medecinTraitant")` sur `Docteur.patients` | **rien** : la relation est déjà portée par la colonne ci-dessus |

Deux décisions de conception :

- **Cascade.** L'adresse d'un patient n'a pas d'existence propre : `cascade = ALL` et
  `orphanRemoval`. La patientèle d'un docteur, elle, a une existence propre : aucune
  cascade. `CascadeType.ALL` appliqué systématiquement est une source d'erreurs.
- **Fetch.** Toutes les relations sont déclarées `LAZY`, y compris les `@ManyToOne` dont la
  valeur par défaut est `EAGER`. Le chargement est demandé explicitement
  (`JOIN FETCH`, branche `06-spring-data`).

## Étape 8 — manipulation

Passer la relation `Docteur → patients` en **unidirectionnelle** : supprimer le
`@ManyToOne medecinTraitant` de `Patient` et retirer le `mappedBy` du `@OneToMany` de
`Docteur` :

```java
@OneToMany
private List<Patient> patients = new ArrayList<>();
```

Redémarrer et comparer le schéma. Hibernate n'a plus d'information sur la colonne de
jointure : il crée une **table de jointure** `docteur_patients`, avec une contrainte
d'unicité sur `patients_id` — un patient ne peut donc pas avoir deux médecins traitants.
Si cette contrainte n'est pas voulue, la relation est en réalité un `@ManyToMany`.

Revenir à la version bidirectionnelle avant de passer à la suite.

## Suite

Branche `06-spring-data` : une interface qui étend `JpaRepository`, et la suppression du
code de persistance écrit à la main.
