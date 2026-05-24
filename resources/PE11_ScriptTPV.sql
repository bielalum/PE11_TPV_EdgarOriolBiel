DROP DATABASE IF EXISTS tpv_botiga;
CREATE DATABASE tpv_botiga;
USE tpv_botiga;

CREATE TABLE tipus_article (
    id INT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipus_article (id, nom) VALUES
(1, 'Camisa'),
(2, 'Pantaló');

CREATE TABLE articles (
    id INT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    id_tipus INT NOT NULL,
    talla_coll INT NULL,
    amplada_pit INT NULL,
    talla_cintura INT NULL,
    llargada_camal INT NULL,
    preu_base DECIMAL(10,2) NOT NULL,
    iva INT NOT NULL,
    stock INT NOT NULL,
    FOREIGN KEY (id_tipus) REFERENCES tipus_article(id),
    CHECK (iva BETWEEN 4 AND 21),
    CHECK (stock >= 0),
    CHECK (
        (id_tipus = 1 AND talla_coll BETWEEN 36 AND 52 AND amplada_pit BETWEEN 10 AND 15 AND talla_cintura IS NULL AND llargada_camal IS NULL)
        OR
        (id_tipus = 2 AND talla_cintura BETWEEN 24 AND 56 AND llargada_camal BETWEEN 32 AND 46 AND talla_coll IS NULL AND amplada_pit IS NULL)
    )
);

CREATE TABLE clients (
    dni VARCHAR(10) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefon VARCHAR(20)
);

INSERT INTO clients (dni, nom, email, telefon)
VALUES ('000', 'Client Genèric', NULL, NULL);

CREATE TABLE tiquets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_compra DATE NOT NULL,
    dni_client VARCHAR(10) NOT NULL,
    total_base DECIMAL(10,2) NOT NULL,
    total_iva DECIMAL(10,2) NOT NULL,
    total_final DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (dni_client) REFERENCES clients(dni)
);

CREATE TABLE linies_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_tiquet INT NOT NULL,
    id_article INT NOT NULL,
    quantitat INT NOT NULL,
    preu_base DECIMAL(10,2) NOT NULL,
    iva INT NOT NULL,
    preu_final DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_tiquet) REFERENCES tiquets(id) ON DELETE CASCADE,
    FOREIGN KEY (id_article) REFERENCES articles(id),
    CHECK (quantitat > 0),
    CHECK (iva BETWEEN 4 AND 21)
);
