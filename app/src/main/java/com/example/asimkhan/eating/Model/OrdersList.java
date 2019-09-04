package com.example.asimkhan.eating.Model;

public class OrdersList {
    private String phone ;
    private String  address;
    private String  status;

    public OrdersList() {
    }

    public OrdersList(String phone, String address, String status) {
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
