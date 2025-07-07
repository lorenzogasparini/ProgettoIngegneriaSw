BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS "amministratore" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"nome"	VARCHAR(50) NOT NULL,
	"cognome"	VARCHAR(50) NOT NULL,
	"username"	VARCHAR(50) NOT NULL UNIQUE,
	"password"	VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS "diabetologo" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"username"	VARCHAR(50) NOT NULL UNIQUE,
	"nome"	VARCHAR(50) NOT NULL,
	"cognome"	VARCHAR(50) NOT NULL,
	"password"	VARCHAR(255) NOT NULL,
	"email"	VARCHAR(100) NOT NULL,
	"profile_image_name"	TEXT,
	"deleted" BOOLEAN DEFAULT 0
);

CREATE TABLE IF NOT EXISTS "paziente" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"nome"	VARCHAR(50) NOT NULL,
	"cognome"	VARCHAR(50) NOT NULL,
	"username"	VARCHAR(50) NOT NULL UNIQUE,
	"password"	VARCHAR(255) NOT NULL,
	"email"	VARCHAR(100),
	"id_diabetologo"	INTEGER NOT NULL,
	"data_nascita"	DATE,
	"peso"	REAL,
	"provincia_residenza"	VARCHAR(2),
	"comune_residenza"	VARCHAR(100),
	"note_paziente"	TEXT,
	"profile_image_name"	TEXT,
	"deleted" BOOLEAN DEFAULT 0,
	FOREIGN KEY("id_diabetologo") REFERENCES "diabetologo"("id")
);

CREATE TABLE IF NOT EXISTS "farmaco" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"codice_aic"	VARCHAR(10) NOT NULL UNIQUE,
	"nome"	VARCHAR(1000) NOT NULL
);

CREATE TABLE IF NOT EXISTS "patologia" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"nome"	VARCHAR(1000) NOT NULL,
	"codice_icd"	VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "log" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_paziente"	INTEGER,
	"id_diabetologo"	INTEGER,
	"azione"	TEXT NOT NULL,
	"timestamp"	DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY("id_diabetologo") REFERENCES "diabetologo"("id"),
	FOREIGN KEY("id_paziente") REFERENCES "paziente"("id")
);

CREATE TABLE IF NOT EXISTS "terapia" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_diabetologo"	INTEGER NOT NULL,
	"id_farmaco"	INTEGER NOT NULL,
	"dosi_giornaliere"	INTEGER,
	"quantita_per_dose"	REAL,
	"note"	TEXT,
	FOREIGN KEY("id_diabetologo") REFERENCES "diabetologo"("id") ,
	FOREIGN KEY("id_farmaco") REFERENCES "farmaco"("id")
);

CREATE TABLE IF NOT EXISTS "patologia_paziente" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_paziente"	INTEGER NOT NULL,
	"id_patologia"	INTEGER NOT NULL,
	"id_terapia"	INTEGER,
	"data_diagnosi"	DATE,
	"note_patologia"	TEXT,
	UNIQUE("id_paziente","id_patologia"),
	FOREIGN KEY("id_patologia") REFERENCES "patologia"("id") ON DELETE CASCADE,
	FOREIGN KEY("id_paziente") REFERENCES "paziente"("id") ON DELETE CASCADE,
	FOREIGN KEY("id_terapia") REFERENCES "terapia"("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "rilevazione_farmaco" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_paziente"	INTEGER NOT NULL,
	"id_farmaco"	INTEGER NOT NULL,
	"timestamp"	DATETIME DEFAULT CURRENT_TIMESTAMP,
	"quantita"	REAL,
	"note"	TEXT,
	FOREIGN KEY("id_farmaco") REFERENCES "farmaco"("id"),
	FOREIGN KEY("id_paziente") REFERENCES "paziente"("id")
);

CREATE TABLE IF NOT EXISTS "rilevazione_glicemia" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_paziente"	INTEGER NOT NULL,
	"timestamp"	DATETIME DEFAULT CURRENT_TIMESTAMP,
	"valore"	INTEGER NOT NULL,
	"gravita"	INTEGER CHECK("gravita" >= 0 AND "gravita" <= 3),
	"prima_pasto"	BOOLEAN,
	FOREIGN KEY("id_paziente") REFERENCES "paziente"("id")
);

