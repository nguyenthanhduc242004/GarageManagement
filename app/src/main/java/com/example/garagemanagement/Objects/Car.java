package com.example.garagemanagement.Objects;

import android.graphics.Picture;

import java.util.Date;

public class Car {
    private String ownerName;
    private String licensePlate;
    private String carBrand;
    private String address;
    private String phoneNumber;
    private Date receiveDate;
    private int picture;
    private int status;

    public Car(String ownerName, String licensePlate, String carBrand, String address, String phoneNumber, Date receiveDate, int picture, int status) {
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
        this.carBrand = carBrand;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.receiveDate = receiveDate;
        this.picture = picture;
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
