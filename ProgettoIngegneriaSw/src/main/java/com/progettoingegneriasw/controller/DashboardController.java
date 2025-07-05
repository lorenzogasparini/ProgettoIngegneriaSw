package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DashboardController {
    @FXML private Label welcomeLabel;

    @FXML private HBox handlingPanelPaziente;
    @FXML private HBox handlingPanelMedico;
    @FXML private HBox handlingPanelAdmin;


    private UserDAO userDAO;
    
    @FXML
    public void initialize() {
        // This is a protected view, so we should always have an authenticated user
        String username = ViewNavigator.getAuthenticatedUsername();
        userDAO = UserDAO.getInstance();
        welcomeLabel.setText("Benvenuto nella tua dashboard, " +
                (userDAO.getUser(ViewNavigator.getAuthenticatedUsername()).isMedico() ? "dott. " : " ") + username + "!");
        showUserTypePanel();

    }

    private void showUserTypePanel(){
        userDAO = UserDAO.getInstance();
        if(userDAO.getUser(ViewNavigator.getAuthenticatedUsername()).isMedico()) {
            handlingPanelPaziente.setManaged(false);
            handlingPanelPaziente.setVisible(false);
            handlingPanelAdmin.setManaged(false);
            handlingPanelAdmin.setVisible(false);
            handlingPanelMedico.setManaged(true);
            handlingPanelMedico.setVisible(true);
        }
        else if(userDAO.getUser(ViewNavigator.getAuthenticatedUsername()).isPaziente()){
            handlingPanelMedico.setManaged(false);
            handlingPanelMedico.setVisible(false);
            handlingPanelAdmin.setManaged(false);
            handlingPanelAdmin.setVisible(false);
            handlingPanelPaziente.setManaged(true);
            handlingPanelPaziente.setVisible(true);
        }
        else if (userDAO.getUser(ViewNavigator.getAuthenticatedUsername()).isAdmin()){
            handlingPanelPaziente.setManaged(false);
            handlingPanelPaziente.setVisible(false);
            handlingPanelMedico.setManaged(false);
            handlingPanelMedico.setVisible(false);
            handlingPanelAdmin.setManaged(true);
            handlingPanelAdmin.setVisible(true);
        }
    }

    @FXML
    private void handleVisualizzaPazienti() {
        ViewNavigator.navigateToTest();
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
        ViewNavigator.navigateToRegister();  // già esistente
    }

    @FXML
    private void handleApriGestioneUtenti() {
        ViewNavigator.navigateToGestioneUtenti(); // da creare
    }

    @FXML
    private void handleApriVisualizzazioneLog() {
        ViewNavigator.navigateToVisualizzazioneLog(); // da creare
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