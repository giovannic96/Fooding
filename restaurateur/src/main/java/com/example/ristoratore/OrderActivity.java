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
import com.example.ristoratore.menu.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

@SuppressLint("Registered")
public class OrderActivity extends AppCompatActivity implements OrderViewAdapter.ItemClickListener {

    private static final int CHECK_ITEM_REQ = 45;
    private static final int RESULT_STATUS_CHANGE = 46;


    private RecyclerView rView;
    private OrderViewAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;


    //Hard-coded list of orders to check if it works before implementing interactivity with custoemr app
    Dish Potato = new Dish("potato","buone",null,"1",(long)1,1,null);
    Dish Carrot = new Dish("carrot","buone",null,"2",(long)2,2,null);
    Dish Meat = new Dish("meat","buone",null,"3",(long)3,3,null);
    ArrayList<Dish> dishL1 = new ArrayList<Dish>(Arrays.asList(Potato,Carrot));
    ArrayList<Dish> dishL2 = new ArrayList<Dish>(Arrays.asList(Meat,Carrot));
    ArrayList<Dish> dishL3 = new ArrayList<Dish>(Arrays.asList(Potato,Meat));
    GregorianCalendar date1 = new GregorianCalendar(2019,4,18,12,00);
    GregorianCalendar date2 = new GregorianCalendar(2019,4,18,10,00);
    GregorianCalendar date3 = new GregorianCalendar(2019,4,18,16,00);
    Order ord1 = new Order (1, 0, dishL1,"Puzzo","corso Duca 1",date1,"55",(long)55 );
    Order ord2 = new Order (2, 0, dishL2,"Pazzo","corso Duca 2",date2,"75",(long)75 );
    Order ord3 = new Order (3, 0, dishL3,"Pizzo","corso Duca 3",date3,"95",(long)95 );
    ArrayList<Order> orders = new ArrayList<Order>(Arrays.asList(ord1,ord2,ord3));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Collections.sort(orders);

        buildRecyclerView();


    }



    private void buildRecyclerView() {
        rView = findViewById(R.id.orders_rView);
        rLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(rLayoutManager);
        adapter = new OrderViewAdapter(this, orders);
        adapter.setClickListener(this);
        rView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildRecyclerView();
    }


    @Override
    public void onItemClick(View view, int position) {


        Intent i = new Intent(getApplicationContext(), SingleOrderActivity.class);
        i.putExtra("order", adapter.getItem(position));
        i.putExtra("position", position);
        startActivityForResult(i, CHECK_ITEM_REQ);

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHECK_ITEM_REQ:
                if (resultCode == RESULT_STATUS_CHANGE) {
                    int position = data.getIntExtra("position", 0);
                    Order returned_order = (Order)data.getSerializableExtra("order");
                    orders.set(position,returned_order);
                    adapter.notifyItemChanged(position);
                }
                break;

        }
    }
}