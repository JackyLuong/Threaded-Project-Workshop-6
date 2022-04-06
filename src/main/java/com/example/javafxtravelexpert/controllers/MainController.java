/**
 * Sample Skeleton for 'main.fxml' Controller Class
 */

package com.example.javafxtravelexpert.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnBooking"
    private Button btnBooking; // Value injected by FXMLLoader

    @FXML // fx:id="btnCustomer"
    private Button btnCustomer; // Value injected by FXMLLoader

    @FXML // fx:id="btnHome"
    private Button btnHome; // Value injected by FXMLLoader

    @FXML // fx:id="btnPackage"
    private Button btnPackage; // Value injected by FXMLLoader

    @FXML // fx:id="bpMain"
    private BorderPane bpMain; // Value injected by FXMLLoader

    @FXML // fx:id="btnLogout"
    private Button btnLogout; // Value injected by FXMLLoader

    private String homeFXML = "/com/example/javafxtravelexpert/home.fxml";
    private String customersFXML = "/com/example/javafxtravelexpert/customers.fxml";
    private String bookingsFXML = "/com/example/javafxtravelexpert/bookings.fxml";
    private String packagesFXML = "/com/example/javafxtravelexpert/travel-packages-view.fxml";



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnBooking != null : "fx:id=\"btnBooking\" was not injected: check your FXML file 'main.fxml'.";
        assert btnCustomer != null : "fx:id=\"btnCustomer\" was not injected: check your FXML file 'main.fxml'.";
        assert btnHome != null : "fx:id=\"btnHome\" was not injected: check your FXML file 'main.fxml'.";
        assert btnPackage != null : "fx:id=\"btnPackage\" was not injected: check your FXML file 'main.fxml'.";

        //show home scene as default
        showSelectedMenu(homeFXML);
        btnHome.getStyleClass().add("selectedButton");

        //show home scene
        btnHome.setOnMouseClicked(event ->{
            removeSelectedClass(); //remove styling
            showSelectedMenu(homeFXML); //call method to show scene
            this.btnHome.getStyleClass().add("selectedButton"); //add styling for the clicked button
        });

        //show customers scene
        btnCustomer.setOnMouseClicked(event ->{
            removeSelectedClass();
            showSelectedMenu(customersFXML);
            this.btnCustomer.getStyleClass().add("selectedButton");

        });

        //show bookings scene
        btnBooking.setOnMouseClicked(event ->{
            removeSelectedClass();
            showSelectedMenu(bookingsFXML);
            this.btnBooking.getStyleClass().add("selectedButton");
        });

        //show packages scene
        btnPackage.setOnMouseClicked( event ->{
            removeSelectedClass();
            showSelectedMenu(packagesFXML);
            this.btnPackage.getStyleClass().add("selectedButton");
        });



        //logout
        btnLogout.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("You're about to logout!");
            alert.setContentText("Are you sure you want to logout? ");

            if(alert.showAndWait().get() == ButtonType.OK){
                Stage stage = (Stage)bpMain.getScene().getWindow();
                System.out.println("You successfully logged out!");
                stage.close();
            }else {
                btnHome.requestFocus();
            }
        });

    }

    /**
     * method that changes the scene on each menu button
     */
    private void showSelectedMenu(String path) {
        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource(path));
            bpMain.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * remove css styling on the menu buttons
     */

    private void removeSelectedClass(){
        btnHome.getStyleClass().remove("selectedButton");
        btnCustomer.getStyleClass().remove("selectedButton");
        btnPackage.getStyleClass().remove("selectedButton");
        btnBooking.getStyleClass().remove("selectedButton");
    }




}
