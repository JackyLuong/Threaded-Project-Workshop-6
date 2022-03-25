package com.example.threaded_project_workshop_6;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Date;

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
}
