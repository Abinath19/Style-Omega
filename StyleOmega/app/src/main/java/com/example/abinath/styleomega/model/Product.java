package com.example.abinath.styleomega.model;

/**
 * Created by Abinath on 06-Sep-17.
 */

public class Product {
    private int id;
    private String name;
    private double price;
    private String image;
    private String gender;
    private String type;
    private int quantity;

    public Product(int id, String name, double price, String image, String gender, String type, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.gender = gender;
        this.type = type;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
