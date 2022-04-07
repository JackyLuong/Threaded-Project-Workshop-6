/**
 * Sample Skeleton for 'customers.fxml' Controller Class
 */

package com.example.javafxtravelexpert.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


import com.example.javafxtravelexpert.entity.Customers;
import com.example.javafxtravelexpert.utils.DBConnectionMngr;
import com.example.javafxtravelexpert.utils.TravelExpertsProperties;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CustomersController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Button btnCustSearch;

    @FXML // fx:id="btnCustAdd"
    private Button btnCustAdd; // Value injected by FXMLLoader

    @FXML // fx:id="btnCustDelete"
    private Button btnCustDelete; // Value injected by FXMLLoader

    @FXML // fx:id="btnCustEdit"
    private Button btnCustEdit; // Value injected by FXMLLoader

    @FXML // fx:id="colAddress"
    private TableColumn<Customers, String> colAddress; // Value injected by FXMLLoader

    @FXML // fx:id="colAgencyId"
    private TableColumn<Customers, Integer> colAgentId; // Value injected by FXMLLoader

    @FXML // fx:id="colBusNum"
    private TableColumn<Customers, String> colBusNum; // Value injected by FXMLLoader

    @FXML // fx:id="colCity"
    private TableColumn<Customers, String> colCity; // Value injected by FXMLLoader

    @FXML // fx:id="colCountry"
    private TableColumn<Customers, String> colCountry; // Value injected by FXMLLoader

    @FXML // fx:id="colEmail"
    private TableColumn<Customers, String> colEmail; // Value injected by FXMLLoader

    @FXML // fx:id="colHomeNum"
    private TableColumn<Customers, String> colHomeNum; // Value injected by FXMLLoader

    @FXML // fx:id="colName"
    private TableColumn<Customers, String> colName; // Value injected by FXMLLoader

    @FXML // fx:id="colProvince"
    private TableColumn<Customers, String> colProvince; // Value injected by FXMLLoader

    @FXML // fx:id="colPostal"
    private TableColumn<Customers, String> colPostal; // Value injected by FXMLLoader

    @FXML // fx:id="colCustId"
    private TableColumn<Customers, Integer> colCustId; // Value injected by FXMLLoader

    @FXML // fx:id="tvCustomers"
    private TableView<Customers> tvCustomers; // Value injected by FXMLLoader

    @FXML
    private TextField txtCustomer;

    ObservableList<Customers> data = FXCollections.observableArrayList();

    private String custAddModFXML = "/com/example/javafxtravelexpert/customerAddMod.fxml";

    private int selectedCustomerIndex ;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert colAddress != null : "fx:id=\"colAddress\" was not injected: check your FXML file 'customers.fxml'.";
        assert colAgentId != null : "fx:id=\"colAgencyId\" was not injected: check your FXML file 'customers.fxml'.";
        assert colBusNum != null : "fx:id=\"colBusNum\" was not injected: check your FXML file 'customers.fxml'.";
        assert colCity != null : "fx:id=\"colCity\" was not injected: check your FXML file 'customers.fxml'.";
        assert colCountry != null : "fx:id=\"colCountry\" was not injected: check your FXML file 'customers.fxml'.";
        assert colEmail != null : "fx:id=\"colEmail\" was not injected: check your FXML file 'customers.fxml'.";
        assert colHomeNum != null : "fx:id=\"colHomeNum\" was not injected: check your FXML file 'customers.fxml'.";
        assert colName != null : "fx:id=\"colName\" was not injected: check your FXML file 'customers.fxml'.";
        assert colProvince != null : "fx:id=\"colProvince\" was not injected: check your FXML file 'customers.fxml'.";
        assert colPostal != null : "fx:id=\"colPostal\" was not injected: check your FXML file 'customers.fxml'.";
        assert colCustId != null : "fx:id=\"colPostal\" was not injected: check your FXML file 'customers.fxml'.";
        assert tvCustomers != null : "fx:id=\"tvCustomers\" was not injected: check your FXML file 'customers.fxml'.";


        colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customers, String>,
                        ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(
                    TableColumn.CellDataFeatures<Customers, String> c) {
                return new SimpleStringProperty(c.getValue().getCustLastName()
                        + ", " + c.getValue().getCustFirstName());
            }
        });

        colAddress.setCellValueFactory(new PropertyValueFactory<Customers, String>("custAddress"));
        colCity.setCellValueFactory(new PropertyValueFactory<Customers, String>("custCity"));
        colProvince.setCellValueFactory(new PropertyValueFactory<Customers, String>("custProvince"));
        colPostal.setCellValueFactory(new PropertyValueFactory<Customers, String>("custPostal"));
        colCountry.setCellValueFactory(new PropertyValueFactory<Customers, String>("custCountry"));
        colHomeNum.setCellValueFactory(new PropertyValueFactory<Customers, String>("custHomeNum"));
        colBusNum.setCellValueFactory(new PropertyValueFactory<Customers, String>("custBusNum"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Customers, String>("custEmail"));
        colAgentId.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("custAgentId"));
        colCustId.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("custId"));

        tvCustomers.setItems(data);
        getCustomers();

        //disable edit and delete button as default until user select a customer
        btnCustDelete.setDisable(true);
        btnCustEdit.setDisable(true);

        //enable edit and delete button after selection made
        //initialize selected customer
        tvCustomers.setOnMouseClicked(mouseEvent -> {
            btnCustDelete.setDisable(false);
            btnCustEdit.setDisable(false);

            if(tvCustomers.getSelectionModel().getSelectedItem() != null){
                selectedCustomerIndex = tvCustomers.getSelectionModel().getSelectedIndex();
            }
        });

        //modify customer data
        btnCustEdit.setOnMouseClicked(mouseEvent -> openCustDialog(true));

        //add new customer data
        btnCustAdd.setOnMouseClicked(mouseEvent -> openCustDialog(false));

        //search entered customer
        btnCustSearch.setOnMouseClicked(mouseEvent -> {
            if (txtCustomer.getText().isEmpty()) {
                getCustomers();
            }else
                getSearchCustomer();
        });

        //delete customer
        btnCustDelete.setOnMouseClicked(mouseEvent -> {


            //ask for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete customer?");
            alert.setContentText("Are you sure to delete the customer?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(type ->{
                if (type == yesButton){

                    delCustomer(); // call delete method
                    //get update customer list
                    getCustomers();
                    tvCustomers.getSelectionModel().select(0);// highlight the first customer data
                    ;
                }
            });

        });

    }

    /**
     * Method that deletes the customer data and insert to the archive db
     */

    private void delCustomer() {

        //define and assign variables
        Customers customer = data.get(selectedCustomerIndex);
        int oldPackageId = customer.getCustId();
        String firstName = customer.getCustFirstName();
        String lastName = customer.getCustLastName();
        String address = customer.getCustAddress();
        String city = customer.getCustCity();
        String province = customer.getCustProvince();
        String postal = customer.getCustPostal();
        String country = customer.getCustCountry();
        String homeNo = customer.getCustHomeNum();
        String busNo = customer.getCustBusNum();
        String email = customer.getCustEmail();
        int agentId = customer.getCustAgentId();


        DBConnectionMngr cm = DBConnectionMngr.getInstance(); //get conn obj
        TravelExpertsProperties prop = new TravelExpertsProperties(); //instantiate property obj
        PreparedStatement pstmt = null;
        String insertPsql = "INSERT INTO `CUSTOMERS` (" +
                "`CustomerIdOld`," +
                "`CustFirstName`," +
                "`CustLastName`," +
                "`CustAddress`," +
                "`CustCity`," +
                "`CustProv`," +
                "`CustPostal`," +
                "`CustCountry`," +
                "`CustHomePhone`," +
                "`CustBusPhone`," +
                "`CustEmail`," +
                "`AgentId`)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        String delPsql = "DELETE FROM `CUSTOMERS` WHERE CustomerId=?";

        //connect to archive db
        Connection archiveConn = cm.getConnection(prop.getDatabaseURLArchive(),prop.getDatabaseUser(), prop.getDatabasePwd());

        //connect to prod db
        Connection conn = cm.getConnection(prop.getDatabaseURL(),prop.getDatabaseUser(),prop.getDatabasePwd());

        if(archiveConn != null && conn != null) {
            try {

                //initiate delete row
                archiveConn.setAutoCommit(false);
                pstmt = archiveConn.prepareStatement(insertPsql);
                //set sql parameter value
                pstmt.setInt(1, oldPackageId);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, address);
                pstmt.setString(5, city);
                pstmt.setString(6, province);
                pstmt.setString(7, postal);
                pstmt.setString(8, country);
                pstmt.setString(9, homeNo);
                pstmt.setString(10, busNo);
                pstmt.setString(11, email);
                pstmt.setInt(12, agentId);

                int insertRow = pstmt.executeUpdate();

                //initiate insert row to archive db
                conn.setAutoCommit(false);

                //set sql parameter value
                pstmt = conn.prepareStatement(delPsql);
                pstmt.setInt(1, customer.getCustId());
                int delRow = pstmt.executeUpdate();

                //validate the number of delete/insert row
                if (insertRow == 1 && delRow == 1) {
                    archiveConn.commit(); // commit insert
                    conn.commit(); //commit delete

                } else {
                    archiveConn.rollback(); //rollback insert
                    conn.rollback(); //rollback delete
                }

            }catch (SQLIntegrityConstraintViolationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete failed");
                alert.setContentText("Customer has booking and cannot be deleted!");
                alert.showAndWait();

            }catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    //close connection objs
                    pstmt.close();
                    archiveConn.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    /**
     * Method that query the entered customer name by the user
     */
    private void getSearchCustomer() {
        data.clear();
        //initiate DB connection and jdbc objects
        DBConnectionMngr cm = DBConnectionMngr.getInstance();
        TravelExpertsProperties prop = new TravelExpertsProperties();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String psql = "SELECT * FROM CUSTOMERS WHERE CUSTLASTNAME LIKE? OR CUSTFIRSTNAME LIKE? ORDER BY CustLastName";
        String searchCust = "%" + txtCustomer.getText() + "%";
        Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(), prop.getDatabasePwd());
        //execute
        if(conn != null){
            try {
                pstmt = conn.prepareStatement(psql);
                pstmt.setString(1,searchCust);
                pstmt.setString(2,searchCust);
                rs = pstmt.executeQuery();
                while (rs.next()){
                    data.add(new Customers(rs.getInt(1),rs.getString(2),rs.getString(3),
                            rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                            rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                            rs.getInt(12)));

                }
                pstmt.close();
                rs.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * Open dialog to add/edit customer
     */
    private void openCustDialog(boolean isEdit) {
        //instantiate fxml object
        FXMLLoader fxmlLoader = new FXMLLoader(CustomerAddModController.class.getResource(custAddModFXML));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //assign controller obj
        CustomerAddModController customerAddModController = fxmlLoader.getController();

        //transfer data information to add/edit customers dialog
        customerAddModController.setObservableList(data);

        String stageTitle = null;

        //Display selected package information if user is modifying or adding customer
        if (isEdit == true){
            customerAddModController.initCustomer(selectedCustomerIndex);
            stageTitle = "Modify Customer";
        }else {
            stageTitle = "Add Customer";
        }

        //create scene and stage objects
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(stageTitle);// set title for edit agent dialog
        stage.setScene(scene);
        stage.showAndWait(); // display edit agent dialog

        getCustomers(); //get updated customers
        tvCustomers.getSelectionModel().select(selectedCustomerIndex);// highlight the updated customer data

    }

    /**
     * Load customers data
     */
    private void getCustomers() {
        data.clear();
        DBConnectionMngr cm = DBConnectionMngr.getInstance();
        TravelExpertsProperties prop = new TravelExpertsProperties();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String psql = "SELECT * FROM CUSTOMERS ORDER BY CustLastName";
        Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(), prop.getDatabasePwd());

        if(conn != null){
            try {
                pstmt = conn.prepareStatement(psql);
                rs = pstmt.executeQuery();
                while (rs.next()){
                    data.add(new Customers(rs.getInt(1),rs.getString(2),rs.getString(3),
                            rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                            rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                            rs.getInt(12)));

                }
                pstmt.close();
                rs.close();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

}
