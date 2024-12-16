package com.example.garagemanagement.Objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Car {
    public static final int STATE_NEW = 0;
    public static final int STATE_REPAIRING = 1;
    public static final int STATE_COMPLETED = 2;

    private String ownerName;
    private String licensePlate;
    private String carBrand;
    private String carType;
    private String phoneNumber;
    private Date receiveDate;
    private int carImage;
    private int state;
    private String[] carServices;
    private String[] carSupplies;

    public Car(String ownerName, String licensePlate, String carBrand, String carType, String phoneNumber, Date receiveDate, int carImage, int state) {
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
        this.carBrand = carBrand;
        this.carType = carType;
        this.phoneNumber = phoneNumber;
        this.receiveDate = receiveDate;
        this.carImage = carImage;
        this.state = state;
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public int getCarImage() {
        return carImage;
    }

    public void setCarImage(int carImage) {
        this.carImage = carImage;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String[] getCarServices() {
        return carServices;
    }

    public void setCarServices(String[] carServices) {
        this.carServices = carServices;
    }

    public String[] getCarSupplies() {
        return carSupplies;
    }

    public void setCarSupplies(String[] carSupplies) {
        this.carSupplies = carSupplies;
    }
}
