package com.example.fooding;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private List<Restaurant> itemList;
    private ItemClickListener clkListener;
    private Typeface robotoRegular, robotoBold;
    Context context;

    BrowseAdapter(Context context, List<Restaurant> data) {
        this.context = context;
        this.itemList = data;
        robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int pos) {
        Restaurant restaurant = itemList.get(pos);

        holder.restName.setTypeface(robotoRegular);
        holder.restName.setText(restaurant.getName());

        holder.restType.setTypeface(robotoBold);
        holder.restType.setText(restaurant.getType());
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try{
            if(itemList.size()==0){
                arr = 0;
            }
            else{
                arr=itemList.size();
            }
        }catch (Exception e){}
        return arr;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView restName;
        TextView restType;

        ViewHolder(View itemView) {
            super(itemView);
            restName = itemView.findViewById(R.id.name_tv);
            restType = itemView.findViewById(R.id.type_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clkListener != null) clkListener.onItemClick(view, getAdapterPosition());
        }
    }

    Restaurant getItem(int id) {
        return itemList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clkListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}