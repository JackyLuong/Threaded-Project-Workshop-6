package com.example.javafxtravelexpert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Travel Experts Login");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}