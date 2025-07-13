package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class HomeController {
    @FXML private Hyperlink loginButton;
    
    @FXML
    public void initialize() {
        // If we're already authenticated, hide login/register buttons
        if (ViewNavigator.isAuthenticated()) {
            loginButton.setVisible(false);
        }
    }
    
    @FXML
    private void handleLogin() {
        ViewNavigator.navigateToLogin();
    }

}