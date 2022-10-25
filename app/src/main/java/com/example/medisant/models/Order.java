package com.example.medisant.models;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.medisant.config.Config;
import com.example.medisant.models.interfaces.OrderInterface;

import java.util.Map;

public class Order implements OrderInterface {
    private static final String END_POINT = "api/orders";
    private int id;
    private int status;
    private double total;
    private String orderStart;
    private String orderEnd;

    public Order() {
    }

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

    @Override
    public JsonArrayRequest get(Response.Listener listener, Response.ErrorListener errorListener, String token) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Config.URL + Order.END_POINT,
                null,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new Config().getHeaders(token);
            }
        };
        return request;
    }

    @Override
    public JsonObjectRequest findOne(Response.Listener listener, Response.ErrorListener errorListener) {
        return null;
    }

    @Override
    public JsonObjectRequest save(Response.Listener listener, Response.ErrorListener errorListener) {
        return null;
    }
}
