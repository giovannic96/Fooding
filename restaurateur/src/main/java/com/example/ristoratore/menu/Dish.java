package com.example.ristoratore.menu;

import android.widget.ImageView;

public class Dish {

    private String description;
    private ImageView photo;
    private float price;
    private int available_qty;

    public Dish() {
        this.description = "";
        this.photo = null;
        this.price = -1;
        this.available_qty = 0;
    }

    public Dish(String description, ImageView photo, float price, int available_qty) {
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
