package com.example.javafxtravelexpert.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import com.example.javafxtravelexpert.entity.Packages;
import com.example.javafxtravelexpert.utils.DBConnectionMngr;
import com.example.javafxtravelexpert.utils.TravelExpertsProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TravelPackagesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane apBackground;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModify;

    @FXML
    private TableColumn<Packages, Double> colAgencyCommission;

    @FXML
    private TableColumn<Packages, Double> colBasePrice;

    @FXML
    private TableColumn<Packages, String> colDescription;

    @FXML
    private TableColumn<Packages, Date> colEndDate;

    @FXML
    private TableColumn<Packages, Integer> colPkgId;

    @FXML
    private TableColumn<Packages, String> colPkgName;

    @FXML
    private TableColumn<Packages, Date> colStartDate;

    @FXML
    private TableView<Packages> tvPackages;

    private ObservableList<Packages> data = FXCollections.observableArrayList();

    private int selectedPackageIndex;

    private String packageAddModFXML = "/com/example/javafxtravelexpert/travel-packages-add-update-view.fxml";

    @FXML
    void initialize() {
        assert apBackground != null : "fx:id=\"apBackground\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert btnModify != null : "fx:id=\"btnModify\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert colAgencyCommission != null : "fx:id=\"colAgencyComission\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert colBasePrice != null : "fx:id=\"colBasePrice\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert colDescription != null : "fx:id=\"colDescription\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert colEndDate != null : "fx:id=\"colEndDate\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert colPkgId != null : "fx:id=\"colPkgId\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert colPkgName != null : "fx:id=\"colPkgName\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert colStartDate != null : "fx:id=\"colStartDate\" was not injected: check your FXML file 'travel-packages-view.fxml'.";
        assert tvPackages != null : "fx:id=\"tvPackages\" was not injected: check your FXML file 'travel-packages-view.fxml'.";

        //set columns to retrieve its corresponding data
        colPkgId.setCellValueFactory(new PropertyValueFactory<Packages, Integer>("packageId"));
        colPkgName.setCellValueFactory(new PropertyValueFactory<Packages, String>("pkgName"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<Packages, Date>("pkgStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<Packages, Date>("pkgEndDate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Packages, String>("pkgDesc"));
        colBasePrice.setCellValueFactory(new PropertyValueFactory<Packages, Double>("pkgBasePrice"));
        colAgencyCommission.setCellValueFactory(new PropertyValueFactory<Packages, Double>("pkgAgencyCommission"));

        tvPackages.setItems(data);

        getPackages();

        //add packages to database
        btnAdd.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                openDialog(false);
            }
        });

        //modify selected package
        btnModify.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                openDialog(true);
            }
        });

        btnDelete.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                //Create variables for each column in the packages table
                Packages selectedPackage = data.get(selectedPackageIndex);
                int packageId = selectedPackage.getPackageId();
                String pkgName = selectedPackage.getPkgName();
                Date pkgStartDate = selectedPackage.getPkgStartDate();
                Date pkgEndDate = selectedPackage.getPkgEndDate();
                String pkgDesc = selectedPackage.getPkgDesc();
                double pkgBasePrice = selectedPackage.getPkgBasePrice();
                double pkgAgencyCommission = selectedPackage.getPkgAgencyCommission();

                //Call properties object
                TravelExpertsProperties properties = new TravelExpertsProperties();

                try
                {
                    //connect to archive database
                    Connection archiveConn = DriverManager.getConnection(properties.getDatabaseURLArchive(), properties.getDatabaseUser(), properties.getDatabasePwd());

                    //set save point to undo insert if delete function fails
                    Savepoint savepoint = archiveConn.setSavepoint("insertSavepoint");

                    //insert string to archive database
                    String insertSQL = "INSERT INTO `packages`(`PackageId`, " +
                            "`PkgName`, " +
                            "`PkgStartDate`," +
                            "`PkgEndDate`, " +
                            "`PkgDesc`, " +
                            "`PkgBasePrice`, " +
                            "`PkgAgencyCommission`) " +
                            "VALUES (?,?,?,?,?,?,?)";
                    PreparedStatement insertSTMT = archiveConn.prepareStatement(insertSQL);

                    //set values to question marks
                    insertSTMT.setInt(1, packageId);
                    insertSTMT.setString(2, pkgName);
                    insertSTMT.setDate(3, pkgStartDate);
                    insertSTMT.setDate(4, pkgEndDate);
                    insertSTMT.setString(5, pkgDesc);
                    insertSTMT.setDouble(6, pkgBasePrice);
                    insertSTMT.setDouble(7, pkgAgencyCommission);

                    //execute insert
                    int numInsertRows = insertSTMT.executeUpdate();

                    //checks of there are any changes
                    if (numInsertRows == 0)
                    {
                        System.out.println("Insert failed");
                    }
                    //connect to main database
                    Connection conn = DriverManager.getConnection(properties.getDatabaseURL(), properties.getDatabaseUser(), properties.getDatabasePwd());

                    //sql to delete selected package
                    String sql = "DELETE FROM `packages` WHERE packageId=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    //set variable for question mark
                    stmt.setInt(1, packageId);

                    //execute delete
                    int numRows = stmt.executeUpdate();
                    //checks if the delete function was successful
                    if (numRows == 0)
                    {
                        archiveConn.rollback(savepoint); // revert insert
                        System.out.println("update failed");
                    }
                    archiveConn.close();
                    conn.close();
                    getPackages();
                }
                catch (SQLIntegrityConstraintViolationException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete failed");
                    alert.setContentText("The package is linked to other tables");
                    alert.showAndWait();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Gets table index based on what row the user clicked on
     * @param event
     */
    @FXML
    void onTvPackagesClicked(MouseEvent event) {
        if(tvPackages.getSelectionModel().getSelectedItem() != null)
            selectedPackageIndex = tvPackages.getSelectionModel().getSelectedIndex();
    }


    /**
     * Extract data from packages table in travel experts database
     */
    private void getPackages()
    {
        //clear the observable list prior to adding the data
        data.clear();

        //load the agents from the database
        DBConnectionMngr cm = DBConnectionMngr.getInstance();
        TravelExpertsProperties prop = new TravelExpertsProperties();

        try
        {
            //connection string to the database
            Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(), prop.getDatabasePwd());
            //create sql statement
            Statement stmt = conn.createStatement();
            //execute sql statement
            ResultSet rs = stmt.executeQuery("select * from packages");
            //format prices to commission to canadian currency
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            //get all data from agents table
            while (rs.next())
            {
                data.add(new Packages
                            (
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getDate(3),
                                rs.getDate(4),
                                rs.getString(5),
                                rs.getDouble(6),
                                rs.getDouble(7)
                            )
                        );
            }
            //close connection
            conn.close();
        }
        //Handle exception
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Open a form that allows users to edit selected agent information and update it in the database
     */
    private void openDialog(boolean isEdit)
    {
        //loads edit agent dialog
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(packageAddModFXML));
        Parent parent = null; //set parent variable to be called later in the dialog

        try
        {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //add and edit packages dialog controller
        TravelPackagesAddUpdateController addUpdateController = fxmlLoader.getController();

        //transfer data information to add/edit packages dialog
        addUpdateController.setObservableList(data);


        //Display selected package information if user is modifying
        if (isEdit == true)
            addUpdateController.initPackage(selectedPackageIndex);

        Scene scene = new Scene(parent);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Package Form");// set title for edit agent dialog
        stage.setScene(scene);
        stage.showAndWait(); // display edit agent dialog

        getPackages();
    }
}
