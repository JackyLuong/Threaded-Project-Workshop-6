module com.example.threaded_project_workshop_6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.threaded_project_workshop_6 to javafx.fxml;
    exports com.example.threaded_project_workshop_6;
}