CREATE TABLE IF NOT EXISTS "rilevazione_sintomo" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_paziente"	INTEGER NOT NULL,
	"timestamp"	DATETIME DEFAULT CURRENT_TIMESTAMP,
	"sintomo"	TEXT NOT NULL,
	"intensita"	INTEGER CHECK("intensita" >= 1 AND "intensita" <= 10),
	FOREIGN KEY("id_paziente") REFERENCES "paziente"("id")
);

CREATE TABLE IF NOT EXISTS "alert" (
	"id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"id_paziente"	INTEGER NOT NULL,
	"id_rilevazione"	INTEGER NOT NULL,
	"tipo_alert"	TEXT NOT NULL CHECK("tipo_alert" IN ('glicemia', 'promemoriaFarmaco', 'farmacoNonAssuntoDa3Giorni')),
	"data_alert"	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	"letto"	BOOLEAN DEFAULT 0,
	FOREIGN KEY("id_paziente") REFERENCES "paziente"("id")
);





-- DB population

INSERT INTO "amministratore" VALUES (1,'Lorenzo','Gasparini','admin1','hashed_pw1');
INSERT INTO "amministratore" VALUES (2,'Andrea','Pellizzari','admin2','hashed_pw2');
INSERT INTO "amministratore" VALUES (3,'Pietro','Corsi','admin3','admin3');

INSERT INTO "diabetologo" VALUES (1,'drrossi','Luca','Rossi','1234','rossi@asl.it','drrossi.png', false);
INSERT INTO "diabetologo" VALUES (2,'drbianchi','Pietro','Bianchi','hashed_pw4','bianchi@asl.it','default.png', false);
INSERT INTO "diabetologo" VALUES (3,'drdestri','Mario','Destri','123456','mario.destri@gmail.it','drdestri.png', false);


INSERT INTO "paziente" VALUES (1,'Mario','Rossi','mario.rossi','1234','mario.rossi@gmail.com',1,'1980-05-12',87.0,'RM','Roma','Segue dieta bilanciata con carboidrati, frutta e verdura','mario.rossi.png', false);
INSERT INTO "paziente" VALUES (2,'Lucia','Verdi','lucia.verdi','1234','lucia.verdi@gmail.com',1,'1975-11-23',67.0,'MI','Milano','Allergica a penicillina','lucia.verdi.png', false);
INSERT INTO "paziente" VALUES (3,'Giulia','Bianchi','giulia.bianchi','hashed_pw7','giulia.bianchi@gmail.com',2,'1990-02-08',72.3,'TO','Torino',NULL,'default.png', false);
INSERT INTO "paziente" VALUES (4,'Andrea','Neri','andrea.neri','hashed_pw8','andrea.neri@gmail.com',2,'2000-07-30',78.0,'FI','Firenze','Sportivo, non fumatore','andrea.neri.png', true);
INSERT INTO "paziente" VALUES (5,'Mattia','Rebonato','rebonato.mattia','1234','mattia.rebonato@gmail.com',1,'2004-05-01',65.0,'VR','Angiari','non assume regolarmente i farmaci e ha spesso problemi!','rebonato.mattia.png', false);


INSERT INTO "farmaco" VALUES (1,'012345678','Metformina');
INSERT INTO "farmaco" VALUES (2,'987654321','Lisinopril');
INSERT INTO "farmaco" VALUES (3,'111222333','Atorvastatina');
INSERT INTO "farmaco" VALUES (4,'444555666','Insulina');


INSERT INTO "patologia" VALUES (1,'Diabete mellito tipo 2','E11');
INSERT INTO "patologia" VALUES (2,'Ipertensione','I10');
INSERT INTO "patologia" VALUES (3,'Dislipidemia','E78');
INSERT INTO "patologia" VALUES (4,'Obesità','E66');
INSERT INTO "patologia" VALUES (5,'Ipertensione secondaria','I15');


