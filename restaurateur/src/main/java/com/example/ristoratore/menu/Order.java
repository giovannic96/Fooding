package com.example.ristoratore.menu;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Order implements Serializable, Comparable<Order> {

    private String orderId;
    private int status;
    private ArrayList<Dish> dishList;
    private String info;
    private String address;
    private GregorianCalendar deliveryTime;
    private String price;
    private long priceL;

    public Order(){}

    public Order(String orderId, int status, ArrayList<Dish> dishList, String info, String address, GregorianCalendar deliveryTime, String price, Long priceL) {
        this.orderId = orderId;
        this.status = status;
        this.dishList = dishList;
        this.info = info;
        this.address = address;
        this.deliveryTime = deliveryTime;
        this.price = price;
        this.priceL = priceL;
    }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public ArrayList<Dish> getDishList() { return dishList; }

    public void setDishList(ArrayList<Dish> dishList) {
        this.dishList = dishList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GregorianCalendar getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(GregorianCalendar deliveryTime) { this.deliveryTime = deliveryTime; }

    public Long getPriceL(){
        return priceL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public int compareTo(Order o) {
        return getDeliveryTime().compareTo(o.getDeliveryTime());
    }

}
