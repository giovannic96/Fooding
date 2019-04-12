package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

import com.example.ristoratore.menu.Dish;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

@SuppressLint("Registered")
public class DailyOfferActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    private static final int ADD_ITEM_REQ = 40;

    SharedPreferences preferences;
    private static final String PREF_NAME = "DishList sp";
    private static final String DISHLIST_NAME = "Dishes List";

    private RecyclerView rView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private DividerItemDecoration divider;
    ArrayList<Dish> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyoffer);

        loadData();
        buildRecyclerView();

        Button addItem_btn = findViewById(R.id.add_dish_btn);
        Button removeItem_btn = findViewById(R.id.remove_dish_btn);
        Button removeAllItem_btn = findViewById(R.id.removeAll_dish_btn);
        Button updateItem_btn = findViewById(R.id.update_dish_btn);
        Button moveItem_btn = findViewById(R.id.move_dish_btn);

        addItem_btn.setOnClickListener(v -> {
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
        divider = new DividerItemDecoration(rView.getContext(), new LinearLayoutManager(this).getOrientation());
        rLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(rLayoutManager);
        adapter = new RecyclerViewAdapter(this, dishes);
        adapter.setClickListener(this);
        rView.setAdapter(adapter);
        rView.addItemDecoration(divider);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        buildRecyclerView();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Clicked " + adapter.getItem(position).getName() + " on position " + position, Toast.LENGTH_SHORT).show();
    }
}