package com.progettoingegneriasw;

import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

public class Main extends Application {
    
    //private static UserDAO userDAO = new UserDAO();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main application view

//        URL mainViewUrl = getClass().getResource("/com/progettoingegneriasw/fxml/MainView.fxml");
//        FXMLLoader loader = new FXMLLoader(mainViewUrl);
//
//        Parent root = loader.load();
//
//        // Set up the scene
//        Scene scene = new Scene(root, 800, 600);
//        URL cssUrl = getClass().getResource("/com/progettoingegneriasw/css/styles.css");
//        scene.getStylesheets().add(cssUrl.toExternalForm());
//
//        // Configure and show the stage
//        primaryStage.setTitle("Progetto Diabete");
//        primaryStage.setScene(scene);
//        primaryStage.show();

    }

    // funzionalità spostata in UserDAO.getUserDAO()
//    /**
//     * Get the application-wide user repository
//     */
//    public static UserDAO getUserRepository() {
//        return userDAO;
//    }

    public static void main(String[] args) {
        //launch(args); // todo: da decommentare finiti i test su model

        // todo: eseguire i test in quest'area
        System.out.println("---- TEST Model ----");
        AdminDAO adminDAO = AdminDAO.getInstance();
        MedicoDAO medicoDAO = MedicoDAO.getInstance();
        PazienteDAO pazienteDAO = PazienteDAO.getInstance();

        // TEST generale per prendere tutti i pazienti
        adminDAO.printAllPazientiDB();



        // TEST ricerca admin, medico, paziente e utente non trovato
        if(adminDAO.userExists("admin1")){
            User adminUser = adminDAO.getUser("admin1");
            System.out.println(adminUser.toString());
        }

        if(pazienteDAO.userExists("drbianchi")){
            User medicoUser = medicoDAO.getUser("drbianchi");
            System.out.println(medicoUser.toString());
        }

        if(medicoDAO.userExists("lucia.verdi")){
            User pazienteUser = pazienteDAO.getUser("lucia.verdi");
            System.out.println(pazienteUser.toString());
        }

        if(adminDAO.userExists("utenteNonEsistente")){
            User utenteNonEsistente = adminDAO.getUser("utenteNonEsistente");
            System.out.println(utenteNonEsistente.toString());
        }

        // TEST INSERIMENTO/MODIFICA utenti (modificare i valori dei campi per poterne inserire di nuovi)
        AdminUser newAdminUser = new AdminUser("admin3", "admin3", "Pietro", "Corsi");
        adminDAO.saveUser(newAdminUser);

        MedicoUser newMedicoUser = new MedicoUser("drdestri", "1234", "Mario",
                "Destri", "mario.destri@gmail.it");
        medicoDAO.saveUser(newMedicoUser);

        PazienteUser newPazienteUser = new PazienteUser("rebonato.mattia", "1234", "Mattia",
                "Rebonato", "mattia.rebonato@gmail.com", 0,
                Date.valueOf("2004-05-01"), 70.0, "VR",
                "Angiari","non assume regolarmente i farmaci e ha spesso problemi!");
        pazienteDAO.saveUser(newPazienteUser);


        // TEST CANCELLAZIONE UTENTE
        medicoDAO.deleteUser(newMedicoUser.getUsername());
        pazienteDAO.deleteUser(newPazienteUser.getUsername());
        // test che deve generare un errore:
        adminDAO.deleteUser(newAdminUser.getUsername());


    }
}