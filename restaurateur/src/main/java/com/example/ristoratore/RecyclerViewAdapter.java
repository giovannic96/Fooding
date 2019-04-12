package com.example.ristoratore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ristoratore.menu.Dish;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Dish> itemList;
    private LayoutInflater layInflater;
    private ItemClickListener clkListener;

    RecyclerViewAdapter(Context context, List<Dish> data) {
        this.layInflater = LayoutInflater.from(context);
        this.itemList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        Dish dish = itemList.get(pos);
        holder.dishName.setText(dish.getName());
        holder.dishDesc.setText(dish.getDescription());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dishName;
        TextView dishDesc;

        ViewHolder(View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.dish_name_tv);
            dishDesc = itemView.findViewById(R.id.dish_desc_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clkListener != null) clkListener.onItemClick(view, getAdapterPosition());
        }
    }

    Dish getItem(int id) {
        return itemList.get(id);
    }

    // Catch click events
    void setClickListener(ItemClickListener itemClickListener) {
        this.clkListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}