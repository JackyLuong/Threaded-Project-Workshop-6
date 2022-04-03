package com.example.javafxtravelexpert.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TravelExpertsProperties {

    //declare private variables
    private String databaseURL;
    private String databaseUser;
    private String databasePwd;
    private String databaseURLArchive;



    //constructor
    public TravelExpertsProperties(){

        Properties prop = new Properties(); // instantiate property obj

        try {

            InputStream input = new FileInputStream("src/main/resources/properties/travelExpertsProps.properties"); //instantiate input file input ob
            prop.load(input);// load the property file to prop obj

            //assign property value to instance variables
            this.databaseURL = prop.getProperty("dbUrl");
            this.databaseUser = prop.getProperty("dbUser");
            this.databasePwd = prop.getProperty("dbPwd");
            this.databaseURLArchive = prop.getProperty("dbUrlArchive");
        } catch (FileNotFoundException fileErr) {
            fileErr.printStackTrace();
        } catch (IOException fileErr2) {
            fileErr2.printStackTrace();
        }

    }

    //getter methods

    public String getDatabaseURL() {
        return databaseURL;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePwd() {
        return databasePwd;
    }

    public String getDatabaseURLArchive() { return databaseURLArchive;}
}
