package com.zaviron.burgershotadminapp.model;

import java.util.Date;

public class Orders {
    //new
    private String order_id;
    private String cart_id;
    private String client_id;
    private String product_id;
    private String product_title;
    private String product_price;
    private int Qty;
 //new
    private Date date;
    //new
    private String total_price;

    public Orders(String order_id, String cart_id, String client_id, String product_id, String product_title, String product_price, int qty, Date date, String total_price) {
        this.order_id = order_id;
        this.cart_id = cart_id;
        this.client_id = client_id;
        this.product_id = product_id;
        this.product_title = product_title;
        this.product_price = product_price;
        Qty = qty;
        this.date = date;
        this.total_price = total_price;
    }

    public Orders() {
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
