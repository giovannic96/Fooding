package com.example.fooding;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Dish> itemList;
    private LayoutInflater layInflater;
    private ItemClickListener clkListener;
    private Typeface robotoRegular, robotoBold;
    private ItemLongClickListener longClkListener;
    private TextView total_tv;
    private Context context;
    private float total;
    private Order order;

    RecyclerViewAdapter(Context context, List<Dish> data, float total, TextView total_tv, Order order) {
        this.layInflater = LayoutInflater.from(context);
        this.itemList = data;
        this.context=context;
        this.total=total;
        this.total_tv=total_tv;
        this.order = order;
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

        if(dish.getQtySel()!=0){
            holder.qtySel.setTextSize(20);
            holder.qtySel.setText(Integer.toString(dish.getQtySel()));}
        else
        {
            holder.qtySel.setTextSize(10);
            holder.qtySel.setText("Order");
        }


        StorageReference photoRef= FirebaseStorage.getInstance().getReference().child(dish.getPhotoUri());
        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) { Picasso.get().load(uri).into(holder.dishPhoto); }
        });

        holder.slideAnimator.addUpdateListener(animation -> {
            holder.cardView.getLayoutParams().height = (Integer) animation.getAnimatedValue(); // set as height the value the interpolator is at
            holder.cardView.requestLayout(); // force all layouts to see which ones are affected by this layouts height change
        });

        holder.qtySel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);
                view = layInflater.inflate(R.layout.dialog_order, null);
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Input quantity");

                final EditText quantity_et = view.findViewById(R.id.quantity_et);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        float price_to_remove=0;
                        if(quantity_et.getText().toString().isEmpty()||quantity_et.getText().toString().matches("0*")) {
                            if(dish.getQtySel()!=0)
                            {
                                price_to_remove=dish.getPriceL()*dish.getQtySel();
                                order.dishList.remove(dish);
                                total-=price_to_remove;
                            }
                            dish.setQtySel(0);
                        }
                        else
                        {
                            if(dish.getQtySel()!=0)
                            {
                                price_to_remove=dish.getPriceL()*dish.getQtySel();
                                total-=price_to_remove;
                                order.dishList.remove(dish);
                            }
                            dish.setQtySel(Integer.parseInt(quantity_et.getText().toString()));
                            order.dishList.add(dish);
                            total+=dish.getPriceL()*dish.getQtySel();
                        }

                        total_tv.setText("Total: "+Float.toString(total/100)+" "+Currency.getInstance(Locale.getDefault()).getSymbol());
                        RecyclerViewAdapter.this.notifyItemChanged(pos);
                        alertDialog.dismiss();
                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        float price_to_remove=0;
                        if(dish.getQtySel()!=0)
                        {
                            price_to_remove=dish.getPriceL()*dish.getQtySel();
                            total-=price_to_remove;
                        }
                        dish.setQtySel(0);
                        total_tv.setText("Total: "+Float.toString(total/100)+" "+Currency.getInstance(Locale.getDefault()).getSymbol());
                        order.dishList.remove(dish);
                        RecyclerViewAdapter.this.notifyItemChanged(pos);
                        alertDialog.dismiss();
                    }
                });


                alertDialog.setView(view);
                alertDialog.show();

            }

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView dishPhoto;
        TextView dishName;
        TextView dishDesc;
        TextView dishPrice;
        Button qtySel;

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
            qtySel = itemView.findViewById(R.id.qtySel_btn);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clkListener != null) clkListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view){
            if(longClkListener != null) longClkListener.onItemLongClick(view, getAdapterPosition());
            return false;
        }
    }

    Dish getItem(int id) {
        return itemList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) { // Catch click events
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
    }
}

class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat -1f);
    }
}