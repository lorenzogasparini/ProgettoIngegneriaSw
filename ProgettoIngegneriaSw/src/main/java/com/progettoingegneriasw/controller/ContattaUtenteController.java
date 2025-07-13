package com.progettoingegneriasw.controller;
import com.progettoingegneriasw.config.AppConfig;
import com.progettoingegneriasw.model.Medico.MedicoUser;
import com.progettoingegneriasw.model.Paziente.PazienteUser;
import com.progettoingegneriasw.model.User;
import com.progettoingegneriasw.model.UserDAO;
import com.progettoingegneriasw.view.ViewNavigator;
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

public class ContattaUtenteController {
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
    @FXML private TextField emailDestinatario;
    @FXML private VBox VBoxOggetto;
    @FXML private TextField emailOggetto;

    @FXML private VBox VBoxCorpo;
    @FXML private TextArea emailCorpo;

    @FXML private Button contactButton;

    private UserDAO userDAO = UserDAO.getInstance();

    public void initialize() throws SQLException {
        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        nome.setCellValueFactory(new PropertyValueFactory<User, String>("nome"));
        cognome.setCellValueFactory(new PropertyValueFactory<User, String>("cognome"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        setupTableSelection();
        refreshTable();
    }

    @FXML
    private void setupComunication() {
        VBoxContatta.setVisible(true);
        VBoxContatta.setManaged(true);

        VBoxOggetto.setVisible(true);
        emailOggetto.setVisible(true);

        VBoxCorpo.setVisible(true);
        emailCorpo.setVisible(true);

        VBoxDestinatario.setVisible(true);
        emailDestinatario.setVisible(true);
    }

    private void setupTableSelection(){
        tableView.setOnMouseClicked(event -> {
            // Per  la riga cliccata:
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                setupComunication(); // Show communication panel

                // clean fields
                emailOggetto.setText("");
                emailCorpo.setText("");

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
                    System.out.println("Errore");
                }
            }
        });
    }

    @FXML
    private void handleComunication() {
        userDAO.contattaDiabetologo(emailDestinatario.getText(), emailOggetto.getText(), emailCorpo.getText());
        VBoxContatta.setVisible(false);
        VBoxContatta.setManaged(false);
    }

    private void refreshTable(){
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
}
