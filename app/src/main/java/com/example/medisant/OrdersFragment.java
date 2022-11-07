package com.example.medisant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.adapters.OrdersAdapter;
import com.example.medisant.models.Order;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class OrdersFragment extends Fragment {

    private String token;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        RecyclerView rvOrders = view.findViewById(R.id.rv_orders);
        TextView empty = view.findViewById(R.id.tv_empty);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        ProgressBar progressBar = view.findViewById(R.id.progress_orders);
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest request = new Order().get(
                listener -> {
                    JSONArray data = (JSONArray) listener;
                    if (data.length() > 0) {
                        progressBar.setVisibility(View.GONE);
                        rvOrders.setAdapter(new OrdersAdapter(view.getContext(), data));
                    } else {
                        empty.setVisibility(View.VISIBLE);
                    }


                },
                error -> {
                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                },
                token
        );
        queue.add(request);
        rvOrders.setLayoutManager(new LinearLayoutManager(view.getContext()));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JsonArrayRequest request = new Order().get(
                        listener -> {
                            JSONArray data = (JSONArray) listener;
                            swipeRefreshLayout.setRefreshing(false);
                            rvOrders.setAdapter(new OrdersAdapter(view.getContext(), data));
                        },
                        error -> {
                            Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                        },
                        token
                );
                queue.add(request);
            }
        });

    }
}