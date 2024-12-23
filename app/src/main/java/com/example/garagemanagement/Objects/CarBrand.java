package com.example.garagemanagement.Objects;

import java.io.Serializable;

public class CarBrand {
    private String carBrandId;
    private String carBrandText;

    public CarBrand() {
    }

    public CarBrand(String carBrandId, String carBrandText) {
        this.carBrandId = carBrandId;
        this.carBrandText = carBrandText;
    }

    public String getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getCarBrandText() {
        return carBrandText;
    }

    public void setCarBrandText(String carBrandText) {
        this.carBrandText = carBrandText;
    }
}
