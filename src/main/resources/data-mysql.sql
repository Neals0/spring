INSERT INTO `pays` (`id`, `nom`)
VALUES (1, 'France'),
       (2, 'Allemagne'),
       (3, 'Portugal');

INSERT INTO `entreprise` (`id`, `nom`)
VALUES (1, 'Amazon'),
       (2, 'Google'),
       (3, 'Red hat');

INSERT INTO `emploi` (`id`, `nom`)
VALUES (1, 'Developpeur'),
       (2, 'Testeur'),
       (3, 'Chef de projet');

INSERT INTO `role` (`id`, `nom`)
VALUES (1, 'ROLE_UTILISATEUR'),
       (2, 'ROLE_ADMINISTRATEUR');

INSERT INTO utilisateur(prenom, nom, pays_id, entreprise_id, email, mot_de_passe, role_id, created_at, updated_at)
VALUES ("John", "DOE", 2, 1, "jd@a.com", "$2a$10$rURJI5NnPF7tjrFMxrgk/eTJ/hZW1WQPtffrzbESGrRdCiOL/Rs72", 1, "2023-01-01", "2023-01-01"),
       ("Sofiane", "FIANSO", 3, 1, "sf@a.com", "$2a$10$rURJI5NnPF7tjrFMxrgk/eTJ/hZW1WQPtffrzbESGrRdCiOL/Rs72", 2, UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `recherche_emploi_utilisateur` (`utilisateur_id`, `emploi_id`)
VALUES (1, 1),
       (1, 2),
       (2, 2);