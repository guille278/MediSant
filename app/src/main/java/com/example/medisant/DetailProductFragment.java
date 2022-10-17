package com.example.medisant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.config.Config;
import com.example.medisant.models.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailProductFragment extends Fragment {

    private String token;

    public DetailProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Product product = new Product();
        ImageView imageView = view.findViewById(R.id.iv_product_image);
        TextView productName = view.findViewById(R.id.tv_product_name);
        TextView productShortDesc = view.findViewById(R.id.tv_product_short_description);
        TextView productLongDesc = view.findViewById(R.id.tv_product_long_description);
        TextView productPrice = view.findViewById(R.id.tv_product_price);
        TextView productAvailable = view.findViewById(R.id.tv_product_available);
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        product.setId(Integer.parseInt(getArguments().getString("id")));
        JsonObjectRequest request = product.findOne(
                response -> {
                    JSONObject data = (JSONObject) response;
                    try {
                        Picasso.get().load(Config.URL + data.getString("image")).into(imageView);
                        productName.setText(data.getString("name"));
                        productShortDesc.setText(data.getString("short_description"));
                        productLongDesc.setText(data.getString("long_description"));
                        productPrice.setText("Precio: $"+data.getString("price"));
                        productAvailable.setText("Displonible: "+data.getString("available"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                token
        );
        queue.add(request);
    }
}