package com.example.medisant.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisant.R;
import com.example.medisant.config.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private JSONArray items;
    private Context mContext;


    public CartAdapter(JSONArray items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }


    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView productImage;
        TextView productName, productShortDesc, productPrice, productQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_product_image);
            productName = itemView.findViewById(R.id.tv_product_name);
            productShortDesc = itemView.findViewById(R.id.tv_product_short_description);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productQuantity = itemView.findViewById(R.id.tv_product_quantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_cart_card, null);
            alert.setView(v);
            final AlertDialog dialog = alert.create();
            dialog.show();
            ImageView dialogProductImage = v.findViewById(R.id.iv_dialog_product_image);
            TextView dialogProductName = v.findViewById(R.id.tv_dialog_product_name);
            TextView dialogProductShortDesc = v.findViewById(R.id.tv_dialog_product_short_description);
            TextView dialogProductPrice = v.findViewById(R.id.tv_dialog_product_price);
            EditText dialogProductQuantity = v.findViewById(R.id.et_dialog_product_quantity);
            Button btnDelete = v.findViewById(R.id.btn_dialog_delete);
            Button btnUpdate = v.findViewById(R.id.btn_dialog_update);
            try {
                Picasso.get().load(Config.URL+"storage/"+ items.getJSONObject(getAdapterPosition()).getString("image")).into(dialogProductImage);
                dialogProductName.setText(items.getJSONObject(getAdapterPosition()).getString("name"));
                dialogProductShortDesc.setText(items.getJSONObject(getAdapterPosition()).getString("short_description"));
                dialogProductPrice.setText(NumberFormat.getCurrencyInstance().format(items.getJSONObject(getAdapterPosition()).getDouble("price")));
                dialogProductQuantity.setText(items.getJSONObject(getAdapterPosition()).getJSONObject("pivot").getString("quantity"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder confirm = new AlertDialog.Builder(mContext);
                    confirm.setTitle("Confirmación");
                    try {
                        confirm.setMessage("¿Deseas eliminar " + items.getJSONObject(getAdapterPosition()).getString("name") + " de tu carrito?");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    confirm.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                SharedPreferences preferences = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                                RequestQueue queue = Volley.newRequestQueue(mContext);
                                StringRequest request = new StringRequest(
                                        Request.Method.DELETE,
                                        Config.URL + "api/cart/" + items.getJSONObject(getAdapterPosition()).getString("id"),
                                        listener -> {
                                            View rootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
                                            TextView orderTotal = rootView.findViewById(R.id.tv_order_total);
                                            Button makeOrder = rootView.findViewById(R.id.btn_make_order);
                                            items.remove(getAdapterPosition());
                                            float total = 0;
                                            for (int j = 0; j < items.length(); j++) {
                                                try {
                                                    total += items.getJSONObject(j).getDouble("price") * items.getJSONObject(j).getJSONObject("pivot").getInt("quantity");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            orderTotal.setText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(total));
                                            notifyItemRemoved(getAdapterPosition());
                                            Toast.makeText(mContext, "Producto eliminado.", Toast.LENGTH_SHORT).show();
                                            if (items.length() <= 0){
                                                makeOrder.setEnabled(false);
                                            }
                                        },
                                        error -> {
                                            Toast.makeText(mContext, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                ) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        return new Config().getHeaders(preferences.getString("token", ""));
                                    }
                                };
                                queue.add(request);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.hide();
                        }
                    });
                    confirm.setNegativeButton("Cancelar", null);
                    confirm.create().show();

                }
            });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = mContext.getSharedPreferences("token", Context.MODE_PRIVATE);
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    try {
                        JSONObject updatedData = new JSONObject();
                        updatedData.put("quantity", Integer.parseInt(dialogProductQuantity.getText().toString()));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.PUT,
                                Config.URL + "api/cart/" + items.getJSONObject(getAdapterPosition()).getString("id"),
                                updatedData,
                                listener -> {
                                    View rootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
                                    TextView orderTotal = rootView.findViewById(R.id.tv_order_total);
                                    try {
                                        items.getJSONObject(getAdapterPosition()).getJSONObject("pivot").put("quantity", Integer.parseInt(dialogProductQuantity.getText().toString()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    float total = 0;
                                    for (int i = 0; i < items.length(); i++) {
                                        try {
                                            total += items.getJSONObject(i).getDouble("price") * items.getJSONObject(i).getJSONObject("pivot").getInt("quantity");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    orderTotal.setText("Total: " + NumberFormat.getCurrencyInstance().format(total));
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Producto actualizado.", Toast.LENGTH_SHORT).show();
                                },
                                error -> {
                                    Toast.makeText(mContext, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                return new Config().getHeaders(preferences.getString("token", ""));
                            }
                        };
                        queue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.hide();
                }
            });


        }
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        try {
            Picasso.get().load(Config.URL+"storage/" + items.getJSONObject(position).getString("image")).into(holder.productImage);
            holder.productName.setText(items.getJSONObject(position).getString("name"));
            holder.productShortDesc.setText(items.getJSONObject(position).getString("short_description"));
            holder.productPrice.setText("$" + items.getJSONObject(position).getDouble("price"));
            holder.productQuantity.setText("X" + items.getJSONObject(position).getJSONObject("pivot").getString("quantity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.items.length();
    }
}
