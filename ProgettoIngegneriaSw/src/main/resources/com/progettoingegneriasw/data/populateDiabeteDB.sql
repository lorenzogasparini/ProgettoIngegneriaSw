
-- Amministratori
INSERT INTO amministratore (nome, cognome, username, password) VALUES
('Lorenzo', 'Gasparini', 'admin1', 'hashed_pw1'),
('Andrea', 'Pellizzari', 'admin2', 'hashed_pw2');

-- Diabetologi
INSERT INTO diabetologo (nome, cognome, username, password, email) VALUES
('Luca', 'Rossi', 'drrossi', 'hashed_pw3', 'rossi@asl.it'),
('Pietro', 'Bianchi', 'drbianchi', 'hashed_pw4', 'bianchi@asl.it');

-- Pazienti
INSERT INTO paziente (nome, cognome, username, password, email, id_diabetologo, data_nascita, peso, provincia_residenza, comune_residenza, note_paziente) VALUES
('Mario' , 'Rossi', 'mario.rossi', 'hashed_pw5', 'mario.rossi@gmail.com', 1, '1980-05-12', 80.5, 'RM', 'Roma', 'Segue dieta bilanciata'),
('Lucia', 'Verdi', 'lucia.verdi', 'hashed_pw6', 'lucia.verdi@gmail.com', 1, '1975-11-23', 65.0, 'MI', 'Milano', 'Allergica a penicillina'),
('Giulia', 'Bianchi', 'giulia.bianchi', 'hashed_pw7', 'giulia.bianchi@gmail.com', 2, '1990-02-08', 72.3, 'TO', 'Torino', NULL),
('Andrea', 'Neri', 'andrea.neri', 'hashed_pw8', 'andrea.neri@gmail.com', 2, '2000-07-30', 78.0, 'FI', 'Firenze', 'Sportivo, non fumatore');

-- Log
INSERT INTO log (id_paziente, id_diabetologo, azione) VALUES
(1, 1, 'Login'),
(2, 1, 'Aggiornato peso'),
(3, 2, 'Aggiunta rilevazione glicemia');

-- Patologie (con codici ICD)
INSERT INTO patologia (nome, codice_icd) VALUES
('Diabete mellito tipo 2', 'E11'),
('Ipertensione', 'I10'),
('Ipertensione secondaria', 'I15'),
('Dislipidemia', 'E78'),
('Obesità', 'E66');

-- Farmaci
INSERT INTO farmaco (codice_aic, nome) VALUES
('012345678', 'Metformina'),
('987654321', 'Lisinopril'),
('111222333', 'Atorvastatina'),
('444555666', 'Insulina');

-- Terapie
INSERT INTO terapia (id_farmaco, dosi_giornaliere, quantita_per_dose, note) VALUES
(1, 2, 500, 'Dopo i pasti'),
(2, 1, 10, 'Al mattino'),
(3, 1, 20, 'Preferibilmente la sera'),
(4, 3, 15, 'Prima dei pasti');

-- Patologia_Paziente
INSERT INTO patologia_paziente (id_paziente, id_patologia, id_terapia, data_diagnosi, note_patologia) VALUES
(1, 1, 1, '2021-06-01', 'Controlli regolari'),
(1, 2, 2, '2020-01-15', NULL),
(2, 1, NULL, '2022-03-12', 'Necessita insulina'),
(3, 3, 3, '2021-10-05', 'Stabile'),
(4, 4, 1, '2019-12-30', NULL);

-- Rilevazione Glicemia
INSERT INTO rilevazione_glicemia (id_paziente, valore, gravita, prima_pasto) VALUES
(1, 145, 1, 1),
(1, 140, 0, 0),
(2, 230, 2, 0),
(3, 60, 2, 1),
(4, 95, 0, 1);

-- Rilevazione Sintomo
INSERT INTO rilevazione_sintomo (id_paziente, sintomo, intensita) VALUES
(1, 'Mal di testa', 3),
(2, 'Stanchezza', 6),
(3, 'Nausea', 2),
(4, 'Capogiri', 5);

-- Rilevazione Farmaco
INSERT INTO rilevazione_farmaco (id_paziente, id_farmaco, quantita, note) VALUES
(1, 1, 500, 'Dopo colazione'),
(1, 1, 500, 'Dopo cena'),
(2, 4, 15, 'Insulina serale'),
(3, 1, 20, 'Routine sera');


-- Alert
INSERT INTO alert (id_paziente, id_rilevazione, tipo_alert, data_alert, letto) VALUES
(2, 3, 'glicemia', '2025-05-17 08:00:00', 0),
(1, 2, 'glicemia', '2025-05-16 18:30:00', 1),
(1, 2, 'promemoriaFarmaco', '2025-05-15 21:00:00', 0),
(2, 3, 'farmacoNonAssuntoDa3Giorni', '2025-05-16 22:00:00', 0),
(4, 5, 'glicemia', '2025-05-17 07:00:00', 0),
(3, 4, 'promemoriaFarmaco', '2025-05-16 08:00:00', 0);
