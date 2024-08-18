package com.search.vehicle.dto;


import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Represents a car model - V1.
 * @author Vansh Pratap Singh
 */
public class CarDtoV1 extends CarBaseDtoV1 {

    @NotEmpty(message = "Model must not be empty.")
    private String model;

    @NotEmpty(message = "Brand must not be empty.")
    private String brand;

    private Double kmsDriven;

    @NotEmpty(message = "Transmission must not be empty.")
    private String transmission;

    private List<String> features;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getKmsDriven() {
        return kmsDriven;
    }

    public void setKmsDriven(Double kmsDriven) {
        this.kmsDriven = kmsDriven;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

}
