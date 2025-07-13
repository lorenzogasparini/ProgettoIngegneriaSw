package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.Paziente;
import com.progettoingegneriasw.model.Paziente.PazienteDAO;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.UserType;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Controller for the profile view.
 * Handles displaying user information and updating the user's password.
 */
public class ProfileController {

    @FXML private HBox HBoxUserInfo;
    @FXML private ImageView profileImage;
    private final Image imgDoctor = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/doctorIcon.png");
    @FXML private Button contactDoctorButton;
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

        if(ViewNavigator.getAuthenticatedUser().isAdmin()){
            HBoxUserInfo.setVisible(false);
            HBoxUserInfo.setManaged(false);
        }

        usernameLabel.setText(currentUsername);
        setContactDoctorIcon();
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        loadProfileImageAsync(currentUsername);
    }

    /**
     * Handle updating the user's password.
     * This method is called when the user clicks the "Update Password" button.
     */
    @FXML
    private void handleUpdatePassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Riempi entrambi i campi per la nuova password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Le password non corrispondono");
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    User currentUser = userDAO.getUser(currentUsername);
                    switch (currentUser.getUserType()) {
                        case UserType.Admin -> userDAO.saveUser(new AdminUser((AdminUser) currentUser, newPassword));
                        case UserType.Medico -> userDAO.saveUser(new MedicoUser((MedicoUser) currentUser, newPassword));
                        case UserType.Paziente -> userDAO.saveUser(new PazienteUser((PazienteUser) currentUser, newPassword));
                    }

                    Platform.runLater(() -> {
                        showSuccess("Password aggiornata! ");
                        newPasswordField.clear();
                        confirmPasswordField.clear();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showError("Errore nell'aggiornamento"));
                }
                return null;
            }
        };

        new Thread(task).start();
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona immagine profilo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(profileImage.getScene().getWindow());
        if (selectedFile == null) return;

        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {
                User currentUser = ViewNavigator.getAuthenticatedUser();
                String fileName = selectedFile.getName();

                File destDir = new File(AppConfig.IMAGE_DIR);
                File destFile = new File(destDir, fileName);
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if (currentUser instanceof Medico m)
                    m.setProfileImageName(fileName);
                else if (currentUser instanceof Paziente p)
                    p.setProfileImageName(fileName);

                UserDAO.getInstance().saveUser(currentUser);

                return new Image(destFile.toURI().toString());
            }
        };

        task.setOnSucceeded(e -> profileImage.setImage(task.getValue()));
        task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR, "Errore nel caricamento immagine.").showAndWait());

        new Thread(task).start();
    }



    /**
     * Show an error message in the status label.
     * 
     * @param message The error message to display
     */
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-background-color: #ffe6e6;");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
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
        statusLabel.setManaged(true);
    }

    private void loadProfileImageAsync(String username) {
        Task<Image> task = new Task<>() {
            @Override
            protected Image call() {
                User user = UserDAO.getInstance().getUser(username);
                String imagePath = "";
                if (user instanceof Medico m)
                    imagePath = AppConfig.IMAGE_DIR + m.getProfileImageName();
                else if (user instanceof Paziente p)
                    imagePath = AppConfig.IMAGE_DIR + p.getProfileImageName();

                File file = new File(imagePath);
                if (file.exists()) {
                    return new Image(file.toURI().toString());
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            Image img = task.getValue();
            if (img != null) profileImage.setImage(img);
        });

        new Thread(task).start();
    }

    private void setContactDoctorIcon(){

        if(!ViewNavigator.getAuthenticatedUser().getUserType().equals(UserType.Paziente)){
            contactDoctorButton.setVisible(false);
            contactDoctorButton.setManaged(false);
            return;
        }

        ImageView icon = new ImageView(imgDoctor);
        icon.setFitWidth(16);
        icon.setFitHeight(16);
        contactDoctorButton.setGraphic(icon);
        contactDoctorButton.setContentDisplay(ContentDisplay.LEFT);
    }

    @FXML
    private void handleContattaDiabetologo() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                if(!ViewNavigator.getAuthenticatedUser().getUserType().equals(UserType.Paziente))
                    return null;
                String email = PazienteDAO.getInstance()
                        .getMedicoRiferimento(currentUsername)
                        .getEmail();
                UserDAO.getInstance().contattaDiabetologo(email, "", "");
                return null;
            }
        };
        new Thread(task).start();
    }

}