package com.example.ristoratore.menu;

import android.widget.ImageView;

import java.io.Serializable;

public class Dish implements Serializable {

    private String name;
    private String description;
    private transient ImageView photo;
    private String price;
    private int available_qty;
    private String photoUri;
    private long priceL;
    private int qtySel;


    public Dish(String name, String description, ImageView photo, String price, Long priceL, int available_qty, String photoUri) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.available_qty = available_qty;
        this.photoUri = photoUri;
        this.priceL = priceL;
    }

    public Dish(){}

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPriceL(){
        return priceL;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(int available_qty) {
        this.available_qty = available_qty;
    }

    public int getQtySel() {
        return qtySel;
    }

    public void setQtySel(int qtySel) {
        this.qtySel = qtySel;
    }
}
