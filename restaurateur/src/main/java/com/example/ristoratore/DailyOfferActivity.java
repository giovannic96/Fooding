package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

@SuppressLint("Registered")
public class DailyOfferActivity2 extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    DividerItemDecoration divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyoffer2);

        ArrayList<String> dishes = new ArrayList<>();
        dishes.add("Amatriciana");
        dishes.add("Carbonara");
        dishes.add("Cacio e Pepe");
        dishes.add("Pasta al ragù");
        dishes.add("Gricia");

        RecyclerView rView = findViewById(R.id.dishes_rView);
        divider = new DividerItemDecoration(rView.getContext(), new LinearLayoutManager(this).getOrientation());
        Button addItem_btn = findViewById(R.id.add_dish_btn);
        Button removeItem_btn = findViewById(R.id.remove_dish_btn);

        rView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, dishes);
        adapter.setClickListener(this);
        rView.setAdapter(adapter);
        rView.addItemDecoration(divider);

        addItem_btn.setOnClickListener(v -> {
            String item = "Nuova pasta";
            int insertIndex = 2;
            dishes.add(insertIndex, item);
            adapter.notifyItemInserted(insertIndex);
        });

        removeItem_btn.setOnClickListener(v -> {
            int removeIndex = 2;
            dishes.remove(removeIndex);
            adapter.notifyItemRemoved(removeIndex);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Clicked " + adapter.getItem(position) + " on position " + position, Toast.LENGTH_SHORT).show();
    }
}