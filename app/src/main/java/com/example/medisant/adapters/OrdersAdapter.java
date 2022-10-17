package com.example.medisant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medisant.R;
import com.example.medisant.models.Order;

import java.util.LinkedList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final LinkedList<Order> orders;

    public OrdersAdapter(LinkedList<Order> orders) {
        this.orders = orders;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), ""+orders.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.detailOrderFragment);
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

    }

    @Override
    public int getItemCount() {
        return this.orders.size();
    }
}
