package com.example.javafxtravelexpert.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import com.example.javafxtravelexpert.entity.Bookings;

import com.example.javafxtravelexpert.utils.DBConnectionMngr;
import com.example.javafxtravelexpert.utils.TravelExpertsProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookingsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnBookingAdd;

    @FXML
    private Button btnBookingDelete;

    @FXML
    private Button btnBookingEdit;

    @FXML
    private TableColumn<Bookings, String> colBookingNo;

    @FXML
    private TableColumn<Bookings, Integer> colCustomerId;

    @FXML
    private TableColumn<Bookings, Date> colDate;

    @FXML
    private TableColumn<Bookings, Integer> colPackageId;

    @FXML
    private TableColumn<Bookings, Double> colTotal;

    @FXML
    private TableColumn<Bookings, String> colTripType;

    @FXML
    private TableView<Bookings> tvBookings;

    private ObservableList<Bookings> data = FXCollections.observableArrayList();

    private int selectedBookingIndex;

    private String bookingAddModFXML = "/com/example/javafxtravelexpert/bookings-add-update-view.fxml";

    @FXML
    void initialize()
    {
        assert btnBookingAdd != null : "fx:id=\"btnBookingAdd\" was not injected: check your FXML file 'bookings.fxml'.";
        assert btnBookingDelete != null : "fx:id=\"btnBookingDelete\" was not injected: check your FXML file 'bookings.fxml'.";
        assert btnBookingEdit != null : "fx:id=\"btnBookingEdit\" was not injected: check your FXML file 'bookings.fxml'.";
        assert colBookingNo != null : "fx:id=\"colBookingNo\" was not injected: check your FXML file 'bookings.fxml'.";
        assert colCustomerId != null : "fx:id=\"colCustomerId\" was not injected: check your FXML file 'bookings.fxml'.";
        assert colDate != null : "fx:id=\"colDate\" was not injected: check your FXML file 'bookings.fxml'.";
        assert colPackageId != null : "fx:id=\"colPackageId\" was not injected: check your FXML file 'bookings.fxml'.";
        assert colTotal != null : "fx:id=\"colTotal\" was not injected: check your FXML file 'bookings.fxml'.";
        assert colTripType != null : "fx:id=\"colTripType\" was not injected: check your FXML file 'bookings.fxml'.";
        assert tvBookings != null : "fx:id=\"tvBookings\" was not injected: check your FXML file 'bookings.fxml'.";

        //set columns to retrieve its corresponding data
        colBookingNo.setCellValueFactory(new PropertyValueFactory<Bookings, String>("bookingNo"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<Bookings, Integer>("customer"));
        colDate.setCellValueFactory(new PropertyValueFactory<Bookings, Date>("bookingDate"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<Bookings, Integer>("_package"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Bookings, Double>("bookingTotal"));
        colTripType.setCellValueFactory(new PropertyValueFactory<Bookings, String>("tripType"));

        tvBookings.setItems(data);

        getBookings();

        //add packages to database
        btnBookingAdd.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                openDialog(false);
            }
        });

        //modify selected booking
        btnBookingEdit.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                openDialog(true);
            }
        });

        btnBookingDelete.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                //Create variables for each column in the bookings table
                Bookings selectedBookings= data.get(selectedBookingIndex);
                int BookingId = selectedBookings.getId();
                Date BookingDate = (Date) selectedBookings.getBookingDate();
                String BookingNo = selectedBookings.getBookingNo();
                double BookingTotal = selectedBookings.getBookingTotal();
                double TravelerCount = selectedBookings.getTravelerCount();
                int CustomerId = selectedBookings.getCustomer();
                String TripTypeId = selectedBookings.getTripType();
                int PackageId = selectedBookings.get_package();

                //Call properties object
                TravelExpertsProperties properties = new TravelExpertsProperties();

                try
                {
                    //connect to archive database
                    Connection archiveConn = DriverManager.getConnection(properties.getDatabaseURLArchive(), properties.getDatabaseUser(), properties.getDatabasePwd());

                    //set save point to undo insert if delete function fails
                    Savepoint savepoint = archiveConn.setSavepoint("insertSavepoint");

                    //insert string to archive database
                    String insertSQL = "INSERT INTO `bookings`(`BookingId`, " +
                            "`BookingDate`, " +
                            "`BookingNo`," +
                            "`BookingTotal`, " +
                            "`TravelerCount`, " +
                            "`CustomerId`, " +
                            "`TripTypeId`, " +
                            "`PackageId`) " +
                            "VALUES (?,?,?,?,?,?,?,?)";
                    PreparedStatement insertSTMT = archiveConn.prepareStatement(insertSQL);

                    //set values to question marks
                    insertSTMT.setInt(1, BookingId);
                    insertSTMT.setDate(2, BookingDate);
                    insertSTMT.setString(3, BookingNo);
                    insertSTMT.setDouble(4, BookingTotal);
                    insertSTMT.setDouble(5, TravelerCount);
                    insertSTMT.setInt(6, CustomerId);
                    insertSTMT.setString(7, TripTypeId);
                    insertSTMT.setInt(8, PackageId);

                    //execute insert
                    int numInsertRows = insertSTMT.executeUpdate();

                    //checks of there are any changes
                    if (numInsertRows == 0)
                    {
                        System.out.println("Insert failed");
                    }
                    //connect to main database
                    Connection conn = DriverManager.getConnection(properties.getDatabaseURL(), properties.getDatabaseUser(), properties.getDatabasePwd());

                    //sql to delete selected booking
                    String sql = "DELETE FROM `Bookings` WHERE bookingId=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    //set variable for question mark
                    stmt.setInt(1, BookingId);

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
                    getBookings();
                }
                catch (SQLIntegrityConstraintViolationException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete failed");
                    alert.setContentText("The booking is linked to other tables");
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
     * Extract data from bookings table in travel experts database
     */
    private void getBookings()
    {
        //clear the observable list prior to adding the data
        data.clear();

        //load the bookings from the database
        DBConnectionMngr cm = DBConnectionMngr.getInstance();
        TravelExpertsProperties prop = new TravelExpertsProperties();

        try
        {
            //connection string to the database
            Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(), prop.getDatabasePwd());
            //create sql statement
            Statement stmt = conn.createStatement();
            //execute sql statement
            ResultSet rs = stmt.executeQuery("select * from bookings");
            //format prices to commission to canadian currency
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            //get all data from bookings table
            while (rs.next())
            {
                data.add(new Bookings
                    (
                            rs.getInt(1),
                            rs.getDate(2),
                            rs.getString(3),
                            rs.getDouble(4),
                            rs.getDouble(5),
                            rs.getInt(6),
                            rs.getString(7),
                            rs.getInt(8)
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
     * Gets table index based on what row the user clicked on
     * @param event
     */
    @FXML
    void onTVBookingsClicked(MouseEvent event) {
        if(tvBookings.getSelectionModel().getSelectedItem() != null)
        {
            selectedBookingIndex = tvBookings.getSelectionModel().getSelectedIndex();
            btnBookingDelete.setDisable(false);
            btnBookingEdit.setDisable(false);
        }
    }

    /**
     * Open a form that allows users to edit selected agent information and update it in the database
     */
    private void openDialog(boolean isEdit)
    {
        //loads edit agent dialog
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(bookingAddModFXML));
        Parent parent = null; //set parent variable to be called later in the dialog

        try
        {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //add and edit bookings dialog controller
        BookingAddUpdateController addUpdateController = fxmlLoader.getController();

        //transfer data information to add/edit bookings dialog
        addUpdateController.setObservableList(data);


        //Display selected booking information if user is modifying
        if (isEdit == true)
            addUpdateController.initBookings(selectedBookingIndex);

        Scene scene = new Scene(parent);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Booking Form");// set title for edit bookings dialog
        stage.setScene(scene);
        stage.showAndWait(); // display edit bookings dialog

        getBookings();
    }
}

