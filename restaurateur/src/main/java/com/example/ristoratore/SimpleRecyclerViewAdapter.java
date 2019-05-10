package com.example.ristoratore;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ristoratore.menu.Dish;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder> {

    private List<Dish> itemList;
    private LayoutInflater layInflater;
    //private ItemClickListener clkListener;
    private Typeface robotoRegular, robotoBold;
    //private ItemLongClickListener longClkListener;

    SimpleRecyclerViewAdapter(Context context, List<Dish> data) {
        this.layInflater = LayoutInflater.from(context);
        this.itemList = data;
        robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layInflater.inflate(R.layout.item_recycler_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        Dish dish = itemList.get(pos);
        //final boolean[] isReverse = {false}; // for reverting the animation

        holder.dishName.setTypeface(robotoRegular);
        holder.dishName.setText(dish.getName());

        //holder.dishPrice.setTypeface(robotoBold);
        //holder.dishPrice.setText(dish.getPrice());

        //holder.dishDesc.setTypeface(robotoBold);
        //holder.dishDesc.setText(dish.getDescription());

        holder.dishQtySel.setTypeface(robotoBold);
        holder.dishQtySel.setText("Ordered: "+dish.getQtySel());

        StorageReference photoRef= FirebaseStorage.getInstance().getReference().child(dish.getPhotoUri());
        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.dishPhoto);
            }
        });

        /*holder.slideAnimator.addUpdateListener(animation -> {
            holder.cardView.getLayoutParams().height = (Integer) animation.getAnimatedValue(); // set as height the value the interpolator is at
            holder.cardView.requestLayout(); // force all layouts to see which ones are affected by this layouts height change
        });*/

        /*holder.itemView.setOnClickListener(v -> {
            AnimatorSet set = new AnimatorSet();
            if (!isReverse[0]) {
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                holder.dishDesc.setVisibility(View.VISIBLE);
                isReverse[0] = !isReverse[0];
            } else {
                set.setInterpolator(new ReverseInterpolator());
                holder.dishDesc.setVisibility(View.GONE);
                isReverse[0] = !isReverse[0];
            }
            set.play(holder.slideAnimator);
            set.start();
        });*/
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

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener, View.OnLongClickListener*/ {

        ImageView dishPhoto;
        TextView dishName;
        TextView dishDesc;
        TextView dishPrice;
        TextView dishQtySel;

        final View cardView = itemView.findViewById(R.id.card_view);
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(cardView.getLayoutParams().height, 700)
                .setDuration(300);

        ViewHolder(View itemView) {
            super(itemView);
            dishPhoto = itemView.findViewById(R.id.dish_photo_rec_iv);
            dishName = itemView.findViewById(R.id.dish_name_tv);
            dishPrice = itemView.findViewById(R.id.dish_price_tv);
            dishDesc = itemView.findViewById(R.id.dish_desc_tv);
            dishQtySel = itemView.findViewById(R.id.dish_qtySel_tv);
            //itemView.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
        }

        /*@Override
        public void onClick(View view) {
            if (clkListener != null) clkListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view){
            if(longClkListener != null) longClkListener.onItemLongClick(view, getAdapterPosition());
            return false;
        }*/
    }

    Dish getItem(int id) {
        return itemList.get(id);
    }

    /*void setClickListener(ItemClickListener itemClickListener) { // Catch click events
        this.clkListener = itemClickListener;
    }

    void setLongClkListener(ItemLongClickListener longClkLister){
        this.longClkListener = longClkLister;
    }

    public interface ItemClickListener { // parent activity will implement this method to respond to click events
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }*/
}