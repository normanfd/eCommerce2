package com.example.ecommerce.Model;

public class Users {
    private String name, phone, password;
    //default constructor without parameters
    public Users(){

    }
    //default constructor with parameters
    public Users(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
    //getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
