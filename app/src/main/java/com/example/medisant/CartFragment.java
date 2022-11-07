package com.example.medisant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.adapters.CartAdapter;
import com.example.medisant.config.Config;
import com.example.medisant.models.Cart;
import com.example.medisant.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

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
        ProgressBar progressBar = view.findViewById(R.id.progress_cart);
        RecyclerView rvCart = view.findViewById(R.id.rv_cart);
        Button makeOrder = view.findViewById(R.id.btn_make_order);
        TextView orderTotal = view.findViewById(R.id.tv_order_total);
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest request = new Cart().get(
                listener -> {
                    rvCart.setAdapter(new CartAdapter((JSONArray) listener, view.getContext()));
                    JSONArray data = (JSONArray) listener;
                    if (data.length() > 0) {
                        float total = 0;
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                total += data.getJSONObject(i).getDouble("price") * data.getJSONObject(i).getJSONObject("pivot").getInt("quantity");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        order.setTotal(total);
                        makeOrder.setEnabled(true);
                    }
                    orderTotal.setText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(order.getTotal()));

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
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Confirmación");
                alert.setMessage("¿Desea realizar el pedido?");
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeOrder.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                JsonObjectRequest request1 = new JsonObjectRequest(
                                        Request.Method.POST,
                                        Config.URL + "api/orders",
                                        null,
                                        listener -> {
                                            progressBar.setVisibility(View.GONE);
                                            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                                            alert.setView(LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_confirmation, null));
                                            alert.setPositiveButton("Aceptar", null);
                                            alert.create().show();
                                            Navigation.findNavController(view).navigate(R.id.ordersFragment);
                                        },
                                        error -> {
                                            Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                ) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        return new Config().getHeaders(token);
                                    }
                                };
                                queue.add(request1);
                            }
                        }, 3000);

                    }
                });
                alert.setNegativeButton("Cancelar", null);
                alert.create().show();
            }
        });


    }
}