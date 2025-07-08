package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;

public class DashboardController {
    @FXML private Label welcomeLabel;

    @FXML private HBox handlingPanelPaziente;
    @FXML private HBox handlingPanelMedico;
    @FXML private HBox handlingPanelAdmin;

    @FXML private ImageView iconVisualizzaPazienti;
    @FXML private ImageView iconContattaUtente;
    @FXML private ImageView iconAlerts;
    @FXML private ImageView iconGestioneRilevazioni;
    @FXML private ImageView iconGestioneTerapie;
    @FXML private ImageView iconContattaMedico;
    @FXML private ImageView iconRegister;
    @FXML private ImageView iconGestioneUtenti;
    @FXML private ImageView iconVisualizzaLog;

// ... repeat for others



    private UserDAO userDAO;
    
    @FXML
    public void initialize() {
        // This is a protected view, so we should always have an authenticated user
        String username = ViewNavigator.getAuthenticatedUsername();
        userDAO = UserDAO.getInstance();
        welcomeLabel.setText("Benvenuto nella tua dashboard, " +
                (ViewNavigator.getAuthenticatedUser().isMedico() ? "dott. " : " ") + username + "!");
        showUserTypePanel();
        loadDashboardIcons();

    }

    private void showUserTypePanel(){
        userDAO = UserDAO.getInstance();
        if(ViewNavigator.getAuthenticatedUser().isMedico()) {
            handlingPanelPaziente.setManaged(false);
            handlingPanelPaziente.setVisible(false);
            handlingPanelAdmin.setManaged(false);
            handlingPanelAdmin.setVisible(false);
            handlingPanelMedico.setManaged(true);
            handlingPanelMedico.setVisible(true);
        }
        else if(ViewNavigator.getAuthenticatedUser().isPaziente()){
            handlingPanelMedico.setManaged(false);
            handlingPanelMedico.setVisible(false);
            handlingPanelAdmin.setManaged(false);
            handlingPanelAdmin.setVisible(false);
            handlingPanelPaziente.setManaged(true);
            handlingPanelPaziente.setVisible(true);
        }
        else if (ViewNavigator.getAuthenticatedUser().isAdmin()){
            handlingPanelPaziente.setManaged(false);
            handlingPanelPaziente.setVisible(false);
            handlingPanelMedico.setManaged(false);
            handlingPanelMedico.setVisible(false);
            handlingPanelAdmin.setManaged(true);
            handlingPanelAdmin.setVisible(true);
        }
    }

    private void loadDashboardIcons() {
        iconVisualizzaPazienti.setImage(loadImage("visualizzaPazienti_pulsar.png"));

        iconContattaUtente.setImage(loadImage("contattaUtente_pulsar.png"));
        iconAlerts.setImage(loadImage("alerts_pulsar.png"));
        iconGestioneRilevazioni.setImage(loadImage("gestioneRilevazioni_pulsar.png"));
        iconGestioneTerapie.setImage(loadImage("gestioneTerapie_pulsar.png"));
        iconContattaMedico.setImage(loadImage("contattaMedico_pulsar.png"));
        iconRegister.setImage(loadImage("register_pulsar.png"));
        iconGestioneUtenti.setImage(loadImage("gestioneUtenti_pulsar.png"));
        iconVisualizzaLog.setImage(loadImage("log_pulsar.png"));
        // ...

    }

    private Image loadImage(String fileName) {
        String resourcePath = "/icons/dashboardImages/" + fileName;
        var url = getClass().getResource(resourcePath);
        if (url == null) {
            System.err.println("Image not found: " + resourcePath);
            return null;
        }
        return new Image(url.toString());
    }


    @FXML
    private void handleVisualizzaPazienti() {
        ViewNavigator.navigateToPazienti();
    }

    @FXML
    private void handleVisualizzaAlerts() {
        ViewNavigator.navigateToHandleVisualizzaAlerts();
    }

    @FXML
    private void handleContattaUtente() {
        ViewNavigator.navigateToContattaUtente();
    }

    @FXML
    private void handleGestioneRilevazioni() {
        ViewNavigator.navigateToRilevazioniHandling();
    }

    @FXML
    private void handleGestioneTerapie() {ViewNavigator.navigateToTerapie();}

    @FXML
    private void handleApriRegisterView() {
        ViewNavigator.navigateToRegister();
    }

    @FXML
    private void handleApriGestioneUtenti() {
        ViewNavigator.navigateToGestioneUtenti();
    }

    @FXML
    private void handleApriVisualizzazioneLog() {
        ViewNavigator.navigateToVisualizzazioneLog();
    }


    @FXML
    private void handleViewProfile() {
        ViewNavigator.navigateToProfile();
    }
    
    @FXML
    private void handleLogout() {
        ViewNavigator.logout();
    }
}