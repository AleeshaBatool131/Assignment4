module com.example.hspsm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.hspsm to javafx.fxml;
    exports com.example.hspsm;
}