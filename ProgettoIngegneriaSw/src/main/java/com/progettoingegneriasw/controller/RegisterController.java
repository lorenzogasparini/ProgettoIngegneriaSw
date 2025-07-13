package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Admin.Admin;
import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Admin.AdminUser;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.time.LocalDate;

public class RegisterController {
    @FXML private TextField usernameField;
    
    @FXML private PasswordField passwordField;
    
    @FXML private PasswordField confirmPasswordField;
    
    @FXML private Label statusLabel;
    @FXML private RadioButton radioPaziente, radioMedico, radioAdmin;
    private final ToggleGroup userTypeGroup = new ToggleGroup();
    @FXML private VBox pazienteVBox, medicoVBox;

    @FXML private ComboBox<Medico> diabetologoComboBox;
    @FXML private TextField nomeField, cognomeField;
    @FXML private TextField emailMedicoField, emailPazienteField;
    @FXML private TextField pesoField, provinciaField, comuneField;
    @FXML private DatePicker dataNascitaPicker;
    
    private UserDAO userDAO = UserDAO.getInstance();

    @FXML
    public void initialize() {
        statusLabel.setVisible(false);
        radioPaziente.setToggleGroup(userTypeGroup);
        radioMedico.setToggleGroup(userTypeGroup);
        radioAdmin.setToggleGroup(userTypeGroup);

        radioPaziente.setOnAction(e -> updateFieldsVisibility());
        radioMedico.setOnAction(e -> updateFieldsVisibility());
        radioAdmin.setOnAction(e -> updateFieldsVisibility());

        // Carica diabetologi nel ComboBox
        ObservableList<Medico> diabetologi = FXCollections.observableArrayList(AdminDAO.getInstance().getAllMedici());
        diabetologoComboBox.setItems(diabetologi);
        diabetologoComboBox.setPromptText("Seleziona diabetologo");
    }

    private void updateFieldsVisibility() {
        boolean isPaziente = radioPaziente.isSelected();
        boolean isMedico = radioMedico.isSelected();

        pazienteVBox.setVisible(isPaziente);
        pazienteVBox.setManaged(isPaziente);
        medicoVBox.setVisible(isMedico);
        medicoVBox.setManaged(isMedico);

    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (userTypeGroup.getSelectedToggle() == null) {
            showError("Seleziona un tipo di utente da inserire");
            return;
        }

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Compila tutti i campi obbligatori");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Le password non coincidono");
            return;
        }

        if (userDAO.userExists(username)) {
            showError("Username già esistente");
            return;
        }

        String nome = nomeField.getText();
        String cognome = cognomeField.getText();

        if (radioPaziente.isSelected()) {
            // Campi specifici paziente
            String email = emailPazienteField.getText();
            String provincia = provinciaField.getText();
            String comune = comuneField.getText();
            LocalDate dataNascita = dataNascitaPicker.getValue();
            String notePaziente = "";
            String profileImageName = AppConfig.DEFAULT_IMAGE;

            double peso;
            try {
                peso = Double.parseDouble(pesoField.getText());
            } catch (NumberFormatException e) {
                showError("Peso non valido");
                return;
            }

            Medico diabetologo = diabetologoComboBox.getValue();
            if (diabetologo == null) {
                showError("Seleziona un diabetologo di riferimento");
                return;
            }

            Integer idDiabetologo = diabetologo.getId();

            PazienteUser newPaziente = new PazienteUser(
                    username, password, nome, cognome, email,
                    idDiabetologo,
                    Date.valueOf(dataNascita),
                    peso,
                    provincia,
                    comune,
                    notePaziente,
                    profileImageName
            );

            userDAO.saveUser(newPaziente);
        } else if (radioMedico.isSelected()) {
            // Campi specifici medico
            String email = emailMedicoField.getText();
            String profileImageName = AppConfig.DEFAULT_IMAGE;

            MedicoUser newMedico = new MedicoUser(username, password, nome, cognome, email, profileImageName);
            userDAO.saveUser(newMedico);

        } else if (radioAdmin.isSelected()) {
            // Campi admin (nessun campo extra richiesto)
            userDAO.saveUser(new AdminUser(username, password, nome, cognome));
        }

        showSuccess("Utente registrato con successo!");
        clearForm();
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        nomeField.clear();
        cognomeField.clear();
        emailPazienteField.clear();
        emailMedicoField.clear();
        pesoField.clear();
        provinciaField.clear();
        comuneField.clear();
        dataNascitaPicker.setValue(null);
        radioPaziente.setSelected(false);
        radioMedico.setSelected(false);
        radioAdmin.setSelected(false);
        updateFieldsVisibility();
    }


    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        statusLabel.getStyleClass().setAll("status-label", "error");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        statusLabel.getStyleClass().setAll("status-label", "success");
    }

}