package com.example.garagemanagement.Objects;

import java.io.Serializable;
import java.util.Map;

public class CarService implements Serializable {
    private String serviceId;
    private String serviceName;
    private Map<String, Long> prices;

    public CarService() {
    }

    public CarService(String serviceId, String serviceName, Map<String, Long> prices) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.prices = prices;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, Long> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Long> prices) {
        this.prices = prices;
    }
}
