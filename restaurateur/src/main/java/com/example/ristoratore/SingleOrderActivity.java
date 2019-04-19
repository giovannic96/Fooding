package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.ristoratore.menu.Dish;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

@SuppressLint("Registered")
public class SingleOrderActivity extends AppCompatActivity {

    private static final int CHECK_ITEM_REQ = 45;


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
        setContentView(R.layout.single_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadData();
        buildRecyclerView();

    }




    private void loadData() {

        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(DISHLIST_NAME, null);
        Type type = new TypeToken<ArrayList<Dish>>() {}.getType();
        dishes = gson.fromJson(json, type);


    }

    private void buildRecyclerView() {
        rView = findViewById(R.id.dishes_rView);
        rLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(rLayoutManager);
        adapter = new RecyclerViewAdapter(this, dishes);
        rView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        buildRecyclerView();
    }



    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }
}