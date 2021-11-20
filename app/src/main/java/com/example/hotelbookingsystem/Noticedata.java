package com.example.hotelbookingsystem;

public class Noticedata {
    String title,editdescs,image,data,time,key;

    public Noticedata() {
    }

    public Noticedata(String title,String editdescs, String image, String data, String time, String key) {
        this.title = title;
        this.image = image;
        this.data = data;
        this.time = time;
        this.editdescs=editdescs;
        this.key = key;
    }

    public String getEditdescs() {
        return editdescs;
    }

    public void setEditdescs(String editdescs) {
        this.editdescs = editdescs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
