package com.example.asimkhan.eating.Model;

public class Order {
    private String Productid, Productname, Quantity, Price, Discount;

    public Order() {

    }

    public Order(String productid, String productname, String quantity, String price, String discount) {
        Productid = productid;
        Productname = productname;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getProductid() {
        return Productid;
    }

    public void setProductid(String productid) {
        Productid = productid;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
