package com.progettoingegneriasw.view.components;

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
    private Image imgBack = new Image("file:" + System.getProperty("user.dir") + "/src/main/java/com/progettoingegneriasw/view/components/buttonIcons/backIcon.png");
    private Image imgDashboard = new Image("file:" + System.getProperty("user.dir") + "/src/main/java/com/progettoingegneriasw/view/components/buttonIcons/dashboardIcon.png");
    private Image imgProfile = new Image("file:" + System.getProperty("user.dir") + "/src/main/java/com/progettoingegneriasw/view/components/buttonIcons/profileIcon.png");
    private Image imgLogout = new Image("file:" + System.getProperty("user.dir") + "/src/main/java/com/progettoingegneriasw/view/components/buttonIcons/logoutIcon.png");
    private Image imgLogin = new Image("file:" + System.getProperty("user.dir") + "/src/main/java/com/progettoingegneriasw/view/components/buttonIcons/loginIcon.png");

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

        //  Label brandLabel = new Label("Dash App");
        //  brandLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");
        //  this.getChildren().add(brandLabel);
        
        // Spacer
        /*
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);
        */

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
        /*
        // Crea il bottone con testo + icona
        Button btn = new Button("Profile", icon);
        btn.setGraphicTextGap(5); // spazio tra testo e icona
        */


        //  Button homeBtn = createNavButton("Home", e -> ViewNavigator.navigateToHome());
        Button backbutton = createNavButton("Back", e -> ViewNavigator.navigateBack(), imgBack);
        Button dashboardBtn = createNavButton("Dashboard", e -> ViewNavigator.navigateToDashboard(), imgDashboard);
        Button profileBtn = createNavButton("Profile", e -> ViewNavigator.navigateToProfile(), imgProfile);
        
        //  Label userLabel = new Label("Hello, " + username);
        //  userLabel.setStyle("-fx-text-fill: white;");
        
        Button logoutBtn = createNavButton("Logout", e -> ViewNavigator.logout(), imgLogout);

        Button userBtn = createNavButton(ViewNavigator.getAuthenticatedUsername(), e -> ViewNavigator.navigateToProfile(), imgProfile);

        // Spacer per spingere userBtn a destra
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(backbutton, dashboardBtn, profileBtn, logoutBtn, spacer, userBtn);
    }
    
    /**
     * Create navigation buttons for unauthenticated users
     */
    private void createUnauthenticatedNavButtons() {
        Button homeBtn = createNavButton("Home", e -> ViewNavigator.navigateToHome(), imgDashboard);
        Button loginBtn = createNavButton("Login", e -> ViewNavigator.navigateToLogin(), imgLogin);
        //  Button registerBtn = createNavButton("Register", e -> ViewNavigator.navigateToRegister());

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
        } else {
            System.out.println("Immagine caricata correttamente");
        }

        Button button = new Button(text, iconView);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;");
        button.setOnAction(handler);
        
        // Hover effect
        button.setOnMouseEntered(e ->
            button.setStyle("-fx-background-color: #21867a; -fx-border-width: 1; -fx-background-radius: 50 50 50 50; -fx-text-fill: white; -fx-cursor: hand;"));
        button.setOnMouseExited(e ->
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;"));

        /*
        button.setOnMouseClicked(e ->
            button.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #21867a; -fx-border-width: 1; -fx-background-radius: 20 20 0 0; -fx-padding: 8;"));
        */

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
}