module com.example.hspsm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hspsm to javafx.fxml;
    exports com.example.hspsm;
}