INSERT INTO "log" VALUES (25,NULL,1,'UpdateUser','2025-07-03 15:40:49');
INSERT INTO "log" VALUES (26,1,NULL,'UpdateUser','2025-07-03 15:41:35');
INSERT INTO "log" VALUES (27,NULL,3,'UpdateUser','2025-07-03 15:42:11');
INSERT INTO "log" VALUES (28,2,NULL,'SetTerapiaPaziente','2025-07-03 15:42:11');
INSERT INTO "log" VALUES (29,2,NULL,'SetTerapiaPaziente','2025-07-03 15:42:11');
INSERT INTO "log" VALUES (30,5,NULL,'UpdateUser','2025-07-03 15:42:11');
INSERT INTO "log" VALUES (31,1,NULL,'UpdateUser','2025-07-03 15:43:02');
INSERT INTO "log" VALUES (32,NULL,3,'UpdateUser','2025-07-03 15:43:16');
INSERT INTO "log" VALUES (33,2,NULL,'SetTerapiaPaziente','2025-07-03 15:43:16');
INSERT INTO "log" VALUES (34,2,NULL,'SetTerapiaPaziente','2025-07-03 15:43:16');
INSERT INTO "log" VALUES (35,5,NULL,'UpdateUser','2025-07-03 15:43:16');


INSERT INTO "terapia" VALUES (2,1,2,3,10.0,'Alla sera');
INSERT INTO "terapia" VALUES (3,1,3,1,20.0,'Preferibilmente la sera');
INSERT INTO "terapia" VALUES (4,1,4,3,15.0,'Prima dei pasti');
INSERT INTO "terapia" VALUES (16,1,1,2,40.0,'dopo i pasti');
INSERT INTO "terapia" VALUES (17,1,3,1,6.0,'test');
INSERT INTO "terapia" VALUES (19,1,1,1,1.0,'Alla mattina.');
INSERT INTO "terapia" VALUES (20,1,2,3,40.0,'da assumere alle 8.00, 14.00, 20.00 ogni giorno');


INSERT INTO "patologia_paziente" VALUES (1,1,3,2,'2021-06-01','Controlli regolari');
INSERT INTO "patologia_paziente" VALUES (3,2,2,17,'2022-03-12','Necessita insulina');
INSERT INTO "patologia_paziente" VALUES (4,3,3,3,'2021-10-05','Stabile');
INSERT INTO "patologia_paziente" VALUES (5,4,4,4,'2019-12-30',NULL);
INSERT INTO "patologia_paziente" VALUES (7,2,4,16,'2025-05-29','');
INSERT INTO "patologia_paziente" VALUES (9,1,1,19,'2025-06-25','');
INSERT INTO "patologia_paziente" VALUES (10,2,1,20,'2025-06-25','');


