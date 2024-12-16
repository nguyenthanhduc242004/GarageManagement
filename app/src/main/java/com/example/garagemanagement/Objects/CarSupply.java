package com.example.garagemanagement.Objects;

public class CarSupply {
    private String supplyId;
    private String supplyName;
    private long price;
    private int quantity = 0;

    public CarSupply(String supplyId, String supplyName, long price) {
        this.supplyId = supplyId;
        this.supplyName = supplyName;
        this.price = price;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
