package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("Registered")
public class DailyOfferActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    private static final int ADD_ITEM_REQ = 40;

    SharedPreferences preferences;

    RecyclerViewAdapter adapter;
    DividerItemDecoration divider;
    ArrayList<String> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyoffer);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        dishes = new ArrayList<>();
        dishes.add("Amatriciana");
        dishes.add("Carbonara");

        RecyclerView rView = findViewById(R.id.dishes_rView);
        divider = new DividerItemDecoration(rView.getContext(), new LinearLayoutManager(this).getOrientation());
        Button addItem_btn = findViewById(R.id.add_dish_btn);
        Button removeItem_btn = findViewById(R.id.remove_dish_btn);
        Button removeAllItem_btn = findViewById(R.id.removeAll_dish_btn);
        Button updateItem_btn = findViewById(R.id.update_dish_btn);
        Button moveItem_btn = findViewById(R.id.move_dish_btn);

        rView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, dishes);
        adapter.setClickListener(this);
        rView.setAdapter(adapter);
        rView.addItemDecoration(divider);

        addItem_btn.setOnClickListener(v -> {
            /*
            String item = "Nuova pasta";
            int insertIndex = 2;
            dishes.add(insertIndex, item);
            adapter.notifyItemInserted(insertIndex);
            */
            Intent i = new Intent(getApplicationContext(), AddDishActivity.class);
            startActivityForResult(i, ADD_ITEM_REQ);
        });

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        if(preferences.contains(AddDishActivity.DISH_PHOTO_PREFS)) {
            avatar.setImageURI(Uri.parse(preferences.getString(AddDishActivity.DISH_PHOTO_PREFS, "")));
            if(avatar.getDrawable() == null)
                avatar.setImageResource(R.drawable.ic_launcher_foreground);
        }*/
        if(preferences.contains(AddDishActivity.DISH_NAME_PREFS)) { // TO-DO Non devo salvare il nome ecc nelle shared, ma una lista di piatti!!!!
            int insertIndex = 2;
            dishes.add(preferences.getString(AddDishActivity.DISH_NAME_PREFS, ""));
            adapter.notifyItemInserted(insertIndex);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Clicked " + adapter.getItem(position) + " on position " + position, Toast.LENGTH_SHORT).show();
    }
}