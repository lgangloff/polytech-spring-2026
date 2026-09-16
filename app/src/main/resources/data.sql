-- Jeu de données de démonstration, rejoué à chaque démarrage (ddl-auto: create-drop).
-- Aucun identifiant n'est écrit en dur : les colonnes sont auto-générées et les
-- relations sont résolues par sous-requête. Cela évite de désynchroniser la
-- séquence d'identité, ce qui ferait échouer les insertions suivantes.

insert into adresse (numero, rue, code_postal, ville)
values ('12', 'rue des Lilas', '67000', 'Strasbourg'),
       ('3', 'avenue de la Foret', '67200', 'Strasbourg');

insert into docteur (nom, prenom, rpps, date_diplome, specialite, id_adresse)
values ('House', 'Gregory', '10001234567', '1991-06-28', 'INFECTIOLOGIE',
        (select id from adresse where rue = 'avenue de la Foret'));

insert into patient (nom, prenom, email, date_naissance, id_adresse, id_docteur)
values ('Lovelace', 'Ada', 'ada.lovelace@polytech.fr', '1815-12-10',
        (select id from adresse where rue = 'rue des Lilas'),
        (select id from docteur where nom = 'House')),
       ('Hopper', 'Grace', 'grace.hopper@polytech.fr', '1906-12-09',
        null,
        (select id from docteur where nom = 'House'));
