package com.example.medisant.models.interfaces;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public interface CartInterface {
    public JsonArrayRequest get(Response.Listener listener, Response.ErrorListener errorListener, String token);
    public JsonObjectRequest save(Response.Listener listener, Response.ErrorListener errorListener, String token, JSONObject data);
}
