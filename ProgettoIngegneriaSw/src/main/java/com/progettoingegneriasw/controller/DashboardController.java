package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javax.swing.text.View;

public class DashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private HBox handlingPanelPaziente;

    @FXML
    private HBox handlingPanelMedico;

    private UserDAO userDAO;
    
    @FXML
    public void initialize() {
        // This is a protected view, so we should always have an authenticated user
        String username = ViewNavigator.getAuthenticatedUser();
        userDAO = UserDAO.getInstance();
        welcomeLabel.setText("Benvenuto nella tua dashboard, " + (userDAO.getUser(ViewNavigator.getAuthenticatedUser()).isMedico() ? "dott. " : " ") + username + "!");

        userDAO = UserDAO.getInstance();
        if(userDAO.getUser(ViewNavigator.getAuthenticatedUser()).isMedico()) {
            handlingPanelPaziente.setManaged(false);
            handlingPanelPaziente.setVisible(false);
            handlingPanelMedico.setManaged(true);
        }
        else if(userDAO.getUser(ViewNavigator.getAuthenticatedUser()).isPaziente()){
            handlingPanelPaziente.setManaged(true);
            handlingPanelMedico.setManaged(false);
            handlingPanelMedico.setVisible(false);
        }
        else{
            handlingPanelPaziente.setManaged(false);
            handlingPanelMedico.setManaged(true);
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
        ViewNavigator.navigateToStats();
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