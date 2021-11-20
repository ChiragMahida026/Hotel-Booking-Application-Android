package com.example.hotelbookingsystem;

public class CartModel {

    String coffeename, imageURL;
    int quantity, totalprice;

    public CartModel() {
    }

    public CartModel(String coffeename, String imageURL, int quantity, int totalprice) {
        this.coffeename = coffeename;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public String getCoffeename() {
        return coffeename;
    }

    public void setCoffeename(String coffeename) {
        this.coffeename = coffeename;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }
}
