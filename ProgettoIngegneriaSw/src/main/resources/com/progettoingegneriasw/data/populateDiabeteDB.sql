-- Amministratori
INSERT INTO amministratore (username, password) VALUES
('admin1', 'hashed_pw1'),
('admin2', 'hashed_pw2');

-- Diabetologi
INSERT INTO diabetologo (username, password, email) VALUES
('drrossi', 'hashed_pw3', 'rossi@asl.it'),
('drbianchi', 'hashed_pw4', 'bianchi@asl.it');

-- Pazienti
INSERT INTO paziente (username, password, email, id_diabetologo, data_nascita, peso, provincia_residenza, comune_residenza, note_paziente) VALUES
('mario.rossi', 'hashed_pw5', 'mario.rossi@gmail.com', 1, '1980-05-12', 80.5, 'RM', 'Roma', 'Segue dieta bilanciata'),
('lucia.verdi', 'hashed_pw6', 'lucia.verdi@gmail.com', 1, '1975-11-23', 65.0, 'MI', 'Milano', 'Allergica a penicillina'),
('giulia.bianchi', 'hashed_pw7', 'giulia.bianchi@gmail.com', 2, '1990-02-08', 72.3, 'TO', 'Torino', NULL),
('andrea.neri', 'hashed_pw8', 'andrea.neri@gmail.com', 2, '2000-07-30', 78.0, 'FI', 'Firenze', 'Sportivo, non fumatore');

-- Log
INSERT INTO log (id_paziente, id_diabetologo, azione) VALUES
(1, 1, 'Login'),
(2, 1, 'Aggiornato peso'),
(3, 2, 'Aggiunta rilevazione glicemia');

-- Patologie (con codici ICD)
INSERT INTO patologia (nome, codice_icd) VALUES
('Diabete mellito tipo 2', 'E11'),
('Ipertensione', 'I10'),
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
(2, 1, 4, '2022-03-12', 'Necessita insulina'),
(3, 3, 3, '2021-10-05', 'Stabile'),
(4, 4, 1, '2019-12-30', NULL);

-- Rilevazione Glicemia
INSERT INTO rilevazione_glicemia (id_paziente, valore, prima_pasto) VALUES
(1, 120, 1),
(1, 140, 0),
(2, 180, 0),
(3, 100, 1),
(4, 95, 1);

-- Rilevazione Sintomo
INSERT INTO rilevazione_sintomo (id_paziente, sintomo, intensita) VALUES
(1, 'Mal di testa', 3),
(2, 'Stanchezza', 6),
(3, 'Nausea', 2),
(4, 'Capogiri', 5);

-- Rilevazione Farmaco
INSERT INTO rilevazione_farmaco (id_paziente, quantita, note) VALUES
(1, 500, 'Dopo colazione'),
(1, 500, 'Dopo cena'),
(2, 15, 'Insulina serale'),
(3, 20, 'Routine sera');


