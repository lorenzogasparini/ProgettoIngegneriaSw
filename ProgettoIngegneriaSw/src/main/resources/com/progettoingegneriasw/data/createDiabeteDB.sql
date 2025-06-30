/* CANCELLA TUTTI I DATI DALLE TABELLE
-- Disabilita temporaneamente le chiavi esterne per evitare errori di vincolo
PRAGMA foreign_keys = OFF;

DELETE FROM log;
DELETE FROM rilevazione_sintomo;
DELETE FROM rilevazione_glicemia;
DELETE FROM rilevazione_farmaco;
DELETE FROM patologia_paziente;
DELETE FROM terapia;
DELETE FROM farmaco;
DELETE FROM patologia;
DELETE FROM paziente;
DELETE FROM diabetologo;
DELETE FROM amministratore;

-- Riabilita i vincoli
PRAGMA foreign_keys = ON;
*/

/* CANCELLA TUTTE LE TABELLE
DROP TABLE IF EXISTS log;
DROP TABLE IF EXISTS rilevazione_sintomo;
DROP TABLE IF EXISTS rilevazione_glicemia;
DROP TABLE IF EXISTS rilevazione_farmaco;
DROP TABLE IF EXISTS patologia_paziente;
DROP TABLE IF EXISTS terapia;
DROP TABLE IF EXISTS farmaco;
DROP TABLE IF EXISTS patologia;
DROP TABLE IF EXISTS paziente;
DROP TABLE IF EXISTS diabetologo;
DROP TABLE IF EXISTS amministratore;

*/

-- Tabella amministratore
CREATE TABLE amministratore (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
	nome VARCHAR(50) NOT NULL,
	cognome VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Tabella diabetologo
CREATE TABLE diabetologo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
	nome VARCHAR(50) NOT NULL,
	cognome VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Tabella paziente
CREATE TABLE paziente (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
	nome VARCHAR(50) NOT NULL,
	cognome VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    id_diabetologo INTEGER NOT NULL,
    data_nascita DATE,
    peso REAL,
    provincia_residenza VARCHAR(2),
    comune_residenza VARCHAR(100),
    note_paziente TEXT,
    FOREIGN KEY(id_diabetologo) REFERENCES diabetologo(id)
);


-- Tabella log
CREATE TABLE log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paziente INTEGER NOT NULL,
    id_diabetologo INTEGER NOT NULL,
    azione TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRE
    NT_TIMESTAMP,
    FOREIGN KEY(id_paziente) REFERENCES paziente(id),
    FOREIGN KEY(id_diabetologo) REFERENCES diabetologo(id)
);


-- Tabella rilevazione_glicemia
-- DESCRIZIONE CAMPO gravita
-- 0: valore normale
--    - prima pasto: 80 - 130 mg/dL
--    - dopo pasto: ≤ 180 mg/dL
--
-- 1: iperglicemia lieve
--    - prima pasto: 131–160 mg/dL
--    - dopo pasto: 181–200 mg/dL
-- 1: ipoglicemia lieve
--    - prima pasto: 70–79 mg/dL
--    - dopo pasto: 70–79 mg/dL
--
-- 2: iperglicemia moderata
--    - prima pasto: 161–200 mg/dL
--    - dopo pasto: 201–250 mg/dL
-- 2: ipoglicemia moderata
--    - prima pasto: 60–69 mg/dL
--    - dopo pasto: 60–69 mg/dL
--
-- 3: iperglicemia grave
--    - prima pasto: >200 mg/dL
--    - dopo pasto: >250 mg/dL
-- 3: ipoglicemia grave
--    - prima pasto: <60 mg/dL
--    - dopo pasto: <60 mg/dL
CREATE TABLE rilevazione_glicemia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paziente INTEGER NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    valore INTEGER NOT NULL, -- in mg
    gravita INTEGER CHECK (gravita >= 0 AND gravita <= 3),
    prima_pasto BOOLEAN, -- se false il valore è dopo il pasto
    FOREIGN KEY(id_paziente) REFERENCES paziente(id)
);

-- Tabella rilevazione_sintomo
CREATE TABLE rilevazione_sintomo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paziente INTEGER NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    sintomo TEXT NOT NULL,
    intensita INTEGER CHECK (intensita >= 1 AND intensita <= 10), -- valore da 1 a 10
    FOREIGN KEY(id_paziente) REFERENCES paziente(id)
);

-- Tabella patologia
CREATE TABLE patologia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(1000) NOT NULL,
    codice_icd VARCHAR(10) NOT NULL UNIQUE -- codice standardizzato (ICD) a livello internazionale
);


-- Tabella farmaco
CREATE TABLE farmaco (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codice_aic VARCHAR(10) NOT NULL UNIQUE,
    nome VARCHAR(1000) NOT NULL
);

-- Tabella terapia
CREATE TABLE terapia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_farmaco INTEGER NOT NULL,
    dosi_giornaliere INTEGER, -- numero di dosi al giorno
    quantita_per_dose REAL, -- in g
    note TEXT,
    FOREIGN KEY(id_farmaco) REFERENCES farmaco(id)
);

-- Tabella patologia_paziente
CREATE TABLE patologia_paziente (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paziente INTEGER NOT NULL,
    id_patologia INTEGER NOT NULL,
    id_terapia INTEGER,
    data_diagnosi DATE,
	note_patologia TEXT, -- note specifiche relative alla patologia di questo paziente
    FOREIGN KEY(id_paziente) REFERENCES paziente(id),
    FOREIGN KEY(id_patologia) REFERENCES patologia(id),
    FOREIGN KEY(id_terapia) REFERENCES terapia(id),
    UNIQUE(id_paziente, id_patologia) -- un paziente può essere diagnosticato solo una volta con quella patologia
    				      -- ma per la stessa patologia può avere più terapia
);

-- Tabella rilevazione_farmaco
CREATE TABLE rilevazione_farmaco (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paziente INTEGER NOT NULL,
	id_farmaco INTEGER NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    quantita REAL, -- in mg
    note TEXT,
    FOREIGN KEY(id_paziente) REFERENCES paziente(id),
	FOREIGN KEY(id_farmaco) REFERENCES farmaco(id)
);

-- Tabella alert
-- se id_rilevazione punta ad un id di rilevazione_farmaco è relativo all'ultima volta che è stato assunto il farmaco in questione
-- se id_rilevazione punta ad un id di rilevazione_glicemia è relativo alla rilevazione di glicemia di interesse
CREATE TABLE alert (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_paziente INTEGER NOT NULL,
    id_rilevazione INTEGER NOT NULL, -- "dynamic FOREIGN KEY" related to tipo_alert
    tipo_alert TEXT NOT NULL CHECK(tipo_alert IN ('glicemia', 'promemoriaFarmaco', 'farmacoNonAssuntoDa3Giorni')),
    data_alert TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    letto BOOLEAN DEFAULT 0, -- se è stato visualizzato o no
    FOREIGN KEY(id_paziente) REFERENCES paziente(id)
);