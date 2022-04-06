package com.example.javafxtravelexpert.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionMngr {
    //DB connection instance
    private static DBConnectionMngr instance = null;

    //set default constructor to private to restrict object creation. Object creation Only via the static method.
    private DBConnectionMngr(){}

    public static DBConnectionMngr getInstance(){
        if(instance == null){
            instance = new DBConnectionMngr();
        }return instance;
    }

    public Connection getConnection(String url, String user, String pwd){
        Connection conn = null;
        try {

            conn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        }

        return conn;
    }

}
