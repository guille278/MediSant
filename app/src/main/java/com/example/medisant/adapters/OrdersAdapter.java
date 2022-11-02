package com.example.medisant.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medisant.R;
import com.example.medisant.models.Order;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final JSONArray orders;
    private Context mContext;

    public OrdersAdapter(Context context, JSONArray orders) {
        this.orders = orders;
        this.mContext=context;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView orderDate, orderTotal, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.tv_order_date);
            orderTotal = itemView.findViewById(R.id.tv_order_total);
            orderStatus = itemView.findViewById(R.id.tv_order_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            try {
                bundle.putString("id", orders.getJSONObject(getAdapterPosition()).getString("id"));
                Navigation.findNavController(view).navigate(R.id.detailOrderFragment, bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public OrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderViewHolder holder, int position) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                OffsetDateTime time = OffsetDateTime.ofInstant(Instant.parse(this.orders.getJSONObject(position).getString("created_at")), ZoneId.systemDefault());
                holder.orderDate.setText(mContext.getString(R.string.order_date, DateTimeFormatter.ofPattern("dd MMMM yyyy").format(time)));
                holder.orderTotal.setText(mContext.getString(R.string.order_total, NumberFormat.getCurrencyInstance(Locale.US).format(this.orders.getJSONObject(position).getDouble("total"))));
                switch (this.orders.getJSONObject(position).getInt("status")) {
                    case 1:
                        holder.orderStatus.setTextColor(Color.parseColor("#0398fc"));
                        holder.orderStatus.setText(mContext.getString(R.string.order_state, "Recibido"));
                        break;
                    case 2:
                        holder.orderStatus.setTextColor(Color.parseColor("#fcd703"));
                        holder.orderStatus.setText(mContext.getString(R.string.order_state, "En camino"));
                        break;
                    case 3:
                        holder.orderStatus.setTextColor(Color.parseColor("#FF018786"));
                        holder.orderStatus.setText(mContext.getString(R.string.order_state_delivered, this.orders.getJSONObject(position).getString("delivered")));
                        break;
                    default:
                        holder.orderStatus.setTextColor(Color.parseColor("#d40222"));
                        holder.orderStatus.setText(mContext.getString(R.string.order_state, "Cancelado"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.orders.length();
    }
}
