package com.progettoingegneriasw.view.components;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoDAO;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import javax.swing.*;

public class NavBar extends HBox {
    private boolean isAuthenticated;
    private String username;

    //  Creazione icone per i bottoni
    // todo: imgBack è uguale all'icona di Logout
    private Image imgBack = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/backIcon.png");
    private Image imgDashboard = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/dashboardIcon.png");
    private Image imgProfile = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/profileIcon.png");
    private Image imgAlert = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/alertIcon.png");
    private Image imgLogout = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/logoutIcon.png");
    private Image imgLogin = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/loginIcon.png");

    private Button backbutton;
    private Button dashboardBtn;
    private Button profileBtn;
    private Button logoutBtn;
    private Button alertBtn;
    private Button userBtn;

    public NavBar() {
        this(false, null);
    }
    
    public NavBar(boolean isAuthenticated, String username) {
        this.isAuthenticated = isAuthenticated;
        this.username = username;
        initialize();
    }
    
    /**
     * Initialize the navigation bar
     */
    private void initialize() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #2a9d8f;");
        this.setAlignment(javafx.geometry.Pos.CENTER);

        // Create buttons based on authentication status
        if (isAuthenticated) {
            createAuthenticatedNavButtons();
        } else {
            createUnauthenticatedNavButtons();
        }
    }
    
    /**
     * Create navigation buttons for authenticated users
     */
    private void createAuthenticatedNavButtons() {

        backbutton = createNavButton("Back", e -> ViewNavigator.navigateBack(), imgBack);
        dashboardBtn = createNavButton("Dashboard", e -> ViewNavigator.navigateToDashboard(), imgDashboard);
        profileBtn = createNavButton("Profile", e -> ViewNavigator.navigateToProfile(), imgProfile);
        logoutBtn = createNavButton("Logout", e -> ViewNavigator.logout(), imgLogout);
        alertBtn = createNavButton("Alerts", e -> ViewNavigator.navigateToAlerts(), imgAlert);
        startAlertPolling(); // Start checking alerts every 2 seconds
        userBtn = createNavButton(ViewNavigator.getAuthenticatedUsername(), e -> ViewNavigator.navigateToProfile(), imgProfile);

        // Spacer per spingere userBtn a destra
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if(ViewNavigator.getAuthenticatedUser().isPaziente()|| ViewNavigator.getAuthenticatedUser().isMedico())
            this.getChildren().addAll(backbutton, dashboardBtn, spacer, userBtn, alertBtn, logoutBtn);
        else
            this.getChildren().addAll(backbutton, dashboardBtn, spacer, userBtn, logoutBtn);
    }
    
    /**
     * Create navigation buttons for unauthenticated users
     */
    private void createUnauthenticatedNavButtons() {
        Button homeBtn = createNavButton("Home", e -> ViewNavigator.navigateToHome(), imgDashboard);
        Button loginBtn = createNavButton("Login", e -> ViewNavigator.navigateToLogin(), imgLogin);

        this.getChildren().addAll(homeBtn, loginBtn/*, registerBtn*/);
    }
    
    /**
     * Create a styled navigation button
     */
    private Button createNavButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler, Image icon) {
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(24);
        iconView.setFitWidth(24);
        iconView.setPreserveRatio(true);

        if (icon.isError()) {
            System.out.println("Errore nel caricamento dell'immagine: " + icon.getException());
        }

        Button button = new Button(text, iconView);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;");
        button.setOnAction(handler);
        
        // Hover effect
        button.setOnMouseEntered(e ->
            button.setStyle("-fx-background-color: #21867a; -fx-border-width: 1; -fx-background-radius: 50 50 50 50; -fx-text-fill: white; -fx-cursor: hand;"));
        button.setOnMouseExited(e ->
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;"));

        return button;
    }
    
    /**
     * Update the navigation bar based on authentication status
     */
    public void updateAuthStatus(boolean isAuthenticated, String username) {
        this.isAuthenticated = isAuthenticated;
        this.username = username;
        this.getChildren().clear();
        initialize();
    }

    ///  every 2 seconds check if countAlerts() > 0, in this case there are alerts so it sets alertBtn to red
    ///  otherwise if countAlerts() == 0 then the alertBtn is standard
    private void startAlertPolling() {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2), event -> {

                    if (!ViewNavigator.isAuthenticated())
                        return;

                    try {
                        int alertCount = 0;

                        if (ViewNavigator.getAuthenticatedUser() instanceof Medico medico){
                            alertCount = MedicoDAO.getInstance().countAlerts();
                        }else if (ViewNavigator.getAuthenticatedUser() instanceof Paziente paziente){
                            alertCount = PazienteDAO.getInstance().countAlerts();
                        }

                        if (alertCount > 0) {
                            alertBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand;");
                            // Set hover effect for red alert state
                            alertBtn.setOnMouseEntered(e ->
                                    alertBtn.setStyle("-fx-background-color: darkred; -fx-text-fill: white; -fx-cursor: hand;"));
                            alertBtn.setOnMouseExited(e ->
                                    alertBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand;"));
                        } else {
                            alertBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;");
                            // Reset hover effect for normal state
                            alertBtn.setOnMouseEntered(e ->
                                    alertBtn.setStyle("-fx-background-color: #21867a; -fx-text-fill: white; -fx-cursor: hand;"));
                            alertBtn.setOnMouseExited(e ->
                                    alertBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }


}