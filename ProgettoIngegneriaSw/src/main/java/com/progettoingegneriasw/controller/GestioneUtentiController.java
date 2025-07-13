package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Medico.Medico;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.model.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.UnaryOperator;

public class GestioneUtentiController {

    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, String> username;
    @FXML private TableColumn<User, String> nome;
    @FXML private TableColumn<User, String> cognome;
    @FXML private TableColumn<User, String> email;

    @FXML private ScrollPane ScrollPaneGestioneUtente;
    @FXML private VBox VBoxGestioneUtente;
    @FXML private VBox VBoxDestinatario;
    @FXML private ImageView profileImage;
    @FXML private Button deleteButton;
    @FXML private User selectedUser;
    @FXML private TextField emailDestinatario;

    @FXML private VBox updateUserBox;
    @FXML private VBox medicoFieldsBox;
    @FXML private VBox pazienteFieldsBox;


    @FXML private TextField updateNomeField;
    @FXML private TextField updateCognomeField;
    @FXML private TextField updatePasswordField;
    @FXML private TextField updateEmailMedicoField;
    @FXML private TextField updateEmailPazienteField;
    @FXML private ComboBox<Medico> updateDiabetologoComboBox;
    @FXML private DatePicker updateDataNascitaPicker;
    @FXML private TextField updatePesoField;
    @FXML private TextField updateProvinciaField;
    @FXML private TextField updateComuneField;
    @FXML private TextArea updateNotePazienteField;


    private final UserDAO userDAO = UserDAO.getInstance();

