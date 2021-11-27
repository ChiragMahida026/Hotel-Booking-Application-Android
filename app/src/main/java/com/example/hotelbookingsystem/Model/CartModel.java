package com.example.hotelbookingsystem.Model;

import com.google.firebase.firestore.DocumentId;

public class CartModel {


    String roomid,imageURL,roomname;
    int quantity,totalprice;

    public CartModel() {
    }

    public CartModel(String roomid, String imageURL, String roomname, int quantity, int totalprice) {
        this.roomid = roomid;
        this.imageURL = imageURL;
        this.roomname = roomname;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
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

    @Override
    public String toString() {
        return "CartModel{" +
                "roomid='" + roomid + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", roomname='" + roomname + '\'' +
                ", quantity=" + quantity +
                ", totalprice=" + totalprice +
                '}';
    }
}
