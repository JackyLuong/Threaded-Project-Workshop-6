package com.example.threaded_project_workshop_6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TravelExpertsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("travel-packages-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        String css = this.getClass().getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Travel Packages");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}