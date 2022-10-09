package com.example.medisant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medisant.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private JSONArray productsList;

    public ProductsAdapter(JSONArray productsList) {
        this.productsList = productsList;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productShortDesc, productPrice, productAvailable;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_product_image);
            productName = itemView.findViewById(R.id.tv_product_name);
            productShortDesc = itemView.findViewById(R.id.tv_product_description);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productAvailable = itemView.findViewById(R.id.tv_product_available);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        try {
            holder.productName.setText(productsList.getJSONObject(position).getString("name"));
            holder.productShortDesc.setText(productsList.getJSONObject(position).getString("short_description"));
            holder.productPrice.setText("$"+productsList.getJSONObject(position).getString("price"));
            holder.productAvailable.setText("Disponibles: " + productsList.getJSONObject(position).getString("available"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.productsList.length();
    }


}
