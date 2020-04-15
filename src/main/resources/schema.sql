DROP TABLE IF EXISTS voitures;

CREATE TABLE voitures  (
    id BIGINT auto_increment NOT NULL PRIMARY KEY,
    marque VARCHAR(40),
    modele VARCHAR(40),
    etat VARCHAR(40)
);