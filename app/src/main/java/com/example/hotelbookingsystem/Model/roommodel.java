package com.example.hotelbookingsystem.Model;



public class roommodel {

    String title,editdescs,editroomava,editprice,image,data,time;

    public roommodel() {
    }

    public roommodel(String title,String editdescs,String editroomava,String editprice, String image, String data, String time) {

        this.title = title;
        this.image = image;
        this.data = data;
        this.time = time;
        this.editdescs=editdescs;
        this.editroomava=editroomava;
        this.editprice=editprice;
    }


    public String getEditroomava() {
        return editroomava;
    }

    public void setEditroomava(String editroomava) {
        this.editroomava = editroomava;
    }

    public String getEditprice() {
        return editprice;
    }

    public void setEditprice(String editprice) {
        this.editprice = editprice;
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

    @Override
    public String toString() {
        return "roommodel{" +
                "title='" + title + '\'' +
                ", editdescs='" + editdescs + '\'' +
                ", editroomava='" + editroomava + '\'' +
                ", editprice='" + editprice + '\'' +
                ", image='" + image + '\'' +
                ", data='" + data + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

