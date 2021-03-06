package com.example.javafxtravelexpert.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class Validator
{
    /**
     * Checks if the text field contains a value
     * @param tf
     * @return true if present
     */
    public static boolean isPresent(TextField tf)
    {
        boolean result = true;

        //value is null or empty
        if(tf.getText() == null || tf.getText().isEmpty())
        {
            result = false;
            Alert a = new Alert(Alert.AlertType.ERROR); //prints error message
            a.setTitle(tf.getId() + " is not filled");
            a.setContentText("Missing value for " + tf.getId());
            a.show();
        }

        return result;
    }
    /**
     * Checks if the text field contains a value and doesn't display an alert
     * @param tf
     * @return true if present
     */
    public static boolean isPresentWithoutMessage(TextField tf)
    {
        boolean result = true;

        //value is null or empty
        if(tf.getText() == null || tf.getText().isEmpty())
        {
            result = false;
        }

        return result;
    }

    /**
     * Checks if a double value is positive
     * @param tf
     * @return
     */
    public static boolean isDoublePositive(TextField tf)
{
    boolean result = true;
    //value isnt a double
    if(!tryParseDouble(tf.getText()))
    {

        result = false;
        Alert a = new Alert(Alert.AlertType.ERROR); // prints error message
        a.setTitle(tf.getId() + " Invalid Input");
        a.setContentText("Value must be a number in " + tf.getId());
        a.show();
    }
    //value is negative
    else if(Double.parseDouble(tf.getText()) < 0)
    {
        result = false;
        Alert a = new Alert(Alert.AlertType.ERROR); // prints error message
        a.setTitle(tf.getId() + " Invalid Input");
        a.setContentText("Value must be a positive number in " + tf.getId());
        a.show();
    }
    return result;
}

    /**
     * Checks if double is positive but doesnt display alert
     * @param tf
     * @return
     */
    public static boolean isDoublePositiveWithoutMessage(TextField tf)
    {
        boolean result = true;
        //value isnt a double
        if(!tryParseDouble(tf.getText()))
        {
            result = false;
        }
        //value is negative
        else if(Double.parseDouble(tf.getText()) < 0)
        {
            result = false;
        }
        return result;
    }

    /**
     * Checks if a double value is in range
     * @param tf
     * @return
     */
    public static boolean isDoubleInRange(double min, double max, TextField tf)
    {
        boolean result = true;
        //value isnt a double
        if(!tryParseDouble(tf.getText()))
        {

            result = false;
            Alert a = new Alert(Alert.AlertType.ERROR); // prints error message
            a.setTitle(tf.getId() + " Invalid Input");
            a.setContentText("Value must be a number in " + tf.getId());
            a.show();
        }
        //value is out of range
        else if(Double.parseDouble(tf.getText()) > max)
        {
            result = false;
            Alert a = new Alert(Alert.AlertType.ERROR); // prints error message
            a.setTitle(tf.getId() + " Invalid Input");
            a.setContentText("Agency accomodation must be less than base price");
            a.show();
        }
        return result;
    }

    /**
     * Checks if a int value is positive
     * @param tf
     * @return
     */
    public static boolean isIntPositive(TextField tf)
    {
        boolean result = true;
        //value isnt a int
        if(!tryParseInt(tf.getText()))
        {

            result = false;
            Alert a = new Alert(Alert.AlertType.ERROR); // prints error message
            a.setTitle(tf.getId() + " Invalid Input");
            a.setContentText("Value must be a number in " + tf.getId());
            a.show();
        }
        //value is negative
        else if(Integer.parseInt(tf.getText()) < 0)
        {
            result = false;
            Alert a = new Alert(Alert.AlertType.ERROR); // prints error message
            a.setTitle(tf.getId() + " Invalid Input");
            a.setContentText("Value must be a positive number in " + tf.getId());
            a.show();
        }
        return result;
    }

    /**
     * Checks if int is positive but doesnt display alert
     * @param tf
     * @return
     */
    public static boolean isIntPositiveWithoutMessage(TextField tf)
    {
        boolean result = true;
        //value isnt a int
        if(!tryParseInt(tf.getText()))
        {
            result = false;
        }
        //value is negative
        else if(Integer.parseInt(tf.getText()) < 0)
        {
            result = false;
        }
        return result;
    }
    /**
     * Checks if the start date is a valid entry
     * @param tf
     * @return
     */
    public static boolean isStartDateValid(TextField tf)
    {

        boolean result = true;
        //convert string to date
        try
        {
            //convert start date string to date object
            Date startDate = Date.valueOf(tf.getText());

            //compare start date with current date
            Date today = new Date(System.currentTimeMillis());

            //start value is before current date
            if(startDate.before(today))
            {
                result = false;
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle(tf.getId() + " Invalid start date");
                a.setContentText("Start date can not be before today." + tf.getId());
                a.show();
            }

        }
        catch (IllegalArgumentException ie) // failed to convert start date to date type object
        {
            result = false;
            Alert a = new Alert(Alert.AlertType.ERROR); // print error
            a.setTitle(tf.getId() + " Invalid Input");
            a.setContentText("Value must be a valid date in" + tf.getId());
            a.show();
        }
        return result;
    }

    /**
     * Checks if end date is a valid entry
     * @param tfStartDate
     * @param tfEndDate
     * @return
     */
    public static boolean isEndDateValid(TextField tfStartDate, TextField tfEndDate)
    {
        boolean result = true;
        try
        {
            //convert start date string to date type object
            Date startDate = Date.valueOf(tfStartDate.getText());
            //convert end date string to date type object
            Date endDate = Date.valueOf(tfEndDate.getText());

            //end date is before start date
            if(endDate.before(startDate))
            {
                result = false;
                Alert a = new Alert(Alert.AlertType.ERROR); // print error
                a.setTitle(tfEndDate.getId() + " Invalid end date");
                a.setContentText("End date can not be before start date.");
                a.show();
            }
        }
        catch (IllegalArgumentException ie) // failed to convert string objects to date type objects
        {
            result = false;
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Invalid dates");
            a.setContentText("start date and end date must be valid dates");
            a.show();
        }
        return result;
    }

    /**
     * Checks if a string object can convert to a double object
     * Inspired by a solution in stackoverflow
     * url: https://stackoverflow.com/questions/8391979/does-java-have-a-int-tryparse-that-doesnt-throw-an-exception-for-bad-data
     * @param value
     * @return
     */
    private static boolean tryParseDouble (String value)
    {
        boolean result = true;
        try
        {
            //parse string to double
            double doubleValue = Double.parseDouble(value);
        }
        catch (NumberFormatException e) // failed to parse string to double
        {
            result = false;
        }

        return result;
    }
    /**
     * Validate the email and password against the DB data.
     * @param usr, passwd
     * @return true if authenticated
     */
    public static boolean isAuthenticated(TextField usr, TextField passwd){
        boolean isValidUser = true;

        //initiate DB connection
        DBConnectionMngr cm = DBConnectionMngr.getInstance();
        TravelExpertsProperties prop = new TravelExpertsProperties();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String psql = "SELECT COUNT(*) FROM AGENTS WHERE AGTEMAIL = ? and AGTPASSWORD =?";
        Connection conn = cm.getConnection(prop.getDatabaseURL(), prop.getDatabaseUser(),prop.getDatabasePwd());
        int count = 0;

        if(conn != null){
            try{

                pstmt = conn.prepareStatement(psql);
                pstmt.setString(1, usr.getText());
                pstmt.setString(2, passwd.getText());
                rs = pstmt.executeQuery();
                while (rs.next()){
                    count = rs.getInt(1);
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException sqlErr) {
                sqlErr.printStackTrace();
            }
        }

        if (count != 1){
            isValidUser = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            usr.clear();
            passwd.clear();
            usr.requestFocus();
            alert.setTitle("Invalid Credentials");
            alert.setHeaderText("Login Failed!");
            alert.setContentText("Incorrect Email and/or Password. Please try again");
            alert.showAndWait();
        }
        return  isValidUser;
    }

    /**
     * Checks if a string object can convert to a int object
     * Inspired by a solution in stackoverflow
     * url: https://stackoverflow.com/questions/8391979/does-java-have-a-int-tryparse-that-doesnt-throw-an-exception-for-bad-data
     * @param value
     * @return
     */
    private static boolean tryParseInt (String value)
    {
        boolean result = true;
        try
        {
            //parse string to double
            int intValue = Integer.parseInt(value);
        }
        catch (NumberFormatException e) // failed to parse string to double
        {
            result = false;
        }

        return result;
    }

    /**
     * Checks if the login credentials are present
     * @param text
     * @return true if present
     */
    public static boolean isLoginDataPresent(TextField text, Label label){
        boolean isValid = true;
        if(text.getText().isEmpty()){
            isValid = false;
            label.setText(text.getAccessibleText()+ " is required.");
            text.getStyleClass().add("textError");
            text.requestFocus();

        }
        return isValid;
    }



}
