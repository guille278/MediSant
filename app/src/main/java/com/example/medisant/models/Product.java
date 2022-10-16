package com.example.medisant.models;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.medisant.config.Config;
import com.example.medisant.models.interfaces.ProductInterface;

import java.util.Map;

public class Product implements ProductInterface {
    private static final String END_POINT = "api/products";
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public JsonArrayRequest get(Response.Listener listener, Response.ErrorListener errorListener, String token) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Config.URL + Product.END_POINT,
                null,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return new Config().getHeaders(token);
            }
        };
        return request;
    }

    @Override
    public JsonObjectRequest findOne(Response.Listener listener, Response.ErrorListener errorListener, String token) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Config.URL + Product.END_POINT + "/" + this.id,
                null,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return new Config().getHeaders(token);
            }
        };
        return request;
    }
}
