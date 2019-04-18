package com.example.ristoratore;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ristoratore.menu.Dish;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Dish> itemList;
    private LayoutInflater layInflater;
    private ItemClickListener clkListener;
    private Typeface robotoRegular, robotoBold;

    RecyclerViewAdapter(Context context, List<Dish> data) {
        this.layInflater = LayoutInflater.from(context);
        this.itemList = data;
        robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
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
        final boolean[] isReverse = {false}; // for reverting the animation

        holder.dishName.setTypeface(robotoRegular);
        holder.dishName.setText(dish.getName());

        holder.dishPrice.setTypeface(robotoBold);
        holder.dishPrice.setText(dish.getPrice());

        holder.dishDesc.setTypeface(robotoBold);
        holder.dishDesc.setText(dish.getDescription());

        if(dish.getPhotoUri() != null && !dish.getPhotoUri().equals(""))
            holder.dishPhoto.setImageURI(Uri.parse(dish.getPhotoUri()));

        holder.slideAnimator.addUpdateListener(animation -> {
            // set as height the value the interpolator is at
            holder.cardView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            // force all layouts to see which ones are affected by this layouts height change
            holder.cardView.requestLayout();

        });

        holder.itemView.setOnClickListener(v -> {
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
        });
    }

    protected void setDataToView(TextView name, ImageView photo, TextView price, TextView desc, int pos) {
        Dish dish = itemList.get(pos);

        name.setTypeface(robotoRegular);
        name.setText(dish.getName());

<<<<<<< HEAD
        desc.setTypeface(robotoRegular);
=======
        price.setTypeface(robotoItalic);
        price.setText(dish.getPrice());

        desc.setTypeface(robotoItalic);
>>>>>>> 1e35a23dc8c5f654dd9694639c212d035c69bb07
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
<<<<<<< HEAD

=======
>>>>>>> 1e35a23dc8c5f654dd9694639c212d035c69bb07
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

class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat -1f);
    }
}