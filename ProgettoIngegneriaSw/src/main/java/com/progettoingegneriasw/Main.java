package com.progettoingegneriasw;

import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.Utils.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

public class Main extends Application {
    
    private static UserDAO userDAO = UserDAO.getInstance();
    private static AdminDAO adminDAO = AdminDAO.getInstance();
    private static MedicoDAO medicoDAO = MedicoDAO.getInstance();
    private static PazienteDAO pazienteDAO = PazienteDAO.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main application view

        URL mainViewUrl = getClass().getResource("/com/progettoingegneriasw/fxml/MainView.fxml");
        FXMLLoader loader = new FXMLLoader(mainViewUrl);

        Parent root = loader.load();

        // Set up the scene
        Scene scene = new Scene(root, 1000, 650);
        URL cssUrl = getClass().getResource("/com/progettoingegneriasw/css/styles.css");
        scene.getStylesheets().add(cssUrl.toExternalForm());

        // Configure and show the stage
        primaryStage.setTitle("Progetto Diabete");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // funzionalità spostata in UserDAO.getUserDAO()
//    /**
//     * Get the application-wide user repository
//     */
//    public static UserDAO getUserRepository() {
//        return userDAO;
//    }

    public static void main(String[] args) throws SQLException {
        launch(args);

        // eseguire i test in questa sezione
        System.out.println("---- TEST Model ----");

        // todo: si porebbe parametrizzare il nome dell'utente su cui fare i vari test (esempio: "mario.rossi")

        testUser();
        testAdmin();
        testMedico();
        testPaziente();


    }

    /**
     * metodo per testare le funzionalità generali degli utenti
     */
    private static void testUser(){

        System.out.println("\n\n\n -- TEST USER (general) -- \n");

        // TEST ricerca admin, medico, paziente e utente non trovato
        if(userDAO.userExists("admin1")){
            User adminUser = userDAO.getUser("admin1");
            System.out.println(adminUser.toString());
        }


        if(userDAO.userExists("utenteNonEsistente")){
            User utenteNonEsistente = userDAO.getUser("utenteNonEsistente");
            System.out.println(utenteNonEsistente.toString());
        }

        // TEST INSERIMENTO/MODIFICA utenti (modificare i valori dei campi per poterne inserire di nuovi)
        AdminUser newAdminUser = new AdminUser("admin3", "admin3", "Pietro", "Corsi");
        userDAO.saveUser(newAdminUser);

        // USER: test inserimento log
        System.out.println("test inserimento Log");
        userDAO.setLog(new Log(1, 1, "test Log", Timestamp.from(Instant.now())));

    }

    private static void testAdmin(){

        System.out.println("\n\n\n -- TEST ADMIN -- \n");


        // ADMIN: cancellazione utente
        AdminUser newAdminUser = new AdminUser("admin3", "admin3", "Pietro", "Corsi");
        adminDAO.deleteUser(newAdminUser.getUsername());

        // ADMIN: metodo per ottenere i Log
        System.out.println("Lista Logs:");
        Log[] logs = adminDAO.getLogs();
        for(Log log: logs){
            System.out.println("Log: " + log);
        }

    }

