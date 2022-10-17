package com.example.medisant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medisant.adapters.OrdersAdapter;
import com.example.medisant.models.Order;

import java.util.LinkedList;

public class OrdersFragment extends Fragment {

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
        RecyclerView rvOrders = view.findViewById(R.id.rv_orders);
        LinkedList<Order> orders = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            orders.add(new Order(i, 1, 99.99, "12/11/9999", "12/11/9999"));
        }
        rvOrders.setAdapter(new OrdersAdapter(orders));
        rvOrders.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }
}