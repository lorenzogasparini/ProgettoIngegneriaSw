module com.example.progettoingegneriasw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.progettoingegneriasw to javafx.fxml;
    exports com.progettoingegneriasw;
    exports com.progettoingegneriasw.controller;
    opens com.progettoingegneriasw.controller to javafx.fxml;
}