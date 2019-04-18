package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.example.ristoratore.menu.Dish;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import static com.example.ristoratore.EditActivity.URI_PREFS;

@SuppressLint("Registered")
public class DailyOfferActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    private static final int ADD_ITEM_REQ = 40;
    private static final int EDIT_ITEM_REQ = 41;
    private static final int RESULT_SAVE = 34;
    private static final int RESULT_DELETE = 35;
    SharedPreferences preferences;
    private static final String PREF_NAME = "DishList sp";
    private static final String DISHLIST_NAME = "Dishes List";

    private RecyclerView rView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;
    ArrayList<Dish> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyoffer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        loadData();
        buildRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fab);
<<<<<<< HEAD
        /*
        Button removeItem_btn = findViewById(R.id.remove_dish_btn);
        Button removeAllItem_btn = findViewById(R.id.removeAll_dish_btn);
        Button updateItem_btn = findViewById(R.id.update_dish_btn);
        Button moveItem_btn = findViewById(R.id.move_dish_btn);
        */
=======
        /*Button removeItem_btn = findViewById(R.id.remove_dish_btn);
        Button removeAllItem_btn = findViewById(R.id.removeAll_dish_btn);
        Button updateItem_btn = findViewById(R.id.update_dish_btn);
        Button moveItem_btn = findViewById(R.id.move_dish_btn);*/

>>>>>>> 1e35a23dc8c5f654dd9694639c212d035c69bb07
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddDishActivity.class);
            startActivityForResult(i, ADD_ITEM_REQ);
        });

        /*
        removeItem_btn.setOnClickListener(v -> {
            int removeIndex = 2;
            dishes.remove(removeIndex);
            adapter.notifyItemRemoved(removeIndex);
        });

        removeAllItem_btn.setOnClickListener(v -> {
            dishes.clear();
            adapter.notifyDataSetChanged();
        });

        updateItem_btn.setOnClickListener(v -> {
            String newValue = "New dish name";
            int updateIndex = 3;
            dishes.set(updateIndex, newValue);
            adapter.notifyItemChanged(updateIndex);
        });

        moveItem_btn.setOnClickListener(v -> {
            int fromPosition = 3;
            int toPosition = 1;

            String item = dishes.get(fromPosition);
            dishes.remove(fromPosition);
            dishes.add(toPosition, item);

            adapter.notifyItemMoved(fromPosition, toPosition);
        });
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQ && resultCode == RESULT_OK) {
            Dish itemReturned = (Dish) data.getSerializableExtra("dish_item");
            if(itemReturned != null) {
                dishes.add(itemReturned);
                adapter.notifyItemInserted(dishes.size());
                saveData();
            }
        }
        else if (requestCode == EDIT_ITEM_REQ && resultCode == RESULT_SAVE){
            Dish itemReturned = (Dish) data.getSerializableExtra("dish_item");
            int position = data.getIntExtra("position", 0);
            if(itemReturned != null){
                dishes.set(position, itemReturned);
                adapter.notifyItemChanged(position);
                saveData();

            }
        }
        else if (requestCode == EDIT_ITEM_REQ && resultCode == RESULT_DELETE){
            int position = data.getIntExtra("position", 0);
            dishes.remove(position);
            adapter.notifyItemRemoved(position);
            saveData();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dishes);
        editor.putString(DISHLIST_NAME, json);
        editor.apply();
    }

    private void loadData() {
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(DISHLIST_NAME, null);
        Type type = new TypeToken<ArrayList<Dish>>() {}.getType();
        dishes = gson.fromJson(json, type);

        if (dishes == null) {
            dishes = new ArrayList<>();
        }
    }

    private void buildRecyclerView() {
        rView = findViewById(R.id.dishes_rView);
        rLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(rLayoutManager);
        adapter = new RecyclerViewAdapter(this, dishes);
        adapter.setClickListener(this);
        rView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        buildRecyclerView();
    }

    @Override
    public void onItemClick(View view, int position) {
<<<<<<< HEAD
        Toast.makeText(this, "Clicked " + adapter.getItem(position).getName() + " on position " + position, Toast.LENGTH_SHORT).show();
        /*
=======
        /* OLD VERSION */
        /*
        //Toast.makeText(this, "Clicked " + adapter.getItem(position).getName() + " on position " + position, Toast.LENGTH_SHORT).show();
>>>>>>> 1e35a23dc8c5f654dd9694639c212d035c69bb07
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_recycler);
        dialog.setTitle("Position " + position);
        dialog.setCancelable(true);

        // set the custom dialog components - texts and image
        TextView name = dialog.findViewById(R.id.dish_name_tv);
        TextView price= dialog.findViewById(R.id.dish_price_tv);
        TextView desc = dialog.findViewById(R.id.dish_desc_tv);
        ImageView photo = dialog.findViewById(R.id.dish_photo_rec_iv);

        adapter.setDataToView(name, photo, price, desc, position);

        dialog.show();
        */
<<<<<<< HEAD
=======

        Intent i = new Intent(getApplicationContext(), EditDishActivity.class);
        i.putExtra("dish", adapter.getItem(position));
        i.putExtra("position", position);
        startActivityForResult(i, EDIT_ITEM_REQ);




    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
>>>>>>> 1e35a23dc8c5f654dd9694639c212d035c69bb07
    }
}