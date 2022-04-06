/**
 * Sample Skeleton for 'customerAddMod.fxml' Controller Class
 */

package com.example.javafxtravelexpert.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.javafxtravelexpert.entity.Customers;
import com.example.javafxtravelexpert.utils.DBConnectionMngr;
import com.example.javafxtravelexpert.utils.TravelExpertsProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CustomerAddModController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCustCancel"
    private Button btnCustCancel; // Value injected by FXMLLoader

    @FXML // fx:id="btnCustSave"
    private Button btnCustSave; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustAddress"
    private TextField txtCustAddress; // Value injected by FXMLLoader


    @FXML // fx:id="txtCustBusNo"
    private TextField txtCustBusNo; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustCity"
    private TextField txtCustCity; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustCountry"
    private TextField txtCustCountry; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustEmail"
    private TextField txtCustEmail; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustFirstName"
    private TextField txtCustFirstName; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustHomeNo"
    private TextField txtCustHomeNo; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustID"
    private TextField txtCustID; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustLastName"
    private TextField txtCustLastName; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustPostal"
    private TextField txtCustPostal; // Value injected by FXMLLoader

    @FXML // fx:id="txtCustProvince"
    private TextField txtCustProvince; // Value injected by FXMLLoader

    @FXML
    private ComboBox<Integer> cbAgentId;


    private ObservableList<Customers> customerList = FXCollections.observableArrayList();
    private ObservableList<Integer> agentIdList = FXCollections.observableArrayList();

    private boolean isEdit ;


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCustCancel != null : "fx:id=\"btnCustCancel\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert btnCustSave != null : "fx:id=\"btnCustSave\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustAddress != null : "fx:id=\"txtCustAddress\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustBusNo != null : "fx:id=\"txtCustBusNo\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustCity != null : "fx:id=\"txtCustCity\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustCountry != null : "fx:id=\"txtCustCountry\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustEmail != null : "fx:id=\"txtCustEmail\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustFirstName != null : "fx:id=\"txtCustFirstName\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustHomeNo != null : "fx:id=\"txtCustHomeNo\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustID != null : "fx:id=\"txtCustID\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustLastName != null : "fx:id=\"txtCustLastName\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustPostal != null : "fx:id=\"txtCustPostal\" was not injected: check your FXML file 'customerAddMod.fxml'.";
        assert txtCustProvince != null : "fx:id=\"txtCustProvince\" was not injected: check your FXML file 'customerAddMod.fxml'.";

        cbAgentId.setItems(agentIdList);
        getAgentIdList();

        //disable txt for primary key
        txtCustID.setDisable(true);

        //Cancel
        btnCustCancel.setOnMouseClicked(mouseEvent -> {
            closeDialog(mouseEvent);
        });

        //Save updated/new customer data to the DB
        btnCustSave.setOnMouseClicked(mouseEvent -> {
            if(isEdit == true)
                modifyCustomer(mouseEvent);
            else
                addCustomer(mouseEvent);

        });

    }

    /**
     * method to populate data on the agent dropdown menu
     *
     */
    private void getAgentIdList() {
        //initiate DB connection and objects
        DBConnectionMngr cm = DBConnectionMngr.getInstance();
        TravelExpertsProperties prop = new TravelExpertsProperties();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String psql = "SELECT AGENTID FROM AGENTS";
        Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(),prop.getDatabasePwd());
        if(conn != null){
            try {
                pstmt = conn.prepareStatement(psql);
                rs = pstmt.executeQuery();
                while (rs.next()){
                    agentIdList.add(rs.getInt(1));
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
     * method to receive to the observable list in customer controller
     * @param data
     */
    public void setObservableList(ObservableList<Customers> data)
    {
        customerList = data;
    }

    /**
     * Initialize values of the text fields(Customer obj property)
     */
    public void initCustomer(int selectedCustomerIndex) {
        System.out.println(customerList.size());
        isEdit = true;
        Customers customer = customerList.get(selectedCustomerIndex);

        txtCustID.setText(String.valueOf(customer.getCustId()));
        txtCustFirstName.setText(customer.getCustFirstName());
        txtCustLastName.setText(customer.getCustLastName());
        txtCustAddress.setText(customer.getCustAddress());
        txtCustCity.setText(customer.getCustCity());
        txtCustProvince.setText(customer.getCustProvince());
        txtCustPostal.setText(customer.getCustPostal());
        txtCustCountry.setText(customer.getCustCountry());
        txtCustHomeNo.setText(customer.getCustHomeNum());
        txtCustBusNo.setText(customer.getCustBusNum());
        txtCustEmail.setText(customer.getCustEmail());
        cbAgentId.setValue(customer.getCustAgentId());


    }

    /**
     * method that saves the modified changes on the customer obj property
     */

    private void modifyCustomer(MouseEvent mouseEvent){

        DBConnectionMngr cm = DBConnectionMngr.getInstance(); // get connection obj
        TravelExpertsProperties prop = new TravelExpertsProperties(); // instantiate property obj
        PreparedStatement pstmt = null;
        String psql = "UPDATE `CUSTOMERS` SET" +
                "`CustFirstName`=?," +
                "`CustLastName`=?," +
                "`CustAddress`=?," +
                "`CustCity`=?," +
                "`CustProv`=?," +
                "`CustPostal`=?," +
                "`CustCountry`=?," +
                "`CustHomePhone`=?," +
                "`CustBusPhone`=?," +
                "`CustEmail`=?," +
                "`AgentId`=? WHERE `CustomerID`=?";

        Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(),  prop.getDatabasePwd()); //initiate db connection

        try {

            conn.setAutoCommit(false); //disable auto commit

            pstmt = conn.prepareStatement(psql);

            //set sql parameter values
            pstmt.setString(1,txtCustFirstName.getText());
            pstmt.setString(2,txtCustLastName.getText());
            pstmt.setString(3,txtCustAddress.getText());
            pstmt.setString(4,txtCustCity.getText());
            pstmt.setString(5,txtCustProvince.getText());
            pstmt.setString(6,txtCustPostal.getText());
            pstmt.setString(7,txtCustCountry.getText());
            pstmt.setString(8,txtCustHomeNo.getText());
            pstmt.setString(9,txtCustBusNo.getText());
            pstmt.setString(10,txtCustEmail.getText());
            pstmt.setInt(11,cbAgentId.getValue());
            pstmt.setInt(12, Integer.parseInt(txtCustID.getText()));

            int rows = pstmt.executeUpdate();

            //commit or rollback
            if(rows == 1){
                conn.commit();
            }else {
                System.out.println("Update Failed");
                conn.rollback();
            }

            //close objects
            pstmt.close();
            conn.close();

            closeDialog(mouseEvent);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * method that saves the new Customer obj to the DB
     */
    private void addCustomer(MouseEvent mouseEvent) {

        DBConnectionMngr cm = DBConnectionMngr.getInstance();// get connection obj
        TravelExpertsProperties prop = new TravelExpertsProperties();// instantiate property obj
        PreparedStatement pstmt = null;
        String psql = "INSERT INTO `CUSTOMERS` (" +
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
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(), prop.getDatabasePwd());//initiate db connection

        try {
            conn.setAutoCommit(false);
            //set sql parameter values
            pstmt = conn.prepareStatement(psql);
            pstmt.setString(1,txtCustFirstName.getText());
            pstmt.setString(2,txtCustLastName.getText());
            pstmt.setString(3,txtCustAddress.getText());
            pstmt.setString(4,txtCustCity.getText());
            pstmt.setString(5,txtCustProvince.getText());
            pstmt.setString(6,txtCustPostal.getText());
            pstmt.setString(7,txtCustCountry.getText());
            pstmt.setString(8,txtCustHomeNo.getText());
            pstmt.setString(9,txtCustBusNo.getText());
            pstmt.setString(10,txtCustEmail.getText());
            pstmt.setInt(11,cbAgentId.getValue());
            int rows = pstmt.executeUpdate();

            //commit or rollback
            if(rows == 1){
                conn.commit();
            }else {
                System.out.println("Insert Failed");
                conn.rollback();
            }

            //close obj
            pstmt.close();
            conn.close();

            closeDialog(mouseEvent);


        } catch (SQLException e) {
            e.printStackTrace();
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

