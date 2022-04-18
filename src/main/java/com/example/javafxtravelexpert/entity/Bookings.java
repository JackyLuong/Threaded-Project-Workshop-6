package com.example.javafxtravelexpert.entity;

import java.util.Date;

public class Bookings
{
    private Integer id;
    private Date bookingDate;
    private String bookingNo;
    private Double bookingTotal;
    private Double travelerCount;
    private Integer customer;
    private String tripType;
    private Integer _package;

    public Bookings(Integer id, Date bookingDate, String bookingNo, Double bookingTotal, Double travelerCount, Integer customer, String tripType, Integer _package) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.bookingNo = bookingNo;
        this.bookingTotal = bookingTotal;
        this.travelerCount = travelerCount;
        this.customer = customer;
        this.tripType = tripType;
        this._package = _package;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public Double getBookingTotal() {
        return bookingTotal;
    }

    public void setBookingTotal(Double bookingTotal) {
        this.bookingTotal = bookingTotal;
    }

    public Double getTravelerCount() {
        return travelerCount;
    }

    public void setTravelerCount(Double travelerCount) {
        this.travelerCount = travelerCount;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public Integer get_package() {
        return _package;
    }

    public void set_package(Integer _package) {
        this._package = _package;
    }
    @Override
    public String toString() {
        return bookingDate.toString() + " total: " + getBookingTotal();
    }
}
