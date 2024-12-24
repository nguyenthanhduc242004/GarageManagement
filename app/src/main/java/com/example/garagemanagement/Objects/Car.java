package com.example.garagemanagement.Objects;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Car {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final int STATE_NEW = 0;
    public static final int STATE_REPAIRING = 1;
    public static final int STATE_COMPLETED = 2;

    private String carId;
    private String ownerName;
    private String licensePlate;
    private String carBrandId;
    private String carBrandText;
    private String carTypeId;
    private String carTypeText;
    private String phoneNumber;
    private Date receiveDate;
    private int carImage;
    private int state;
    private List<CarService> carServiceList = new ArrayList<>();
    private List<CarSupply> carSupplyList = new ArrayList<>();
    private List<String> carServices= new ArrayList<>();
    private Map<String, Integer> carSupplies = new HashMap<>();
    private Date paymentDate;

    public Car() {
    }

    public Car(String ownerName, String licensePlate, String carBrandId, String carBrandText, String carTypeId, String carTypeText, String phoneNumber, Date receiveDate, int carImage, int state) {
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
        this.carBrandId = carBrandId;
        this.carBrandText = carBrandText;
        this.carTypeId = carTypeId;
        this.carTypeText = carTypeText;
        this.phoneNumber = phoneNumber;
        this.receiveDate = receiveDate;
        this.carImage = carImage;
        this.state = state;
    }

    public Car(String ownerName, String licensePlate, String carBrandId, String carBrandText, String carTypeId, String carTypeText, String phoneNumber, Date receiveDate, int carImage, int state, List<CarService> carServiceList, List<CarSupply> carSupplyList) {
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
        this.carBrandId = carBrandId;
        this.carBrandText = carBrandText;
        this.carTypeId = carTypeId;
        this.carTypeText = carTypeText;
        this.phoneNumber = phoneNumber;
        this.receiveDate = receiveDate;
        this.carImage = carImage;
        this.state = state;
        this.carServiceList = carServiceList;
        this.carSupplyList = carSupplyList;
    }

    public Car(String carId, String ownerName, String licensePlate, String carBrandId, String carBrandText, String carTypeId, String carTypeText, String phoneNumber, Date receiveDate, int carImage, int state, List<String> carServices, Map<String, Integer> carSupplies) {
        this.carId = carId;
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
        this.carBrandId = carBrandId;
        this.carBrandText = carBrandText;
        this.carTypeId = carTypeId;
        this.carTypeText = carTypeText;
        this.phoneNumber = phoneNumber;
        this.receiveDate = receiveDate;
        this.carImage = carImage;
        this.state = state;
        this.carServices = carServices;
        this.carSupplies = carSupplies;
    }

    public Car(String carId, String ownerName, String licensePlate, String carBrandId, String carBrandText, String carTypeId, String carTypeText, String phoneNumber, Date receiveDate, int carImage, int state, List<String> carServices, Map<String, Integer> carSupplies, Date paymentDate) {
        this.carId = carId;
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
        this.carBrandId = carBrandId;
        this.carBrandText = carBrandText;
        this.carTypeId = carTypeId;
        this.carTypeText = carTypeText;
        this.phoneNumber = phoneNumber;
        this.receiveDate = receiveDate;
        this.carImage = carImage;
        this.state = state;
        this.carServices = carServices;
        this.carSupplies = carSupplies;
        this.paymentDate = paymentDate;
    }

    public Car(String carId, String ownerName, String licensePlate, String carBrandId, String carTypeId, String phoneNumber, Date receiveDate, int carImage, int state, List<String> carServices, Map<String, Integer> carSupplies, Date paymentDate) {
        this.carId = carId;
        this.ownerName = ownerName;
        this.licensePlate = licensePlate;
        this.carBrandId = carBrandId;
        this.carTypeId = carTypeId;
        this.phoneNumber = phoneNumber;
        this.receiveDate = receiveDate;
        this.carImage = carImage;
        this.state = state;
        this.carServices = carServices;
        this.carSupplies = carSupplies;
        this.paymentDate = paymentDate;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
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

    public String getCarBrandText() {
        return carBrandText;
    }

    public void setCarBrandText(String carBrandText) {
        this.carBrandText = carBrandText;
    }

    public String getCarTypeText() {
        return carTypeText;
    }

    public void setCarTypeText(String carTypeText) {
        this.carTypeText = carTypeText;
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

    public List<CarService> getCarServiceList() {
        return carServiceList;
    }

    public void setCarServiceList(List<CarService> carServiceList) {
        this.carServiceList = carServiceList;
    }

    public List<CarSupply> getCarSupplyList() {
        return carSupplyList;
    }

    public void setCarSupplyList(List<CarSupply> carSupplyList) {
        this.carSupplyList = carSupplyList;
    }

    public String getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public List<String> getCarServices() {
        return carServices;
    }

    public void setCarServices(List<String> carServices) {
        this.carServices = carServices;
    }

    public Map<String, Integer> getCarSupplies() {
        return carSupplies;
    }

    public void setCarSupplies(Map<String, Integer> carSupplies) {
        this.carSupplies = carSupplies;
    }
}
