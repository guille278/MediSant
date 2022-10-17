package com.example.medisant.models;

public class Order {
    private int id;
    private int status;
    private double total;
    private String orderStart;
    private String orderEnd;

    public Order(int id, int status, double total, String orderStart, String orderEnd) {
        this.id = id;
        this.status = status;
        this.total = total;
        this.orderStart = orderStart;
        this.orderEnd = orderEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getOrderStart() {
        return orderStart;
    }

    public void setOrderStart(String orderStart) {
        this.orderStart = orderStart;
    }

    public String getOrderEnd() {
        return orderEnd;
    }

    public void setOrderEnd(String orderEnd) {
        this.orderEnd = orderEnd;
    }
}
