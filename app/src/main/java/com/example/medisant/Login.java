package com.example.medisant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        Button button = findViewById(R.id.btn_login);
        JSONObject jsonObject = new JSONObject();
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        if (sharedPreferences.contains("token")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(view -> {
            try {
                jsonObject.put("email", email.getText());
                jsonObject.put("password", password.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Config.URL + "api/auth/login",
                    jsonObject,
                    response -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        try {
                            editor.putString("token", response.getString("token"));
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), "Credenciales invalidas!", Toast.LENGTH_SHORT).show();
                    }
            );
            queue.add(jsonObjectRequest);
        });
    }

}