    public void initialize() throws SQLException {
        setupDeleteButton();
        setupDatePicker();

        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        nome.setCellValueFactory(new PropertyValueFactory<User, String>("nome"));
        cognome.setCellValueFactory(new PropertyValueFactory<User, String>("cognome"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        setupTableSelection();
        refreshTable();
    }

    private void setupDeleteButton(){
        Image image = new Image("file:" + AppConfig.ICON_DIR + "buttonIcons/deleteIcon.png");
        ImageView icon = new ImageView(image);
        icon.setFitWidth(16);
        icon.setFitHeight(16);
        deleteButton.setGraphic(icon);
        deleteButton.setContentDisplay(ContentDisplay.LEFT);
    }

    @FXML
    private void setupHandleUserPanel() {

        ScrollPaneGestioneUtente.setVisible(true);
        ScrollPaneGestioneUtente.setManaged(true);

        VBoxGestioneUtente.setVisible(true);
        VBoxGestioneUtente.setManaged(true);

        VBoxDestinatario.setVisible(true);
        VBoxDestinatario.setManaged(true);

        emailDestinatario.setVisible(true);
        emailDestinatario.setManaged(true);
    }


    private void setupTableSelection() {
        tableView.setOnMouseClicked(event -> {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                this.selectedUser = selectedUser;
                setupHandleUserPanel(); // Show communication panel

                updateUserBox.setVisible(true);
                updateUserBox.setManaged(true);

                updateNomeField.setText(selectedUser.getNome());
                updateCognomeField.setText(selectedUser.getCognome());
                updatePasswordField.setText(selectedUser.getPassword());


                // Hide both by default
                medicoFieldsBox.setVisible(false);
                medicoFieldsBox.setManaged(false);
                pazienteFieldsBox.setVisible(false);
                pazienteFieldsBox.setManaged(false);

                if (selectedUser.isMedico()) {
                    MedicoUser medico = (MedicoUser) userDAO.getUser(selectedUser.getUsername());
                    loadProfileImage(medico.getProfileImageName());
                    emailDestinatario.setText(medico.getEmail());

                    medicoFieldsBox.setVisible(true);
                    medicoFieldsBox.setManaged(true);
                    updateEmailMedicoField.setText(medico.getEmail());
                }
                else if (selectedUser.isPaziente()) {
                    PazienteUser paziente = (PazienteUser) userDAO.getUser(selectedUser.getUsername());
                    loadProfileImage(paziente.getProfileImageName());
                    emailDestinatario.setText(paziente.getEmail());

                    pazienteFieldsBox.setVisible(true);
                    pazienteFieldsBox.setManaged(true);
                    updateEmailPazienteField.setText(paziente.getEmail());
                    updateDataNascitaPicker.setValue(paziente.getDataNascita().toLocalDate());
                    updateNotePazienteField.setText(paziente.getNotePaziente());
                    updatePesoField.setText(String.valueOf(paziente.getPeso()));
                    setupNumericField(updatePesoField);
                    updateProvinciaField.setText(paziente.getProvinciaResidenza());
                    updateComuneField.setText(paziente.getComuneResidenza());
                    updateDiabetologoComboBox.setValue((Medico) UserDAO.getInstance().getUser(paziente.getIdMedico(), UserType.Medico));                }
            }
        });
        ObservableList<Medico> diabetologi = FXCollections.observableArrayList(AdminDAO.getInstance().getAllMedici());
        updateDiabetologoComboBox.setItems(diabetologi);


    }

    private void setupNumericField(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*(\\.\\d*)?") ? change : null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }

    private void setupDatePicker(){
        updateDataNascitaPicker.setConverter(new StringConverter<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? date.format(formatter) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return LocalDate.parse(string, formatter);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });

        // prevent future dates
        updateDataNascitaPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

    }


    @FXML
    private void handleDeleteUtente() {
        if (selectedUser == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Conferma eliminazione");
        confirm.setHeaderText("Vuoi davvero eliminare questo utente?");

        ButtonType okButton = new ButtonType("Elimina", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(okButton, cancelButton);

        confirm.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                try {
                    AdminDAO.getInstance().deleteUser(selectedUser.getUsername());

                    // Success notification
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Successo");
                    successAlert.setHeaderText("Utente eliminato con successo");
                    successAlert.setContentText("L'utente " + selectedUser.getUsername() + " è stato eliminato.");
                    successAlert.showAndWait();

                    refreshTable();

                } catch (SQLException e) {
                    // Error notification
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Errore");
                    errorAlert.setHeaderText("Errore durante l'eliminazione");
                    errorAlert.setContentText("Impossibile eliminare l'utente. Riprovare.");
                    errorAlert.showAndWait();

                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleUpdateUser() {
        if (selectedUser == null) return;

        String nome = updateNomeField.getText();
        String cognome = updateCognomeField.getText();
        String password = updatePasswordField.getText();


        if (selectedUser.isMedico()) {
            Medico selectedMedico = (MedicoUser) userDAO.getUser(selectedUser.getUsername());

            if (!isValidEmail(updateEmailMedicoField.getText())) {
                showAlert("Errore", "Email paziente non valida.");
                return;
            }

            User selectedMedicoUpdated = new MedicoUser(selectedMedico.getId(), selectedMedico.getUsername(), password, nome, cognome,
                    updateEmailMedicoField.getText(), selectedMedico.getProfileImageName());
            userDAO.saveUser(selectedMedicoUpdated);
        }
        else if (selectedUser.isPaziente()) {
            PazienteUser selectedPaziente = (PazienteUser) userDAO.getUser(selectedUser.getUsername());

            double peso;
            try {
                peso = Double.parseDouble(updatePesoField.getText());
            } catch (NumberFormatException e) {
                showAlert("Errore", "Il peso non è un numero valido.");
                return;
            }

            if (!isValidEmail(updateEmailPazienteField.getText())) {
                showAlert("Errore", "Email paziente non valida.");
                return;
            }

            Medico selectedMedico = updateDiabetologoComboBox.getValue();
            if (selectedMedico == null) {
                showAlert("Errore", "Seleziona un medico di riferimento.");
                return;
            }

            if (updateDataNascitaPicker.getValue() == null) {
                showAlert("Errore", "Seleziona una data di nascita valida.");
                return;
            }

            User updatedPaziente = new PazienteUser(
                    selectedPaziente.getId(),
                    selectedPaziente.getUsername(),
                    password,
                    nome,
                    cognome,
                    updateEmailPazienteField.getText(),
                    selectedMedico.getId(),
                    java.sql.Date.valueOf(updateDataNascitaPicker.getValue()),
                    peso,
                    updateProvinciaField.getText(),
                    updateComuneField.getText(),
                    updateNotePazienteField.getText(),
                    selectedPaziente.getProfileImageName()
            );

            userDAO.saveUser(updatedPaziente);
        }

        showAlert("Successo", "Dati aggiornati con successo.");
        try {
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void refreshTable() throws SQLException {
        UserDAO userDAO = UserDAO.getInstance();
        User[] user = userDAO.getAllUsers();

        ObservableList<User> users = FXCollections.observableArrayList(user);

        tableView.setItems(users);
    }


    private void loadProfileImage(String profileImageName){
        String imagePath = AppConfig.IMAGE_DIR + profileImageName;

        if (!imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                profileImage.setImage(new Image(file.toURI().toString()));
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$");
    }

}