INSERT INTO "rilevazione_farmaco" VALUES (1,4,1,'2025-05-28 17:36:20',500.0,'Dopo colazione');
INSERT INTO "rilevazione_farmaco" VALUES (2,3,3,'2025-05-28 17:36:20',500.0,'Dopo cena');
INSERT INTO "rilevazione_farmaco" VALUES (3,2,1,'2025-05-28 17:36:20',15.0,'Insulina serale');
INSERT INTO "rilevazione_farmaco" VALUES (4,3,1,'2025-05-28 17:36:20',20.0,'Routine sera');
INSERT INTO "rilevazione_farmaco" VALUES (5,1,2,'2025-05-29 13:28:45.0',50.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (6,1,1,'2025-06-20 20:10:00.0',500.0,'Farmaco assunto correttamente.');
INSERT INTO "rilevazione_farmaco" VALUES (7,1,2,'2025-06-20 21:03:50.0',50.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (8,1,2,'2025-06-20 21:07:27.0',50.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (9,1,1,'2025-06-20 21:15:43.0',500.0,'Tutte le rilevazioni nella norma.');
INSERT INTO "rilevazione_farmaco" VALUES (10,1,2,'2025-06-20 21:19:17.0',100.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (11,1,2,'2025-06-20 21:21:04.0',50.0,'Tutte le rilevazioni corrette.');
INSERT INTO "rilevazione_farmaco" VALUES (12,1,1,'2025-07-02 16:57:30.0',1.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (13,1,1,'2025-07-02 16:58:03.0',1.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (14,1,2,'2025-07-02 17:45:10.0',10.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (20,1,3,'2025-07-02 18:59:55.0',1.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (21,2,2,'2025-07-02 19:00:11.0',40.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (22,2,3,'2025-07-02 19:00:14.0',6.0,'');
INSERT INTO "rilevazione_farmaco" VALUES (23,1,1,'2025-07-03 17:24:23.0',1.0,'');


INSERT INTO "rilevazione_glicemia" VALUES (1,1,'2025-05-28 17:36:20',145,1,1);
INSERT INTO "rilevazione_glicemia" VALUES (2,1,'2025-05-28 17:36:20',140,0,0);
INSERT INTO "rilevazione_glicemia" VALUES (3,2,'2025-05-28 17:36:20',230,2,0);
INSERT INTO "rilevazione_glicemia" VALUES (4,3,'2025-05-28 17:36:20',60,2,1);
INSERT INTO "rilevazione_glicemia" VALUES (5,4,'2025-05-28 17:36:20',95,0,1);
INSERT INTO "rilevazione_glicemia" VALUES (6,1,'2025-05-29 13:28:45.0',270,3,1);
INSERT INTO "rilevazione_glicemia" VALUES (7,1,'2025-06-20 20:54:25.0',150,0,0);
INSERT INTO "rilevazione_glicemia" VALUES (8,1,'2025-06-20 21:10:49.0',160,0,1);
INSERT INTO "rilevazione_glicemia" VALUES (9,1,'2025-06-20 21:14:58.0',150,0,0);


INSERT INTO "rilevazione_sintomo" VALUES (1,1,'2025-05-28 17:36:20','Mal di testa',3);
INSERT INTO "rilevazione_sintomo" VALUES (2,2,'2025-05-28 17:36:20','Stanchezza',6);
INSERT INTO "rilevazione_sintomo" VALUES (3,3,'2025-05-28 17:36:20','Nausea',2);
INSERT INTO "rilevazione_sintomo" VALUES (4,4,'2025-05-28 17:36:20','Capogiri',5);
INSERT INTO "rilevazione_sintomo" VALUES (5,1,'2025-06-20 20:57:04.0','Mal di testa',2);
INSERT INTO "rilevazione_sintomo" VALUES (6,1,'2025-06-20 21:10:02.0','Mal di pancia',3);
INSERT INTO "rilevazione_sintomo" VALUES (7,1,'2025-07-03 17:24:50.0','mal di testa',3);


INSERT INTO "alert" VALUES (1,2,3,'glicemia','2025-05-17 08:00:00',0);
INSERT INTO "alert" VALUES (2,1,2,'glicemia','2025-05-16 18:30:00',1);
INSERT INTO "alert" VALUES (3,1,2,'promemoriaFarmaco','2025-05-15 21:00:00',1);
INSERT INTO "alert" VALUES (4,2,3,'farmacoNonAssuntoDa3Giorni','2025-05-16 22:00:00',1);
INSERT INTO "alert" VALUES (5,4,5,'glicemia','2025-05-17 07:00:00',0);
INSERT INTO "alert" VALUES (6,3,4,'promemoriaFarmaco','2025-05-16 08:00:00',0);
INSERT INTO "alert" VALUES (7,1,6,'glicemia','2025-05-29 13:28:45.924121088',1);
INSERT INTO "alert" VALUES (14,4,1,'farmacoNonAssuntoDa3Giorni','2025-06-22 12:02:25.0',1);
INSERT INTO "alert" VALUES (16,3,2,'farmacoNonAssuntoDa3Giorni','2025-06-22 12:03:03.0',0);
INSERT INTO "alert" VALUES (17,1,9,'farmacoNonAssuntoDa3Giorni','2025-06-24 18:42:00.0',1);
INSERT INTO "alert" VALUES (18,1,11,'farmacoNonAssuntoDa3Giorni','2025-06-24 18:42:00.0',0);


COMMIT;