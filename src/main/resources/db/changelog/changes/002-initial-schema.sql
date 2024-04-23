--liquibase formatted sql

--changeset user:1

CREATE TABLE _user (
                       id SERIAL PRIMARY KEY,
                       nom VARCHAR(255),
                       prenom VARCHAR(255),
                       numtel VARCHAR(255),
                       adresse VARCHAR(255),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL
);

--changeset user:2

CREATE TABLE annonce (
                         id SERIAL PRIMARY KEY,
                         titre VARCHAR(255),
                         description_longue TEXT,
                         description_courte TEXT,
                         adresse VARCHAR(255),
                         ville VARCHAR(255),
                         grandeur VARCHAR(255),
                         type_annonce VARCHAR(255),
                         date_disponibilite DATE,
                         prix NUMERIC(10, 2),
                         active BOOLEAN,
                         user_id BIGINT,
                         FOREIGN KEY (user_id) REFERENCES _user(id)
);

--changeset user:3
-- Inserting users from JSON file
INSERT INTO _user (id, nom, prenom, numtel, adresse, email, password, role) VALUES
                                                                                (1, 'Dupont', 'Jean', '0102030405', '123 rue de Père-Marquette, G8Z 4N6 Trois-Rivieres', 'jean.dupont@gmail.com', 'tatat543', 'USER'),
                                                                                (2, 'Durand', 'Marie', '0607080910', '123 Louis-Pinard,G9Z 5N2 Trois-Rivieres', 'marie.durand@gmail.com', 'yoho3214', 'USER'),
                                                                                (3, 'Duval', 'Annie', '2307082310', '456 rue fouché, G87 Z56 Trois-Rivieres', 'annie.duval@hotmail.com', 'anniedco', 'USER');

--changeset user:4
-- Inserting annonces from JSON file (part 1)
INSERT INTO annonce (titre, description_longue, description_courte, adresse, ville, grandeur, type_annonce, date_disponibilite, prix, active, user_id) VALUES
                                                                                                                                                        ('Spacieux 4 1/2 lumineux à Montréal', 'Appartement de deux chambres avec une grande cuisine ouverte, un salon lumineux, et une vue imprenable sur le parc. Situé dans un quartier vivant, à proximité des transports en commun, des écoles et des commerces.', '2 chambres, cuisine ouverte, vue sur parc', '1234 rue Saint-Denis, Montréal, Québec, H2X 3K5', 'Montréal', '3 1/2', 'Maison', '2024-01-12', 935.00, FALSE, 1),
                                                                                                                                                        ('Condo moderne 3 1/2 au coeur de Québec', 'Condo parfait pour les professionnels, offrant une chambre, un espace de vie ouvert et une terrasse privée. À quelques pas de la vieille ville, des restaurants, et des boutiques.', '1 chambre, espace ouvert, terrasse privée', '5678 rue Saint-Joseph, Québec, G1K 2X5', 'Québec', '4 1/2', 'appartement', '2024-05-10', 1244.00, FALSE, 2),
                                                                                                                                                        ('Charmant appartement à Laval', 'Ce magnifique 5 1/2 offre confort et commodité, avec trois chambres, deux salles de bains, et un grand balcon. Situé dans un quartier calme, idéal pour les familles.', '3 chambres, 2 SDB, grand balcon', '9101 boulevard Saint-Martin, Laval, Québec, H7T 2Y8', 'Laval', '1 1/2', 'Studio', '2024-03-23', 1122.00, TRUE, 2),
                                                                                                                                                        ('Studio urbain à Gatineau', 'Studio moderne idéalement situé près des services essentiels, parfait pour étudiants et jeunes professionnels. Profitez d''un accès facile aux transports en commun.', 'Studio moderne, proche services', '2345 avenue du Parc, Gatineau, Québec, J8X 4G8', 'Gatineau', '4 1/2', 'Condo', '2024-03-28', 1415.00, FALSE, 3),
                                                                                                                                                        ('Maison de ville à Sherbrooke', 'Maison de ville spacieuse avec jardin, trois chambres, salon, cuisine équipée et garage. Quartier familial et sécuritaire, proche des écoles et parcs.', '3 chambres, jardin, quartier familial', '3456 rue King Ouest, Sherbrooke, Québec, J1L 1P9', 'Sherbrooke', '5 1/2', 'Appartement', '2024-05-17', 1339.00, FALSE, 3);


