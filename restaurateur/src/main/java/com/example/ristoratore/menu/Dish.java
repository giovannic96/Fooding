package com.example.ristoratore.menu;

import android.widget.ImageView;

public class Dish {

    private String name;
    private String description;
    private ImageView photo;
    private float price;
    private int available_qty;

    public Dish(String name, String description, ImageView photo, float price, int available_qty) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.available_qty = available_qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(int available_qty) {
        this.available_qty = available_qty;
    }
}
