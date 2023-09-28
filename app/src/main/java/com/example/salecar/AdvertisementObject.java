package com.example.salecar;

import android.content.Intent;

public class AdvertisementObject {
    protected Integer id;
    protected Integer user_id;
    protected String brand;
    protected String model;
    protected String manufactured;
    protected String year;
    protected String plate;
    protected String mileage;
    protected Integer functioning;
    protected Integer esthetic;
    protected String image1;

    protected String price;

    public AdvertisementObject(Integer id, Integer user_id, String brand, String model, String manufactured, String year, String plate, String mileage, Integer functioning, Integer esthetic, String image1, String price) {
        this.id = id;
        this.user_id = user_id;
        this.brand = brand;
        this.model = model;
        this.manufactured = manufactured;
        this.year = year;
        this.plate = plate;
        this.mileage = mileage;
        this.functioning = functioning;
        this.esthetic = esthetic;
        this.image1 = image1;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufactured() {
        return manufactured;
    }

    public void setManufactured(String manufactured) {
        this.manufactured = manufactured;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Integer getFunctioning() {
        return functioning;
    }

    public void setFunctioning(Integer functioning) {
        this.functioning = functioning;
    }

    public Integer getEsthetic() {
        return esthetic;
    }

    public void setEsthetic(Integer esthetic) {
        this.esthetic = esthetic;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
