package com.search.vehicle.dto;

/**
 * Represents a car model - V2.
 * @author Vansh Pratap Singh
 */
public class CarDtoV2 extends CarDtoV1 {

    private String id;

    public CarDtoV2() {}

    public CarDtoV2(String id, CarDtoV1 carDtoV1) {

        this.id = id;
        this.setModel(carDtoV1.getModel());
        this.setBrand(carDtoV1.getBrand());
        this.setKmsDriven(carDtoV1.getKmsDriven());
        this.setTransmission(carDtoV1.getTransmission());
        this.setFeatures(carDtoV1.getFeatures());

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
