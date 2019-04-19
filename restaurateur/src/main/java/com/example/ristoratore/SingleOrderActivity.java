package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ristoratore.menu.Dish;
import com.example.ristoratore.menu.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@SuppressLint("Registered")
public class SingleOrderActivity extends AppCompatActivity {

    private static final int CHECK_ITEM_REQ = 45;
    private static final int RESULT_STATUS_CHANGE = 46;


    private RecyclerView rView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;
    ArrayList<Dish> dishes;
    private int position;
    private Order order;
    private TextView address;
    private TextView deliveryTime;
    private TextView info;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
    private ImageView orderStatus;
    private ImageButton switch_status_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_order);

        Intent i = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        position = i.getIntExtra("position",0);
        order = (Order)i.getSerializableExtra("order");
        address = findViewById(R.id.address);
        deliveryTime = findViewById(R.id.deliveryTime);
        info = findViewById(R.id.info);
        orderStatus = findViewById(R.id.status_iv);
        switch_status_btn = findViewById(R.id.switch_status_btn);

        address.setText(order.getAddress());

        info.setText(order.getInfo());

        switch(order.getStatus()) {
            case 0:
                orderStatus.setImageResource(R.mipmap.new_order);
                break;
            case 1:
                orderStatus.setImageResource(R.mipmap.cooking);
                break;
            case 2:
                orderStatus.setImageResource(R.mipmap.ready);
                break;
            case 3:
                orderStatus.setImageResource(R.mipmap.in_delivery);
                break;
            case 4:
                orderStatus.setImageResource(R.mipmap.delivered);
                break;
        }

        deliveryTime.setText(sdf.format(order.getDeliveryTime().getTime()));

        loadData();
        buildRecyclerView();

        switch_status_btn.setOnClickListener(e -> {
            final CharSequence[] items = { "New Order", "Cooking", "Ready","In Delivery","Delivered" };
            AlertDialog.Builder builder = new AlertDialog.Builder(SingleOrderActivity.this);
            builder.setTitle("Change Order Status");
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals("New Order")) {
                    order.setStatus(0);
                    orderStatus.setImageResource(R.mipmap.new_order);
                } else if (items[item].equals("Cooking")) {
                    order.setStatus(1);
                    orderStatus.setImageResource(R.mipmap.cooking);
                } else if (items[item].equals("Ready")) {
                    order.setStatus(2);
                    orderStatus.setImageResource(R.mipmap.ready);
                } else if (items[item].equals("In Delivery")) {
                    order.setStatus(3);
                    orderStatus.setImageResource(R.mipmap.in_delivery);
                } else if (items[item].equals("Delivered")) {
                    order.setStatus(4);
                    orderStatus.setImageResource(R.mipmap.delivered);
                }
            });
            builder.show();

        });

    }


    private void loadData() {


        dishes = order.getDishList();


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
        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", position);
        returnIntent.putExtra("order", order);
        setResult(RESULT_STATUS_CHANGE, returnIntent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed()
    {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", position);
        returnIntent.putExtra("order", order);
        setResult(RESULT_STATUS_CHANGE, returnIntent);

        super.onBackPressed();


    }
}