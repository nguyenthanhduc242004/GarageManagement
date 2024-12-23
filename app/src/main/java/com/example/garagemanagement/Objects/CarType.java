package com.example.garagemanagement.Objects;

import java.io.Serializable;

public class CarType {
    private String carTypeId;
    private String carTypeText;

    public CarType() {
    }

    public CarType(String carTypeId, String carTypeText) {
        this.carTypeId = carTypeId;
        this.carTypeText = carTypeText;
    }

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCarTypeText() {
        return carTypeText;
    }

    public void setCarTypeText(String carTypeText) {
        this.carTypeText = carTypeText;
    }
}
