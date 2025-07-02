package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.UserTypes;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Controller for the profile view.
 * Handles displaying user information and updating the user's password.
 */
public class ProfileController {

    @FXML private ImageView profileImage;
    @FXML private Label usernameLabel;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;
    
    private UserDAO userDAO;
    private String currentUsername;
    
    /**
     * Initialize the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        userDAO = UserDAO.getLoggedUserDAO();
        currentUsername = ViewNavigator.getAuthenticatedUsername();

        loadProfileImage(currentUsername);
        // Display the current username
        usernameLabel.setText(currentUsername);
        
        // Hide the status label initially
        statusLabel.setVisible(false);
    }
    
    /**
     * Handle updating the user's password.
     * This method is called when the user clicks the "Update Password" button.
     */
    @FXML
    private void handleUpdatePassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Riempi entrambi i campi per la nuova password");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showError("Le password non corrispondono");
            return;
        }

        try{
            // Update the user with the new password
            User currentUser = userDAO.getUser(currentUsername);
            switch (currentUser.getUserType()){
                case UserTypes.Admin -> userDAO.saveUser(new AdminUser((AdminUser) currentUser, newPassword));
                case UserTypes.Medico -> userDAO.saveUser(new MedicoUser((MedicoUser) currentUser, newPassword));
                case UserTypes.Paziente -> userDAO.saveUser(new PazienteUser((PazienteUser) currentUser, newPassword));
            }

            showSuccess("Password aggiornata! ");
        }catch (Exception e){
            showError("Errore nell'aggiornamento");
        }

        
        // Clear fields
        newPasswordField.clear();
        confirmPasswordField.clear();
    }
    
    /**
     * Handle navigating back to the dashboard.
     * This method is called when the user clicks the "Back to Dashboard" button.
     */
    @FXML
    private void handleBackToDashboard() {
        ViewNavigator.navigateToDashboard();
    }

    @FXML
    private void handleCambiaImmagine() {
        String currentUsername = ViewNavigator.getAuthenticatedUsername();
        User currentUser = UserDAO.getInstance().getUser(currentUsername);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona immagine profilo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(profileImage.getScene().getWindow());
        if (selectedFile != null) {
            try {
                /*
                // Crea cartella immagini locale se non esiste
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                */
                File destDir = new File(AppConfig.IMAGE_DIR);

                // Copia l'immagine con un nome unico --> todo: da capiare se è necessario
                String fileName = selectedFile.getName();
                File destFile = new File(destDir, fileName);
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if(currentUser instanceof Medico m){
                    // update medico profile image
                    m.setProfileImagePath(destFile.getPath());
                }else if(currentUser instanceof Paziente p){
                    // update paziente profile image
                    p.setProfileImagePath(destFile.getPath());
                }

                // Salva nel DB
                UserDAO.getInstance().saveUser(currentUser); // aggiorna anche il path

                // Mostra l'immagine aggiornata
                profileImage.setImage(new Image(destFile.toURI().toString()));
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Errore nel caricamento immagine.").showAndWait();
            }
        }
    }


    /**
     * Show an error message in the status label.
     * 
     * @param message The error message to display
     */
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }
    
    /**
     * Show a success message in the status label.
     * 
     * @param message The success message to display
     */
    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setVisible(true);
    }

    private void loadProfileImage(String currentUsername){
        String imagePath = "";
        User currentUser = UserDAO.getInstance().getUser(currentUsername);

        switch (currentUser.getUserType()){
            case UserTypes.Medico -> {
                if (currentUser instanceof Medico m)
                    imagePath = m.getProfileImagePath();
            }
            case UserTypes.Paziente -> {
                if (currentUser instanceof Paziente p){
                    imagePath = p.getProfileImagePath();
                }
            }
        }

        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                profileImage.setImage(new Image(file.toURI().toString()));
            }
        }
    }


}