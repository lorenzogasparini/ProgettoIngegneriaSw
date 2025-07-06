package com.progettoingegneriasw.controller;

import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Admin.AdminDAO;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.sql.SQLException;

public class GestioneUtentiController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, String> username;
    @FXML private TableColumn<User, String> nome;
    @FXML private TableColumn<User, String> cognome;
    @FXML private TableColumn<User, String> email;

    @FXML private VBox VBoxContatta;

    @FXML private VBox VBoxDestinatario;
    @FXML private ImageView profileImage;
    @FXML private Button deleteButton;
    @FXML private User selectedUser;
    @FXML private TextField emailDestinatario;
    @FXML private VBox VBoxOggetto;
    @FXML private TextField emailOggetto;

    @FXML private VBox VBoxCorpo;
    @FXML private TextArea emailCorpo;

    @FXML private Button contactButton;

    private UserDAO userDAO = UserDAO.getInstance();

    public void initialize() throws SQLException {
        setupDeleteButton();
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
        VBoxContatta.setVisible(true);
        VBoxContatta.setManaged(true);

        VBoxOggetto.setVisible(true);
        emailOggetto.setVisible(true);

        VBoxCorpo.setVisible(true);
        emailCorpo.setVisible(true);

        VBoxDestinatario.setVisible(true);
        emailDestinatario.setVisible(true);
    }

    private void setupTableSelection() {
        tableView.setOnMouseClicked(event -> {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                this.selectedUser = selectedUser;
                setupHandleUserPanel(); // Show communication panel

                if(selectedUser.isMedico()) {
                    MedicoUser medico = (MedicoUser) userDAO.getUser(selectedUser.getUsername());
                    loadProfileImage(medico.getProfileImageName());
                    emailDestinatario.setText(medico.getEmail());
                }
                else if(selectedUser.isPaziente()){
                    PazienteUser paziente = (PazienteUser) userDAO.getUser(selectedUser.getUsername());
                    loadProfileImage(paziente.getProfileImageName());
                    emailDestinatario.setText(paziente.getEmail());
                }
                else{
                    System.out.println("Errore: gli utenti admin NON possono essere gestiti da altri utenti admin");
                }
            }
        });
    }

    @FXML
    private void handleComunication() {
        userDAO.contattaDiabetologo(emailDestinatario.getText(), emailOggetto.getText(), emailCorpo.getText());
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
                    // successAlert.getDialogPane().getStyleClass().add("alert-success"); // todo: capire perché non funziona
                    successAlert.showAndWait();

                    refreshTable();

                } catch (SQLException e) {
                    // Error notification
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Errore");
                    errorAlert.setHeaderText("Errore durante l'eliminazione");
                    errorAlert.setContentText("Impossibile eliminare l'utente. Riprovare.");
                    // errorAlert.getDialogPane().getStyleClass().add("alert-danger"); // todo: capire perché non funziona
                    errorAlert.showAndWait();

                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshTable() throws SQLException {
        UserDAO userDAO = UserDAO.getInstance();
        User[] user = userDAO.getAllUsers();

        ObservableList<User> users = FXCollections.observableArrayList(user);

        tableView.setItems(users);
    }


    private void loadProfileImage(String profileImageName){
        String imagePath = AppConfig.IMAGE_DIR + profileImageName;

        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                profileImage.setImage(new Image(file.toURI().toString()));
            }
        }
    }
}
