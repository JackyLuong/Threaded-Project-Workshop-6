package com.example.javafxtravelexpert.controllers;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.example.javafxtravelexpert.entity.Bookings;
import com.example.javafxtravelexpert.utils.DBConnectionMngr;
import com.example.javafxtravelexpert.utils.TravelExpertsProperties;
import com.example.javafxtravelexpert.utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BookingAddUpdateController {

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
    private TextField tfBookingDate;

    @FXML
    private TextField tfBookingNo;

    @FXML
    private TextField tfBookingTotal;

    @FXML
    private TextField tfCustomerId;

    @FXML
    private TextField tfBookingsId;

    @FXML
    private TextField tfTravelerCount;

    @FXML
    private TextField tfTripTypeId;

    @FXML
    private TextField tfPackageId;

    private ObservableList<Bookings> mainObservableList = FXCollections.observableArrayList();

    private int selectedBookingsIndex;

    private String[] connectionCredentials;

    private boolean isEdit = false;
    @FXML
    void initialize() {
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert pane != null : "fx:id=\"pane\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfBookingDate != null : "fx:id=\"tfBookingDate\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfBookingNo != null : "fx:id=\"tfBookingNo\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfBookingTotal != null : "fx:id=\"tfBookingTotal\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfCustomerId != null : "fx:id=\"tfCustomerId\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfBookingsId != null : "fx:id=\"tfBookingsId\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfTravelerCount != null : "fx:id=\"tfTravelerCount\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfTripTypeId != null : "fx:id=\"tfTripTypeId\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";
        assert tfPackageId != null : "fx:id=\"tfPackageId\" was not injected: check your FXML file 'bookings-add-update-view.fxml'.";

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
                    modifyBookings(mouseEvent);
                else
                    addBookings(mouseEvent);
            }
        });
    }

    @FXML
    void onTextChanged(KeyEvent event)
    {
        if(Validator.isPresentWithoutMessage(tfTravelerCount) &&
                Validator.isPresentWithoutMessage(tfPackageId) &&
                Validator.isDoublePositiveWithoutMessage(tfTravelerCount) &&
                Validator.isIntPositiveWithoutMessage(tfPackageId))
        {
            double bookingTotal = calculateBookingsTotal();
            tfBookingTotal.setText(Double.toString(bookingTotal));
        }
    }

    /**
     * method to receive to the observable list in main controller
     * @param data
     */
    public void setObservableList(ObservableList<Bookings> data)
    {
        mainObservableList = data;
    }

    /**
     * method that retrieves the selected Bookings and displays information on the corresponding text fields
     */
    public void initBookings(int selectedBookingsIndex)
    {
        isEdit = true; //change form to update the selected Bookings

        this.selectedBookingsIndex = selectedBookingsIndex;
        Bookings selectedBookings = mainObservableList.get(selectedBookingsIndex);

        tfBookingDate.setText(selectedBookings.getBookingDate().toString());
        tfBookingNo.setText(selectedBookings.getBookingNo());
        tfBookingTotal.setText(selectedBookings.getBookingTotal().toString());
        tfTravelerCount.setText(selectedBookings.getTravelerCount().toString());
        tfCustomerId.setText(selectedBookings.getCustomer().toString());
        tfTripTypeId.setText(selectedBookings.getTripType());
        tfPackageId.setText(selectedBookings.get_package().toString());
    }

    /**
     * Modify selected bookings and update database
     * @param mouseEvent
     */
    private void modifyBookings(MouseEvent mouseEvent)
    {
        //Validates data before modifying the database
        if (Validator.isPresent(tfBookingDate) &&
                Validator.isPresent(tfBookingNo) &&
                Validator.isIntPositive(tfCustomerId) &&
                Validator.isPresent(tfTripTypeId) &&
                Validator.isIntPositive(tfPackageId) &&
                Validator.isDoublePositive(tfBookingTotal) &&
                Validator.isDoublePositive(tfTravelerCount))
        {
            //load the bookings from the database
            DBConnectionMngr cm = DBConnectionMngr.getInstance(); // get connection obj
            TravelExpertsProperties prop = new TravelExpertsProperties(); // instantiate property obj

            try {
                Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(),  prop.getDatabasePwd()); // connect to database

                //SQL String to add bookings to data
                String sql = "UPDATE `bookings` " +
                             "SET `BookingDate`=?," +
                             "`BookingNo`=?," +
                             "`BookingTotal`=?," +
                             "`TravelerCount`=?," +
                             "`CustomerId`=?," +
                             "`TripTypeId`=?," +
                             "`PackageId`=? WHERE `BookingId`=?";

                PreparedStatement stmt = conn.prepareStatement(sql);

                Date bookingDate = Date.valueOf(tfBookingDate.getText());

                //selected booking
                Bookings selectedBooking = mainObservableList.get(selectedBookingsIndex);

                //Assign values for question marks
                stmt.setDate(1, bookingDate);
                stmt.setString(2, tfBookingNo.getText());
                stmt.setDouble(3, Double.parseDouble(tfBookingTotal.getText()));
                stmt.setDouble(4, Double.parseDouble(tfTravelerCount.getText()));
                stmt.setInt(5, Integer.parseInt(tfCustomerId.getText()));
                stmt.setString(6, tfTripTypeId.getText());
                stmt.setInt(7, Integer.parseInt(tfPackageId.getText()));
                stmt.setInt(8, selectedBooking.getId());

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
     * add bookings and update database
     * @param mouseEvent
     */
    private void addBookings(MouseEvent mouseEvent)
    {
        //Validates data before modifying the database
        if (Validator.isPresent(tfBookingDate) &&
                Validator.isPresent(tfBookingNo) &&
                Validator.isIntPositive(tfCustomerId) &&
                Validator.isPresent(tfTripTypeId) &&
                Validator.isIntPositive(tfPackageId) &&
                Validator.isDoublePositive(tfBookingTotal) &&
                Validator.isDoublePositive(tfTravelerCount))
        {
            //load the bookings from the database
            DBConnectionMngr cm = DBConnectionMngr.getInstance(); // get connection obj
            TravelExpertsProperties prop = new TravelExpertsProperties(); // instantiate property obj

            try {
                Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(),  prop.getDatabasePwd()); // connect to database
                //SQL String to add bookings to data
                String sql = "INSERT INTO `bookings`" +
                             "(`BookingId`, " +
                             "`BookingDate`, " +
                             "`BookingNo`, " +
                             "`BookingTotal`, " +
                             "`TravelerCount`, " +
                             "`CustomerId`, " +
                             "`TripTypeId`, " +
                             "`PackageId`) " +
                             "VALUES (null,?,?,?,?,?,?,?)";

                PreparedStatement stmt = conn.prepareStatement(sql);

                Date bookingDate = Date.valueOf(tfBookingDate.getText());

                //selected booking
                Bookings selectedBooking = mainObservableList.get(selectedBookingsIndex);

                //Assign values for question marks
                stmt.setDate(1, bookingDate);
                stmt.setString(2, tfBookingNo.getText());
                stmt.setDouble(3, Double.parseDouble(tfBookingTotal.getText()));
                stmt.setDouble(4, Double.parseDouble(tfTravelerCount.getText()));
                stmt.setInt(5, Integer.parseInt(tfCustomerId.getText()));
                stmt.setString(6, tfTripTypeId.getText());
                stmt.setInt(7, Integer.parseInt(tfPackageId.getText()));

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

    /**
     * Calculate bookingsTotal
     */
    private double calculateBookingsTotal() {
        //booking total
        double bookingTotal = 0;
        double packageBasePrice = 0;
        DBConnectionMngr cm = DBConnectionMngr.getInstance(); // get connection obj
        TravelExpertsProperties prop = new TravelExpertsProperties(); // instantiate property obj

        Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(),  prop.getDatabasePwd()); // connect to database
        try
        {
            //get package base price
            String getPackageBasePrice = "SELECT PkgBasePrice FROM packages WHERE PackageId =?";
            PreparedStatement basePriceStmt = conn.prepareStatement(getPackageBasePrice);
            basePriceStmt.setInt(1, Integer.parseInt(tfPackageId.getText()));
            ResultSet resultSet = basePriceStmt.executeQuery();

            //Calculate bookingTotal
            if(resultSet.next())
            {
                packageBasePrice = resultSet.getInt(1); //package base price
            }
            else
            {
                packageBasePrice = 0;
            }
            double travelerCount = Double.parseDouble(tfTravelerCount.getText());
            bookingTotal = packageBasePrice * travelerCount;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return bookingTotal;
    }
}
