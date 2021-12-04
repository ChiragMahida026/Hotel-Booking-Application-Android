package com.example.hotelbookingsystem;

import com.google.firebase.firestore.DocumentId;

public class Noticedata {

    @DocumentId
    String roomid;
    int editroomava, editprice;
    int quantity;
    String title, editdescs, image, data, time;

    public Noticedata() {
    }

    public Noticedata(String roomid, int editroomava, int editprice, int quantity, String title, String editdescs, String image, String data, String time) {
        this.roomid = roomid;
        this.editroomava = editroomava;
        this.editprice = editprice;
        this.quantity = quantity;
        this.title = title;
        this.editdescs = editdescs;
        this.image = image;
        this.data = data;
        this.time = time;
    }

    public Noticedata(int editroomava, int editprice, int quantity, String title, String editdescs, String image, String data, String time) {
        this.editroomava = editroomava;
        this.editprice = editprice;
        this.quantity = quantity;
        this.title = title;
        this.editdescs = editdescs;
        this.image = image;
        this.data = data;
        this.time = time;
    }

    public Noticedata(String title, String editdescs, String editroomava, String editprice, String downloadUrl, String date, String time) {
        this.quantity = quantity;
        this.title = title;
        this.editdescs = editdescs;
        this.image = image;
        this.data = data;
        this.time = time;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public int getEditroomava() {
        return editroomava;
    }

    public void setEditroomava(int editroomava) {
        this.editroomava = editroomava;
    }

    public int getEditprice() {
        return editprice;
    }

    public void setEditprice(int editprice) {
        this.editprice = editprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEditdescs() {
        return editdescs;
    }

    public void setEditdescs(String editdescs) {
        this.editdescs = editdescs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
