package com.example.medisant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        Button logout = view.findViewById(R.id.btn_logout);
        Button edit = view.findViewById(R.id.btn_user_edit);
        EditText userName = view.findViewById(R.id.et_user_name);
        EditText userEmail = view.findViewById(R.id.et_user_email);
        EditText userAddress = view.findViewById(R.id.et_user_address);
        EditText userCompany = view.findViewById(R.id.et_user_company_name);
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Config.URL + "api/profile",
                null,
                listener -> {
                    JSONObject usuario = listener;
                    try {
                        userName.setText(usuario.getString("name"));
                        userEmail.setText(usuario.getString("email"));
                        userAddress.setText(usuario.getString("address"));
                        userCompany.setText(usuario.getString("company_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new Config().getHeaders(sharedPreferences.getString("token", ""));
            }
        };
        queue.add(request);

        logout.setOnClickListener(listener->{
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Config.URL + "api/auth/logout",
                    response -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear().apply();
                        Intent intent = new Intent(view.getContext(), Login.class);
                        startActivity(intent);
                        this.getActivity().finish();
                    },
                    error -> {
                        Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return new Config().getHeaders(sharedPreferences.getString("token", ""));
                }
            };
            queue.add(stringRequest);
        });

        edit.setOnClickListener(listener->{
            Toast.makeText(view.getContext(), "Editar perfil", Toast.LENGTH_SHORT).show();
        });


    }
}