package com.example.medisant.models;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.medisant.config.Config;
import com.example.medisant.models.interfaces.CartInterface;

import org.json.JSONObject;

import java.util.Map;

public class Cart implements CartInterface {

    private static final String END_POINT = "api/cart";

    @Override
    public JsonArrayRequest get(Response.Listener listener, Response.ErrorListener errorListener, String token) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Config.URL + END_POINT,
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
    public JsonObjectRequest save(Response.Listener listener, Response.ErrorListener errorListener, String token, JSONObject data) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                Config.URL + END_POINT,
                data,
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
}
