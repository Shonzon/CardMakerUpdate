package com.example.cardmaker.models;

public class RvModel4 {
    private String name,designation,email,address;
    private String phone;
    //private long phone;

    public RvModel4() {
    }

    public RvModel4(String name, String designation, String phone, String email, String address) {
        this.name = name;
        this.designation = designation;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone() {
        return phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
