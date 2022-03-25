package com.example.threaded_project_workshop_6;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private TableColumn<Package, Double> colAgencyCommission;

    @FXML
    private TableColumn<Package, Double> colBasePrice;

    @FXML
    private TableColumn<Package, String> colDescription;

    @FXML
    private TableColumn<Package, Date> colEndDate;

    @FXML
    private TableColumn<Package, Integer> colPkgId;

    @FXML
    private TableColumn<Package, String> colPkgName;

    @FXML
    private TableColumn<Package, Date> colStartDate;

    @FXML
    private TableView<Package> tvPackages;

    private ObservableList<Package> data = FXCollections.observableArrayList();

    private int selectedPackageIndex;

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
        colPkgId.setCellValueFactory(new PropertyValueFactory<Package, Integer>("packageId"));
        colPkgName.setCellValueFactory(new PropertyValueFactory<Package, String>("pkgName"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<Package, Date>("pkgStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<Package, Date>("pkgEndDate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Package, String>("pkgDesc"));
        colBasePrice.setCellValueFactory(new PropertyValueFactory<Package, Double>("pkgBasePrice"));
        colAgencyCommission.setCellValueFactory(new PropertyValueFactory<Package, Double>("pkgAgencyCommission"));

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
     * Reads connection properties file and returns url, username, and password to connect to the database
     * @return
     */
    private String[] getConnectionCredentials()
    {
        //Connection credentials
        String user = "";
        String password = "";
        String url = "";

        //read connection properties to connection to database
        try
        {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/com/example/threaded_project_workshop_6/ConnectionProperties.properties");
            Properties p = new Properties();
            p.load(fileInputStream);
            url = (String) p.get("url");
            user = (String) p.get("user");
            password = (String) p.get("password");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String[] credentials = new String[]{url, user, password};
        return credentials;
    }

    /**
     * Extract data from packages table in travel experts database
     */
    private void getPackages()
    {
        //clear the observable list prior to adding the data
        data.clear();

        //load the agents from the database
        String[] credentials = getConnectionCredentials();
        String url = credentials[0];
        String userName = credentials[1];
        String password = credentials[2];

        try
        {
            //connection string to the database
            Connection conn = DriverManager.getConnection(url, userName, password);
            //create sql statement
            Statement stmt = conn.createStatement();
            //execute sql statement
            ResultSet rs = stmt.executeQuery("select * from packages");
            //format prices to commission to canadian currency
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            //get all data from agents table
            while (rs.next())
            {
                data.add(new Package
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
        FXMLLoader fxmlLoader = new FXMLLoader(TravelExpertsApplication.class.getResource("travel-packages-add-update-view.fxml"));
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
        TravelPackagesAddUpdateController addUpdateController = fxmlLoader.<TravelPackagesAddUpdateController>getController();

        //transfer data information to add/edit packages dialog
        addUpdateController.setObservableList(data);

        //transfer connection credentials to add/edit dialog
        addUpdateController.getConnectionCredentials(getConnectionCredentials());

        //Display selected package information if user is modifying
        if (isEdit == true)
            addUpdateController.initPackage(selectedPackageIndex);

        Scene scene = new Scene(parent, 1000, 600);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Package Form");// set title for edit agent dialog
        stage.setScene(scene);
        stage.showAndWait(); // display edit agent dialog

        getPackages();
    }
}
