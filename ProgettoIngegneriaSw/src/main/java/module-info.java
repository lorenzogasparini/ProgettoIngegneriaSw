module com.example.progettoingegneriasw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.progettoingegneriasw to javafx.fxml;
    opens com.progettoingegneriasw.controller to javafx.fxml;
    opens com.progettoingegneriasw.view.components to javafx.fxml;

    exports com.progettoingegneriasw;
    exports com.progettoingegneriasw.controller;
    exports com.progettoingegneriasw.model;
    exports com.progettoingegneriasw.view;
    exports com.progettoingegneriasw.view.components;
}
