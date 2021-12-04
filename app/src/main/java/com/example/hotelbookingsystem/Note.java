package com.example.hotelbookingsystem;

public class Note {
    private String name;
    private String address;
    private String phone;
    private String email;
    public Note() {
        //empty constructor needed
    }

    public Note(String name, String address, String phone,String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email=email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
