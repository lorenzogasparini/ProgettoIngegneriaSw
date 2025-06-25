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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

public class Main extends Application {
    
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final AdminDAO adminDAO = AdminDAO.getInstance();
    private static final MedicoDAO medicoDAO = MedicoDAO.getInstance();
    private static final PazienteDAO pazienteDAO = PazienteDAO.getInstance();
    private static FileLock alertLock;
    private static FileChannel alertChannel;

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

        startAlertLockWatcher(1, 5); // update every 1s, retry lock every 5s
        // todo
        // getAlerts();

        // JavaFX launch
        launch(args);

        // eseguire i test in questa sezione
        System.out.println("---- TEST Model ----");

        // todo: si potrebbe parametrizzare il nome dell'utente su cui fare i vari test (esempio: "mario.rossi")


        testUser();
        testAdmin();
        testMedico();
        testPaziente();
        testAlerts();
    }

    public static void startAlertLockWatcher(int updateIntervalSec, int retryIntervalSec) {
        Timeline retryTimeline = new Timeline(new KeyFrame(Duration.seconds(retryIntervalSec), e -> {
            if (alertLock == null || !alertLock.isValid()) {
                if (acquireLockAlertHandler()) {
                    System.out.println("!!! Starting alert updater !!!");
                    Runtime.getRuntime().addShutdownHook(new Thread(Main::releaseLockAlertHandler));
                    startAlertUpdater(updateIntervalSec);
                }
            }
        }));
        retryTimeline.setCycleCount(Animation.INDEFINITE);
        retryTimeline.play();
    }

    public static boolean acquireLockAlertHandler() {
        File file = new File("lockfile.lck");
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            alertChannel = raf.getChannel();
            alertLock = alertChannel.tryLock(); // hold the lock until the app is closed

            return alertLock != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * metodo per aggiornare la tabella Alert:
     * - viene chiamato questo metodo solo da un utente alla volta tramite meccanismo di .lck
     * - viene eseguito ogni SEC secondi
     * @throws SQLException
     */
    public static void startAlertUpdater(int sec) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(sec), e -> {
            try {
                userDAO.updateAlertTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }



    public static void releaseLockAlertHandler(){
        try {
            if (alertLock != null && alertLock.isValid()) {
                alertLock.release();
            }
            if (alertChannel != null && alertChannel.isOpen()) {
                alertChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getAlerts(){
        // todo: getAllAlerts() --> controlla ogni SEC se ci sono alert per l'utente loggato
        // bisogna prima verificare che ci sia un utente loggato
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
        // System.out.println("test inserimento Log");
        // userDAO.setLog(new Log(1, 1, "test Log", Timestamp.from(Instant.now())));

        // USER: test per apertura app di mail
        /*
        pazienteDAO.contattaDiabetologo(
                "diabetologo@example.com",
                "Valori glicemia fuori soglia",
                "Gentile diabetologo,\n\nHo notato valori anomali oggi. Potremmo sentirci?\n\nCordiali saluti."
        );
        */


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

        /* todo: cancellare*/
        RilevazioneFarmaco[] rilevazioni_farmaci = medicoDAO.getRilevazioniFarmaco("mario.rossi");
        for(RilevazioneFarmaco rilevazioneFarmaco : rilevazioni_farmaci){
            System.out.println("Rilevazione farmaco: " + rilevazioneFarmaco);
        }

        RilevazioneGlicemia[] rilevazioni_glicemia = medicoDAO.getRilevazioniGlicemia("mario.rossi");
        for(RilevazioneGlicemia rilevazioneGlicemia : rilevazioni_glicemia){
            System.out.println("Rilevazione glicemia: " + rilevazioneGlicemia);
        }

        RilevazioneSintomo[] rilevazioni_sintomi = medicoDAO.getRilevazioniSintomo("mario.rossi");
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
        RilevazioneSintomo[] rilevazioniSintomi = medicoDAO.getRilevazioniSintomo();
        for(RilevazioneSintomo rilevazioneSintomo : rilevazioniSintomi){
            System.out.println(rilevazioneSintomo);
        }

        /* todo: cancellare
        System.out.println("test rilevazionesintomo paziente specifico: mario.rossi");
        rilevazioniSintomi = medicoDAO.getRilevazioniSintomi("mario.rossi");
        for(RilevazioneSintomo rilevazioneSintomo : rilevazioniSintomi){
            System.out.println(rilevazioneSintomo);
        }
         */


        /* todo: cancellare
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
         */



        // MEDICO: getRilevazioneFarmaco()
        System.out.println("test rilevazioneFarmaco di qualsiasi paziente");
        RilevazioneFarmaco[] rilevazioniFarmaci = medicoDAO.getRilevazioniFarmaco();
        for(RilevazioneFarmaco rilevazioneFarmaco : rilevazioniFarmaci){
            System.out.println(rilevazioneFarmaco);
        }
        System.out.println("test rilevazioneFarmaco paziente specifico: mario.rossi");
        rilevazioniFarmaci = medicoDAO.getRilevazioniFarmaco("mario.rossi");
        for(RilevazioneFarmaco rilevazioneFarmaco : rilevazioniFarmaci){
            System.out.println(rilevazioneFarmaco);
        }

        // MEDICO: setPatologiaPaziente() --> ok!
        //medicoDAO.setPatologiaPaziente(new Patologia(4), "lucia.verdi", null,
         //       Date.valueOf("2025-05-29"), "");


        // MEDICO: getPatologie() con o senza nome patologia --> ok!
        System.out.println("\nLista di tutte le patologie: ");
        patologie = medicoDAO.getPatologie();
        for (Patologia patologia: patologie){
            System.out.println(patologia);
        }

        System.out.println("\nLista di tutte le patologie che contengono la parola 'Ipertensione'");
        patologie = medicoDAO.getPatologie("Ipertensione");
        for (Patologia patologia: patologie){
            System.out.println(patologia);
        }

        // MEDICO:  addPatologia() --> ok!
        //System.out.println("\n inserimento di una patologia");
        //medicoDAO.addPatologia(new Patologia("Retinopatia diabetica", "E53"));


        // MEDICO: setTerapiaPaziente() --> ok!
        System.out.println("\ninserimento terapia ad un paziente");
        medicoDAO.setTerapiaPaziente(new Terapia(
                new Farmaco(1,"012345678", "Metformina"), 3, 10.0, "pasti"),
                "lucia.verdi", new Patologia(4), "");

        System.out.println("\nmodifica terapia già presente");
        medicoDAO.setTerapiaPaziente(new Terapia(
                        new Farmaco(1,"012345678", "Metformina"), 2, 40.0, "dopo i pasti"),
                "lucia.verdi", new Patologia(4), "");

    }

    private static void testAlerts() throws SQLException {
        System.out.println("\n\n\n -- TEST ALERTS -- \n");

        // Ottieni la lista di alerts di un paziente specifico
        System.out.println("Lista degli alerts di mario.rossi");
        Alert[] alerts = medicoDAO.getAlertPaziente("mario.rossi");
        for(Alert al: alerts){
            System.out.println("Alert: " + al);
        }

        // Ottieni la lista di alerts di tutti i pazienti
        System.out.println("\n\nLista degli alerts generale");
        Alert[] alerts2 = medicoDAO.getAlert();
        for(Alert al: alerts2){
            System.out.println("Alert: " + al);
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

        // PAZIENTE: getMedicoRiferimento
        System.out.println("Medico di riferimento del paziente mario.rossi: " + pazienteDAO.getMedicoRiferimento("mario.rossi"));


    }

}