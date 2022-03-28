package com.example.javafxtravelexpert.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customers {

    private SimpleIntegerProperty custId;
    private SimpleStringProperty custFirstName;
    private SimpleStringProperty custLastName;
    private SimpleStringProperty custAddress;
    private SimpleStringProperty custCity;
    private SimpleStringProperty custProvince;
    private SimpleStringProperty custPostal;
    private SimpleStringProperty custCountry;
    private SimpleStringProperty custHomeNum;
    private SimpleStringProperty custBusNum;
    private SimpleStringProperty custEmail;
    private SimpleIntegerProperty custAgentId;

    public Customers(int custId, String custFirstName,
                     String custLastName, String custAddress,
                     String custCity, String custProvince,
                     String custPostal, String custCountry,
                     String custHomeNum, String custBusNum,
                     String custEmail,int custAgentId) {
        this.custId = new SimpleIntegerProperty(custId);
        this.custFirstName = new SimpleStringProperty(custFirstName);
        this.custLastName = new SimpleStringProperty(custLastName);
        this.custAddress = new SimpleStringProperty(custAddress);
        this.custCity = new SimpleStringProperty(custCity);
        this.custProvince = new SimpleStringProperty(custProvince);
        this.custPostal = new SimpleStringProperty(custPostal);
        this.custCountry = new SimpleStringProperty(custCountry);
        this.custHomeNum = new SimpleStringProperty(custHomeNum);
        this.custBusNum = new SimpleStringProperty(custBusNum);
        this.custAgentId = new SimpleIntegerProperty(custAgentId);
        this.custEmail = new SimpleStringProperty(custEmail);
    }

    public int getCustId() {
        return custId.get();
    }

    public SimpleIntegerProperty custIdProperty() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId.set(custId);
    }

    public String getCustFirstName() {
        return custFirstName.get();
    }

    public SimpleStringProperty custFirstNameProperty() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName.set(custFirstName);
    }

    public String getCustLastName() {
        return custLastName.get();
    }

    public SimpleStringProperty custLastNameProperty() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName.set(custLastName);
    }

    public String getCustAddress() {
        return custAddress.get();
    }

    public SimpleStringProperty custAddressProperty() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress.set(custAddress);
    }

    public String getCustCity() {
        return custCity.get();
    }

    public SimpleStringProperty custCityProperty() {
        return custCity;
    }

    public void setCustCity(String custCity) {
        this.custCity.set(custCity);
    }

    public String getCustProvince() {
        return custProvince.get();
    }

    public SimpleStringProperty custProvinceProperty() {
        return custProvince;
    }

    public void setCustProvince(String custProvince) {
        this.custProvince.set(custProvince);
    }

    public String getCustPostal() {
        return custPostal.get();
    }

    public SimpleStringProperty custPostalProperty() {
        return custPostal;
    }

    public void setCustPostal(String custPostal) {
        this.custPostal.set(custPostal);
    }

    public String getCustCountry() {
        return custCountry.get();
    }

    public SimpleStringProperty custCountryProperty() {
        return custCountry;
    }

    public void setCustCountry(String custCountry) {
        this.custCountry.set(custCountry);
    }

    public String getCustHomeNum() {
        return custHomeNum.get();
    }

    public SimpleStringProperty custHomeNumProperty() {
        return custHomeNum;
    }

    public void setCustHomeNum(String custHomeNum) {
        this.custHomeNum.set(custHomeNum);
    }

    public String getCustBusNum() {
        return custBusNum.get();
    }

    public SimpleStringProperty custBusNumProperty() {
        return custBusNum;
    }

    public void setCustBusNum(String custBusNum) {
        this.custBusNum.set(custBusNum);
    }

    public String getCustEmail() {
        return custEmail.get();
    }

    public SimpleStringProperty custEmailProperty() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail.set(custEmail);
    }

    public int getCustAgentId() {
        return custAgentId.get();
    }

    public SimpleIntegerProperty custAgentIdProperty() {
        return custAgentId;
    }

    public void setCustAgentId(int custAgentId) {
        this.custAgentId.set(custAgentId);
    }
}
