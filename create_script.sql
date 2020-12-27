-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2020-12-18 11:38:21.371

-- tables
-- Table: Doktor
CREATE TABLE Doktor (
    id int  NOT NULL,
    Jmeno nvarchar(15)  NOT NULL,
    Prijmeni nvarchar(15)  NOT NULL,
    Klinika_id int  NOT NULL,
    tel_cislo int  NOT NULL,
    CONSTRAINT Doktor_pk PRIMARY KEY  (id)
);

-- Table: Doktor_Procedura
CREATE TABLE Doktor_Procedura (
    id int  NOT NULL,
    Doktor_id int  NOT NULL,
    Procedura_id int  NOT NULL,
    CONSTRAINT Doktor_Procedura_pk PRIMARY KEY  (id)
);

-- Table: Klinika
CREATE TABLE Klinika (
    id int  NOT NULL,
    Nazev nvarchar(20)  NOT NULL,
    Adresa nvarchar(30)  NOT NULL,
    Mesto nvarchar(20)  NOT NULL,
    CONSTRAINT Klinika_pk PRIMARY KEY  (id)
);

-- Table: Pacient
CREATE TABLE Pacient (
    id int  NOT NULL,
    Jmeno nvarchar(20)  NOT NULL,
    Prijmeni nvarchar(20)  NOT NULL,
    Pohlavi char(1)  NOT NULL,
    Zdravotni_Pojistovna_kod int  NOT NULL,
    CONSTRAINT Pacient_pk PRIMARY KEY  (id)
);

-- Table: Procedura
CREATE TABLE Procedura (
    id int  NOT NULL,
    nazev nvarchar(50)  NOT NULL,
    cena int  NOT NULL,
    CONSTRAINT Procedura_pk PRIMARY KEY  (id)
);

-- Table: Zdravotni_Pojistovna
CREATE TABLE Zdravotni_Pojistovna (
    kod int  NOT NULL,
    Nazev char(50)  NOT NULL,
    CONSTRAINT Zdravotni_Pojistovna_pk PRIMARY KEY  (kod)
);

-- Table: Zprava
CREATE TABLE Zprava (
    id int  NOT NULL,
    Pacient_id int  NOT NULL,
    Doktor_Procedura_id int  NOT NULL,
    datum date  NOT NULL,
    CONSTRAINT Zprava_pk PRIMARY KEY  (id)
);

-- foreign keys
-- Reference: Doktor_Klinika (table: Doktor)
ALTER TABLE Doktor ADD CONSTRAINT Doktor_Klinika
    FOREIGN KEY (Klinika_id)
    REFERENCES Klinika (id);

-- Reference: Pacient_Zdravotni_Pojistovna (table: Pacient)
ALTER TABLE Pacient ADD CONSTRAINT Pacient_Zdravotni_Pojistovna
    FOREIGN KEY (Zdravotni_Pojistovna_kod)
    REFERENCES Zdravotni_Pojistovna (kod);

-- Reference: Table_8_Doktor (table: Doktor_Procedura)
ALTER TABLE Doktor_Procedura ADD CONSTRAINT Table_8_Doktor
    FOREIGN KEY (Doktor_id)
    REFERENCES Doktor (id);

-- Reference: Table_8_Procedura (table: Doktor_Procedura)
ALTER TABLE Doktor_Procedura ADD CONSTRAINT Table_8_Procedura
    FOREIGN KEY (Procedura_id)
    REFERENCES Procedura (id);

-- Reference: Zprava_Doktor_Procedura (table: Zprava)
ALTER TABLE Zprava ADD CONSTRAINT Zprava_Doktor_Procedura
    FOREIGN KEY (Doktor_Procedura_id)
    REFERENCES Doktor_Procedura (id);

-- Reference: Zprava_Pacient (table: Zprava)
ALTER TABLE Zprava ADD CONSTRAINT Zprava_Pacient
    FOREIGN KEY (Pacient_id)
    REFERENCES Pacient (id);

-- End of file.