package com.example.medisant.models.interfaces;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public interface ProductInterface {
    public JsonArrayRequest get(Response.Listener listener, Response.ErrorListener errorListener, String token);
    public JsonObjectRequest findOne(Response.Listener listener, Response.ErrorListener errorListener, String token);
}
