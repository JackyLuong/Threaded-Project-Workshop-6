package com.example.threaded_project_workshop_6;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Package
{
    private SimpleIntegerProperty packageId;
    private SimpleStringProperty pkgName;
    private Date pkgStartDate;
    private Date pkgEndDate;
    private SimpleStringProperty pkgDesc;
    private SimpleDoubleProperty pkgBasePrice;
    private SimpleDoubleProperty pkgAgencyCommission;

    public Package(int packageId, String pkgName,
                   Date pkgStartDate, Date pkgEndDate, String pkgDesc,
                   double pkgBasePrice, double pkgAgencyCommission)
    {
        this.packageId = new SimpleIntegerProperty(packageId);
        this.pkgName = new SimpleStringProperty(pkgName);
        this.pkgStartDate = pkgStartDate;
        this.pkgEndDate = pkgEndDate;
        this.pkgDesc = new SimpleStringProperty(pkgDesc);
        this.pkgBasePrice = new SimpleDoubleProperty(pkgBasePrice);
        this.pkgAgencyCommission = new SimpleDoubleProperty(pkgAgencyCommission);
    }

    public int getPackageId() {
        return packageId.get();
    }

    public SimpleIntegerProperty packageIdProperty() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId.set(packageId);
    }

    public String getPkgName() {
        return pkgName.get();
    }

    public SimpleStringProperty pkgNameProperty() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName.set(pkgName);
    }

    public Date getPkgStartDate() {
        return pkgStartDate;
    }

    public void setPkgStartDate(Date pkgStartDate) {
        this.pkgStartDate = pkgStartDate;
    }

    public Date getPkgEndDate() {
        return pkgEndDate;
    }

    public void setPkgEndDate(Date pkgEndDate) {
        this.pkgEndDate = pkgEndDate;
    }

    public String getPkgDesc() {
        return pkgDesc.get();
    }

    public SimpleStringProperty pkgDescProperty() {
        return pkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        this.pkgDesc.set(pkgDesc);
    }

    public double getPkgBasePrice() {
        return pkgBasePrice.get();
    }

    public SimpleDoubleProperty pkgBasePriceProperty() {
        return pkgBasePrice;
    }

    public void setPkgBasePrice(double pkgBasePrice) {
        this.pkgBasePrice.set(pkgBasePrice);
    }

    public double getPkgAgencyCommission() {
        return pkgAgencyCommission.get();
    }

    public SimpleDoubleProperty pkgAgencyCommissionProperty() {
        return pkgAgencyCommission;
    }

    public void setPkgAgencyCommission(double pkgAgencyCommission) {
        this.pkgAgencyCommission.set(pkgAgencyCommission);
    }
}
