package com.example.medisant.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medisant.R;
import com.example.medisant.config.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class OrdersDetailAdapter extends RecyclerView.Adapter<OrdersDetailAdapter.OrderDetailViewHolder> {

    private JSONArray productsList;

    public OrdersDetailAdapter(JSONArray products) {
        this.productsList = products;
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productShortDesc, productPrice, productQuantity;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_product_image);
            productName = itemView.findViewById(R.id.tv_product_name);
            productShortDesc = itemView.findViewById(R.id.tv_product_short_description);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productQuantity = itemView.findViewById(R.id.tv_product_quantity);
        }
    }

    @NonNull
    @Override
    public OrdersDetailAdapter.OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_card, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersDetailAdapter.OrderDetailViewHolder holder, int position) {
        try {
            Picasso.get().load(Config.URL + productsList.getJSONObject(position).getString("image")).into(holder.productImage);
            holder.productName.setText(productsList.getJSONObject(position).getString("name"));
            holder.productShortDesc.setText(productsList.getJSONObject(position).getString("short_description"));
            holder.productPrice.setText("$" + productsList.getJSONObject(position).getJSONObject("pivot").getDouble("subtotal"));
            holder.productQuantity.setText("X" + productsList.getJSONObject(position).getJSONObject("pivot").getString("quantity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.productsList.length();
    }


}
