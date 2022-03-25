package com.example.threaded_project_workshop_6;

import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TravelPackagesAddUpdateController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField tfAgencyCommission;

    @FXML
    private TextField tfBasePrice;

    @FXML
    private TextField tfDesc;

    @FXML
    private TextField tfEndDate;

    @FXML
    private TextField tfPkgName;

    @FXML
    private TextField tfStartDate;

    private ObservableList<Package> mainObservableList = FXCollections.observableArrayList();

    private int selectedPackageIndex;

    private String[] connectionCredentials;

    private boolean isEdit = false;

    @FXML
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert pane != null : "fx:id=\"pane\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert tfAgencyCommission != null : "fx:id=\"tfAgencyCommission\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert tfBasePrice != null : "fx:id=\"tfBasePrice\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert tfDesc != null : "fx:id=\"tfDesc\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert tfEndDate != null : "fx:id=\"tfEndDate\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert tfPkgName != null : "fx:id=\"tfPkgName\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";
        assert tfStartDate != null : "fx:id=\"tfStartDate\" was not injected: check your FXML file 'travel-packages-add-update-view.fxml'.";

        //return to main dialog
        btnCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent)
            {
                closeDialog(mouseEvent);
            }
        });

        //Save changes and return to dialog
        btnSave.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if(isEdit == true)
                    modifyPackage(mouseEvent);
                else
                    addPackage(mouseEvent);
            }
        });
    }

    /**
     * method to receive to the observable list in main controller
     * @param data
     */
    public void setObservableList(ObservableList<Package> data)
    {
        mainObservableList = data;
    }

    /**
     * method that retrieves the selected package and displays information on the corresponding text fields
     */
    public void initPackage(int selectedPackageIndex)
    {
        isEdit = true; //change form to update the selected package

        this.selectedPackageIndex = selectedPackageIndex;
        Package selectedPackage = mainObservableList.get(selectedPackageIndex);

        String strEndDate = selectedPackage.getPkgEndDate().toString(); //convert end date to string
        String strStartDate = selectedPackage.getPkgStartDate().toString(); // convert start date to string

        tfPkgName.setText(selectedPackage.getPkgName());
        tfStartDate.setText(strStartDate);
        tfEndDate.setText(strEndDate);
        tfDesc.setText(selectedPackage.getPkgDesc());
        tfBasePrice.setText(Double.toString(selectedPackage.getPkgBasePrice()));
        tfAgencyCommission.setText(Double.toString(selectedPackage.getPkgAgencyCommission()));
    }

    /**
     * Gets connection credentials from main controller
     * @return
     */
    public void getConnectionCredentials(String[] credentials)
    {
        connectionCredentials = credentials;
    }

    /**
     * Add package to database
     */
    private void addPackage(MouseEvent mouseEvent)
    {
        double basePrice = Double.parseDouble(tfBasePrice.getText());

        //Validates data before modifying the database
        if (Validator.isPresent(tfPkgName) &&
                Validator.isPresent(tfBasePrice) &&
                Validator.isDoublePositive(tfBasePrice) &&
                Validator.isStartDateValid(tfStartDate) &&
                Validator.isEndDateValid(tfStartDate, tfEndDate) &&
                Validator.isDoubleInRange(0,basePrice,tfAgencyCommission))
        {
            //load the agents from the database
            String[] credentials = connectionCredentials;
            String url = credentials[0];
            String userName = credentials[1];
            String password = credentials[2];

            try {
                Connection conn = DriverManager.getConnection(url, userName, password); // connect to database
                //SQL String to add package to data
                String sql = "INSERT INTO `packages`(`PackageId`, " +
                        "`PkgName`, " +
                        "`PkgStartDate`," +
                        "`PkgEndDate`, " +
                        "`PkgDesc`, " +
                        "`PkgBasePrice`, " +
                        "`PkgAgencyCommission`) " +
                        "VALUES (null,?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                Date startDate = Date.valueOf(tfStartDate.getText());
                Date endDate = Date.valueOf(tfEndDate.getText());

                //Assign values for question marks
                stmt.setString(1, tfPkgName.getText());
                stmt.setDate(2, startDate);
                stmt.setDate(3, endDate);
                stmt.setString(4, tfDesc.getText());
                stmt.setDouble(5, Double.parseDouble(tfBasePrice.getText()));
                stmt.setDouble(6, Double.parseDouble(tfAgencyCommission.getText()));

                int numRows = stmt.executeUpdate();

                //checks of there are any changes
                if (numRows == 0) {
                    System.out.println("update failed");
                }
                conn.close();

                //return to main dialog
                closeDialog(mouseEvent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Modify selected package and update database
     * @param mouseEvent
     */
    private void modifyPackage(MouseEvent mouseEvent)
    {
        double basePrice = Double.parseDouble(tfBasePrice.getText());

        //Validates data before modifying the database
        if (Validator.isPresent(tfPkgName) &&
            Validator.isPresent(tfBasePrice) &&
            Validator.isDoublePositive(tfBasePrice) &&
            Validator.isStartDateValid(tfStartDate) &&
            Validator.isEndDateValid(tfStartDate, tfEndDate) &&
            Validator.isDoubleInRange(0,basePrice,tfAgencyCommission))
        {
            //load the agents from the database
            String[] credentials = connectionCredentials;
            String url = credentials[0];
            String userName = credentials[1];
            String password = credentials[2];

            try {
                Connection conn = DriverManager.getConnection(url, userName, password); // connect to database
                //SQL String to add package to data
                String sql = "UPDATE `packages` " +
                        "SET `PkgName`=?," +
                        "`PkgStartDate`=?," +
                        "`PkgEndDate`=?," +
                        "`PkgDesc`=?," +
                        "`PkgBasePrice`=?," +
                        "`PkgAgencyCommission`=? WHERE `PackageId` =?";

                PreparedStatement stmt = conn.prepareStatement(sql);

                Date startDate = Date.valueOf(tfStartDate.getText());
                Date endDate = Date.valueOf(tfEndDate.getText());

                //selected package
                Package selectedPackage = mainObservableList.get(selectedPackageIndex);

                //Assign values for question marks
                stmt.setString(1, tfPkgName.getText());
                stmt.setDate(2, startDate);
                stmt.setDate(3, endDate);
                stmt.setString(4, tfDesc.getText());
                stmt.setDouble(5, Double.parseDouble(tfBasePrice.getText()));
                stmt.setDouble(6, Double.parseDouble(tfAgencyCommission.getText()));
                stmt.setInt(7, selectedPackage.getPackageId());

                int numRows = stmt.executeUpdate();

                //checks of there are any changes
                if (numRows == 0) {
                    System.out.println("update failed");
                }
                conn.close();

                //return to main form
                closeDialog(mouseEvent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * close and return to main dialog
     */
    private void closeDialog(MouseEvent mouseEvent)
    {
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
