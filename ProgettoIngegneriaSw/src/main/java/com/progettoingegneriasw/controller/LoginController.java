package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.Main;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label statusLabel;
    
    private UserDAO userDAO;
    
    @FXML
    public void initialize() {
        userDAO = Main.getUserRepository();
        statusLabel.setVisible(false);
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }
        
        User user = userDAO.getUser(username);
        if (user != null && user.checkPassword(password)) {
            // Login successful
            ViewNavigator.setAuthenticatedUser(username);
            ViewNavigator.navigateToDashboard();
        } else {
            showError("Invalid username or password");
        }
    }
    
    @FXML
    private void handleRegister() {
        ViewNavigator.navigateToRegister();
    }
    
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }
}