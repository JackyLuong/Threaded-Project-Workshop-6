module com.example.javafxtravelexpertlogin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javafxtravelexpert to javafx.fxml;
    exports com.example.javafxtravelexpert;
    exports com.example.javafxtravelexpert.controllers;
    opens com.example.javafxtravelexpert.controllers to javafx.fxml;
    exports com.example.javafxtravelexpert.utils;
    opens com.example.javafxtravelexpert.utils to javafx.fxml;
    exports com.example.javafxtravelexpert.entity;
    opens com.example.javafxtravelexpert.entity to javafx.fxml;
}