package com.example.medisant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.adapters.CartAdapter;
import com.example.medisant.models.Cart;
import com.example.medisant.models.Order;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;

public class CartFragment extends Fragment {

    private String token;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Order order = new Order();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        RecyclerView rvCart = view.findViewById(R.id.rv_cart);
        Button makeOrder = view.findViewById(R.id.btn_make_order);
        TextView orderTotal = view.findViewById(R.id.tv_order_total);
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest request = new Cart().get(
                listener -> {
                    rvCart.setAdapter(new CartAdapter((JSONArray) listener, view.getContext()));
                    JSONArray data = (JSONArray) listener;
                    float total = 0;
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            total += data.getJSONObject(i).getDouble("price") * data.getJSONObject(i).getJSONObject("pivot").getInt("quantity");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    orderTotal.setText("Total: " + NumberFormat.getCurrencyInstance().format(total));
                },
                error -> {
                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                },
                token
        );
        queue.add(request);


        rvCart.setLayoutManager(new LinearLayoutManager(view.getContext()));
        makeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Realizando pedido", Toast.LENGTH_SHORT).show();
            }
        });


    }
}