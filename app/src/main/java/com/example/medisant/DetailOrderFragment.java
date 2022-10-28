package com.example.medisant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.adapters.OrdersDetailAdapter;
import com.example.medisant.models.Order;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailOrderFragment extends Fragment {
    private String token;

    public DetailOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        RecyclerView rvDetailOrder = view.findViewById(R.id.rv_detail_order);
        TextView orderDate = view.findViewById(R.id.tv_order_date);
        TextView orderUpdated = view.findViewById(R.id.tv_order_updated);
        TextView orderStatus = view.findViewById(R.id.tv_order_status);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView orderSubtotal = view.findViewById(R.id.tv_order_subtotal);
        TextView orderTotal = view.findViewById(R.id.tv_order_total);
        Button btnCancel = view.findViewById(R.id.btn_cancel_order);
        Order order = new Order();
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        order.setId(Integer.valueOf(getArguments().getString("id")));
        JsonObjectRequest request = order.findOne(
                listener -> {
                    JSONObject detailOrder = (JSONObject) listener;
                    try {
                        orderDate.setText("Fecha de pedido: " + detailOrder.getString("created_at"));
                        orderUpdated.setText("Ultima Actualizacion: " + detailOrder.getString("updated_at"));

                        switch (detailOrder.getInt("status")) {
                            case 1:
                                orderStatus.setTextColor(Color.parseColor("#0398fc"));
                                orderStatus.setText("Recibido");
                                progressBar.setProgress(20);
                                break;
                            case 2:
                                orderStatus.setTextColor(Color.parseColor("#fcd703"));
                                orderStatus.setText("En camino");
                                progressBar.setProgress(50);
                                break;
                            case 3:
                                orderStatus.setTextColor(Color.parseColor("#FF018786"));
                                orderStatus.setText("Entregado " + detailOrder.getString("delivered"));
                                progressBar.setProgress(100);
                                btnCancel.setVisibility(View.GONE);
                                break;
                            default:
                                orderStatus.setTextColor(Color.parseColor("#d40222"));
                                orderStatus.setText("Cancelado");
                                progressBar.setProgress(0);
                                btnCancel.setVisibility(View.GONE);
                        }
                        orderTotal.setText("Total: $" + detailOrder.getDouble("total"));
                        double subtotal = 0;
                        for (int i = 0; i < detailOrder.getJSONArray("products").length(); i++) {
                            subtotal += detailOrder.getJSONArray("products").getJSONObject(i).getJSONObject("pivot").getDouble("quantity") * detailOrder.getJSONArray("products").getJSONObject(i).getJSONObject("pivot").getDouble("subtotal");
                        }
                        orderSubtotal.setText("Subtotal :$" + subtotal);
                        rvDetailOrder.setAdapter(new OrdersDetailAdapter(detailOrder.getJSONArray("products")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                token
        );
        rvDetailOrder.setLayoutManager(new LinearLayoutManager(view.getContext()));
        queue.add(request);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                alerta.setTitle("Cancelar pedido");
                alerta.setMessage("¿Estas seguro que deseas cancelar el pedido?");
                alerta.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                    StringRequest request1 = order.delete(
                            listener -> {
                                Toast.makeText(view.getContext(), "Pedido cancelado exitosamente.", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.ordersFragment);
                            },
                            error -> {
                                Toast.makeText(view.getContext(), "Error al cancelar el pedido.", Toast.LENGTH_SHORT).show();
                            },
                            token
                    );
                    queue.add(request1);
                });
                AlertDialog dialog = alerta.create();
                dialog.show();
            }
        });


    }
}