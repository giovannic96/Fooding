package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ristoratore.menu.Order;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.ViewHolder> {

    private List<Order> itemList;
    private LayoutInflater layInflater;
    private ItemClickListener clkListener;
    private Typeface robotoRegular, robotoBold;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");

    OrderViewAdapter(Context context, List<Order> data) {
        this.layInflater = LayoutInflater.from(context);
        this.itemList = data;
        robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layInflater.inflate(R.layout.order_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        Order order = itemList.get(pos);

        switch(order.getStatus()) {
            case 0:
                holder.orderStatus.setImageResource(R.mipmap.new_order);
                break;
            case 1:
                holder.orderStatus.setImageResource(R.mipmap.cooking);
                break;
            case 2:
                holder.orderStatus.setImageResource(R.mipmap.ready);
                break;
            case 3:
                holder.orderStatus.setImageResource(R.mipmap.in_delivery);
                break;
            case 4:
                holder.orderStatus.setImageResource(R.mipmap.delivered);
                break;
        }
        holder.orderAddress.setTypeface(robotoRegular);
        holder.orderAddress.setText(order.getAddress());

        holder.orderTime.setTypeface(robotoBold);
        holder.orderTime.setText(sdf.format(order.getDeliveryTime().getTime()));

        holder.orderPrice.setTypeface(robotoBold);
        holder.orderPrice.setText(order.getPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView orderStatus;
        TextView orderAddress;
        TextView orderTime;
        TextView orderPrice;

        ViewHolder(View itemView) {
            super(itemView);
            orderStatus = itemView.findViewById(R.id.order_status_rec_iv);
            orderAddress = itemView.findViewById(R.id.address_tv);
            orderTime = itemView.findViewById(R.id.delivery_time_tv);
            orderPrice = itemView.findViewById(R.id.order_price_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clkListener != null) clkListener.onItemClick(view, getAdapterPosition());
        }
    }

    Order getItem(int id) {
        return itemList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clkListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}