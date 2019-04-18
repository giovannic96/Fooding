package com.example.ristoratore;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ristoratore.menu.Dish;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Dish> itemList;
    private LayoutInflater layInflater;
    private ItemClickListener clkListener;
    private Typeface robotoItalic, robotoBold;

    RecyclerViewAdapter(Context context, List<Dish> data) {
        this.layInflater = LayoutInflater.from(context);
        this.itemList = data;
        robotoItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
        robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layInflater.inflate(R.layout.item_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        Dish dish = itemList.get(pos);

        holder.dishName.setTypeface(robotoItalic);
        holder.dishName.setText(dish.getName());

        holder.dishPrice.setTypeface(robotoBold);
        holder.dishPrice.setText(dish.getPrice());

        holder.dishDesc.setTypeface(robotoBold);
        holder.dishDesc.setText(dish.getDescription());

        if(dish.getPhotoUri() != null && !dish.getPhotoUri().equals(""))
            holder.dishPhoto.setImageURI(Uri.parse(dish.getPhotoUri()));
    }

    protected void setDataToView(TextView name, ImageView photo, TextView price, TextView desc, int pos) {
        Dish dish = itemList.get(pos);

        name.setTypeface(robotoItalic);
        name.setText(dish.getName());

        price.setTypeface(robotoItalic);
        price.setText(dish.getPrice());

        desc.setTypeface(robotoItalic);
        desc.setText(dish.getDescription());

        if(dish.getPhotoUri() != null && !dish.getPhotoUri().equals(""))
            photo.setImageURI(Uri.parse(dish.getPhotoUri()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView dishPhoto;
        TextView dishName;
        TextView dishDesc;
        TextView dishPrice;

        ViewHolder(View itemView) {
            super(itemView);
            dishPhoto = itemView.findViewById(R.id.dish_photo_rec_iv);
            dishName = itemView.findViewById(R.id.dish_name_tv);
            dishPrice = itemView.findViewById(R.id.dish_price_tv);
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