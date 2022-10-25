package com.example.medisant.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medisant.R;
import com.example.medisant.config.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final JSONArray productsList;

    public ProductsAdapter(JSONArray productsList) {
        this.productsList = productsList;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView productImage;
        TextView productName, productShortDesc, productPrice, productAvailable;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_product_image);
            productName = itemView.findViewById(R.id.tv_product_name);
            productShortDesc = itemView.findViewById(R.id.tv_product_short_description);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productAvailable = itemView.findViewById(R.id.tv_product_available);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            try {
                bundle.putString("id", productsList.getJSONObject(getAdapterPosition()).getString("id"));
                Navigation.findNavController(view).navigate(R.id.action_productsFragment_to_detailProductFragment, bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
            Picasso.get().load(Config.URL + productsList.getJSONObject(position).getString("image")).into(holder.productImage);
            holder.productName.setText(productsList.getJSONObject(position).getString("name"));
            holder.productShortDesc.setText(productsList.getJSONObject(position).getString("short_description"));
            holder.productPrice.setText("$" + productsList.getJSONObject(position).getString("price"));
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
