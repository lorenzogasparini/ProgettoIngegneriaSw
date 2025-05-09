package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.Main;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Label statusLabel;
    
    private UserDAO userDAO;
    
    @FXML
    public void initialize() {
        //userDAO = Main.getUserRepository();
        statusLabel.setVisible(false);
    }
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill out all fields");
            return;
        }
        
        if (userDAO.userExists(username)) {
            showError("Username already exists");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        
        // Create and save new user
        //User newUser = User.create(username, password, "nome", "cognome"); // todo: fix with correct datas
        //userDAO.saveUser(newUser);
        
        // Show success message
        showSuccess("Registration successful! Please log in.");
        
        // Clear fields
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
    
    @FXML
    private void handleBackToLogin() {
        ViewNavigator.navigateToLogin();
    }
    
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }
    
    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setVisible(true);
    }
}