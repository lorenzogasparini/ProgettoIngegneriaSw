package com.progettoingegneriasw;

import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.UserDAO;
import javafx.application.Application;
import javafx.stage.Stage;

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
        String user = adminDAO.getUser("admin1").toString();
        System.out.println(user);


    }
}