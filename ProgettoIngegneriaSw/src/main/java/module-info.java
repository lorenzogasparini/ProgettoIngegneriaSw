module com.example.progettoingegneriasw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.progettoingegneriasw to javafx.fxml;
    exports com.example.progettoingegneriasw;
}