    private static void testMedico() throws SQLException {

        System.out.println("\n\n\n -- TEST MEDICO -- \n");

        // MEDICO: Ottieni tutti i pazienti
        System.out.println("Lista di tutti i pazienti:");
        Paziente[] allPazienti = medicoDAO.getAllPazienti();
        for(Paziente paziente: allPazienti){
            System.out.println("Paziente: " + paziente);
        }

        if(userDAO.userExists("lucia.verdi")){
            User pazienteUser = pazienteDAO.getUser("lucia.verdi");
            System.out.println(pazienteUser.toString());
        }

        MedicoUser newMedicoUser = new MedicoUser("drdestri", "12345", "Mario",
                "Destri", "mario.destri@gmail.it");
        medicoDAO.saveUser(newMedicoUser);

        /*  TEST DAO Query : MedicoDAO */
        Paziente[] pazienti = medicoDAO.getPazientiFromDB("drbianchi");
        System.out.print("\nTest query pazienti curati: dal diabetologo: drbianchi \n");
        for(Paziente paziente: pazienti){
            System.out.println(paziente);
        }

        RilevazioneFarmaco[] rilevazioni_farmaci = medicoDAO.getRilevazioniFarmaci("mario.rossi");
        for(RilevazioneFarmaco rilevazioneFarmaco : rilevazioni_farmaci){
            System.out.println("Rilevazione farmaco: " + rilevazioneFarmaco);
        }

        RilevazioneGlicemia[] rilevazioni_glicemia = medicoDAO.getRilevazioniGlicemia("mario.rossi");
        for(RilevazioneGlicemia rilevazioneGlicemia : rilevazioni_glicemia){
            System.out.println("Rilevazione glicemia: " + rilevazioneGlicemia);
        }

        RilevazioneSintomo[] rilevazioni_sintomi = medicoDAO.getRilevazioniSintomi("mario.rossi");
        for(RilevazioneSintomo rilevazione : rilevazioni_sintomi){
            System.out.println("Rilevazione : " + rilevazione);
        }

        Patologia[] patologie = medicoDAO.getPatologiePaziente("mario.rossi");
        for(Patologia patologia : patologie){
            System.out.println("Patologia : " + patologia);
        }

        Terapia[] terapie = medicoDAO.getTerapiePaziente("mario.rossi");
        for(Terapia ter : terapie){
            System.out.println("Terapia : " + ter);
        }

        // TEST getRilevazioni...
        System.out.println("\n TEST getRilevazioni()...");


        // MEDICO: getRilevazioneSintomo()
        System.out.println("test rilevazionesintomo di qualsiasi paziente");
        RilevazioneSintomo[] rilevazioniSintomi = medicoDAO.getRilevazioneSintomo();
        for(RilevazioneSintomo rilevazioneSintomo : rilevazioniSintomi){
            System.out.println(rilevazioneSintomo);
        }

        System.out.println("test rilevazionesintomo paziente specifico: mario.rossi");
        rilevazioniSintomi = medicoDAO.getRilevazioniSintomi("mario.rossi");
        for(RilevazioneSintomo rilevazioneSintomo : rilevazioniSintomi){
            System.out.println(rilevazioneSintomo);
        }



        // MEDICO: getRilevazioneGlicemia()
        System.out.println("test rilevazioneglicemia di qualsiasi paziente");
        RilevazioneGlicemia[] rilevazioniGlicemia = medicoDAO.getRilevazioneGlicemia();
        for(RilevazioneGlicemia rilevazioneGlicemia : rilevazioniGlicemia){
            System.out.println(rilevazioneGlicemia);
        }
        System.out.println("test rilevazioneglicemia paziente specifico: mario.rossi");
        rilevazioniGlicemia = medicoDAO.getRilevazioniGlicemia("mario.rossi");
        for(RilevazioneGlicemia rilevazioneGlicemia : rilevazioniGlicemia){
            System.out.println(rilevazioneGlicemia);
        }



        // MEDICO: getRilevazioneFarmaco()
        System.out.println("test rilevazioneFarmaco di qualsiasi paziente");
        RilevazioneFarmaco[] rilevazioniFarmaci = medicoDAO.getRilevazioneFarmaco();
        for(RilevazioneFarmaco rilevazioneFarmaco : rilevazioniFarmaci){
            System.out.println(rilevazioneFarmaco);
        }
        System.out.println("test rilevazioneFarmaco paziente specifico: mario.rossi");
        rilevazioniFarmaci = medicoDAO.getRilevazioneFarmaco("mario.rossi");
        for(RilevazioneFarmaco rilevazioneFarmaco : rilevazioniFarmaci){
            System.out.println(rilevazioneFarmaco);
        }

    }

    private static void testPaziente() throws SQLException {

        System.out.println("\n\n\n -- TEST PAZIENTE -- \n");

        if(pazienteDAO.userExists("drbianchi")){
            User medicoUser = medicoDAO.getUser("drbianchi");
            System.out.println(medicoUser.toString());
        }

        PazienteUser newPazienteUser = new PazienteUser("rebonato.mattia", "1234", "Mattia",
                "Rebonato", "mattia.rebonato@gmail.com", 1,
                Date.valueOf("2004-05-01"), 65.0, "VR",
                "Angiari","non assume regolarmente i farmaci e ha spesso problemi!");
        pazienteDAO.saveUser(newPazienteUser);


        // Ottieni la lista farmaci di un paziente
        System.out.println("Lista dei pazienti di mario.rossi");
        Farmaco[] farmaci = pazienteDAO.getFarmaciPaziente("mario.rossi");
        for(Farmaco farmaco: farmaci){
            System.out.println("Farmaco: " + farmaco);
        }

        // Ottieni la lista farmaci di un paziente e indica anche se ogni farmaco è già stato assunto o no di oggi
        System.out.println("Lista dei pazienti di mario.rossi");
        Map<Farmaco, Boolean> farmaciEAssunzioni = pazienteDAO.getFarmaciPazienteEAssunzioni("mario.rossi");
        for(Farmaco farmaco: farmaciEAssunzioni.keySet()){
            System.out.println("Farmaco: " + farmaco + "; assunto: " + farmaciEAssunzioni.get(farmaco));
        }

        // -- TEST INSERIMENTO RILEVAZIONI --
        // PAZIENTE: inserimento di una rilevazione sintomo --> FUNZIONA!
//        System.out.println("Test inserimento rilevazione Sintomo");
//        RilevazioneSintomo rilevazioneSintomo =
//                new RilevazioneSintomo(1, Timestamp.from(Instant.now()), "Cervicale", 7);
//        pazienteDAO.setRilevazioneSintomo(rilevazioneSintomo);

        // PAZIENTE: inserimento di una rilevazione glicemia --> FUNZIONA!
//        System.out.println("Test inserimento rilevazione Glicemia");
//        RilevazioneGlicemia rilevazioneGlicemia =
//                new RilevazioneGlicemia(1, Timestamp.from(Instant.now()), 270, true);
//        pazienteDAO.setRilevazioneGlicemia(rilevazioneGlicemia);
//
//        // PAZIENTE: inserimento di una rilevazione farmaco
//        System.out.println("Test inserimento rilevazione Farmaco");
//        RilevazioneFarmaco rilevazioneFarmaco =
//                new RilevazioneFarmaco(1, 2, Timestamp.from(Instant.now()), 50, "");
//        pazienteDAO.setRilevazioneFarmaco(rilevazioneFarmaco);

    }



}