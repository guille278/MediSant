package com.example.medisant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.adapters.ProductsAdapter;
import com.example.medisant.models.Product;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

public class ProductsFragment extends Fragment {

    private String token;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        RecyclerView rvProducts = view.findViewById(R.id.rv_products);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_products);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_contact);
        shimmerFrameLayout.startShimmer();

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        JsonArrayRequest jsonArrayRequest = new Product().get(
                response -> {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rvProducts.setAdapter(new ProductsAdapter(view.getContext(), (JSONArray) response));
                },
                error -> {
                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                token
        );
        queue.add(jsonArrayRequest);
        rvProducts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
            JsonArrayRequest refresh = new Product().get(
                    response -> {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        rvProducts.setVisibility(View.VISIBLE);
                        rvProducts.setAdapter(new ProductsAdapter(view.getContext(),(JSONArray) response));
                    },
                    error -> {
                        Toast.makeText(view.getContext(), "Error al recargar catalogo", Toast.LENGTH_SHORT).show();
                    },
                    token
            );
            queue.add(refresh);
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/+5213323838752"));
                startActivity(intent);
            }
        });
    }
}