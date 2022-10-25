package com.example.medisant.config;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final String HOST = "192.168.1.78";
    public static final String URL = "http://"+HOST+"/proyecto-9no-web/medisant-admin/public/";

    public Map<String, String> getHeaders(String token){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
