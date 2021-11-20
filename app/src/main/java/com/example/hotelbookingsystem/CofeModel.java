package com.example.hotelbookingsystem;

import com.google.firebase.firestore.DocumentId;

public class CofeModel {

    @DocumentId
    String coffeeid;
    String description, imageURL, coffeename;
    int price, quantity;

    public CofeModel() {
    }

    public CofeModel(String coffeeid, String description, String imageURL, String coffeename, int price, int quantity) {
        this.coffeeid = coffeeid;
        this.description = description;
        this.imageURL = imageURL;
        this.coffeename = coffeename;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCoffeeid() {
        return coffeeid;
    }

    public void setCoffeeid(String coffeeid) {
        this.coffeeid = coffeeid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCoffeename() {
        return coffeename;
    }

    public void setCoffeename(String coffeename) {
        this.coffeename = coffeename;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CofeModel{" +
                "coffeeid='" + coffeeid + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", coffeename='" + coffeename + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
