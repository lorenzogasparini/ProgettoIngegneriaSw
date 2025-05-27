module com.example.progettoingegneriasw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.progettoingegneriasw to javafx.fxml;
    opens com.progettoingegneriasw.controller to javafx.fxml;
    opens com.progettoingegneriasw.view.components to javafx.fxml;

    requires javafx.base;

    opens com.progettoingegneriasw.model.Paziente to javafx.base;
    opens com.progettoingegneriasw.model.Utils to javafx.base;

    exports com.progettoingegneriasw;
    exports com.progettoingegneriasw.controller;
    exports com.progettoingegneriasw.model;
    exports com.progettoingegneriasw.view;
    exports com.progettoingegneriasw.view.components;
}
