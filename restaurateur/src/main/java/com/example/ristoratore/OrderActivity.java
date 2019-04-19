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
import java.util.Objects;

@SuppressLint("Registered")
public class OrderActivity extends AppCompatActivity implements OrderViewAdapter.ItemClickListener {

    private static final int CHECK_ITEM_REQ = 45;
    private static final int RESULT_STATUS_CHANGE = 46;

    private RecyclerView rView;
    private OrderViewAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;

    //Hard-coded list of orders to check if it works before implementing interactivity with custoemr app
    String desc1 = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.";
    String desc2 = "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?";
    String desc3 = "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.";
    Dish Potato = new Dish("Patate a forno", desc1,null,"12 €",(long)12,11,null);
    Dish Carrot = new Dish("Carote in padella", desc2,null,"8 €",(long)8,21,null);
    Dish Meat = new Dish("Carne alla brace", desc3,null,"6.50 €",(long)6.50,13,null);
    ArrayList<Dish> dishL1 = new ArrayList<Dish>(Arrays.asList(Potato,Carrot));
    ArrayList<Dish> dishL2 = new ArrayList<Dish>(Arrays.asList(Meat,Carrot));
    ArrayList<Dish> dishL3 = new ArrayList<Dish>(Arrays.asList(Potato,Meat));
    GregorianCalendar date1 = new GregorianCalendar(2019,4,18,12,03);
    GregorianCalendar date2 = new GregorianCalendar(2019,4,18,10,00);
    GregorianCalendar date3 = new GregorianCalendar(2019,4,18,16,00);
    Order ord1 = new Order (1, 0, dishL1,"Citofono Rossani, piano 4","Corso Duca degli Abruzzi, 22",date1,"47.5 €",(long)47.5);
    Order ord2 = new Order (2, 0, dishL2,"No aglio","Largo Francia, 2",date2,"35 €", (long)35);
    Order ord3 = new Order (3, 0, dishL3,"Quarto piano","Piazza Statuto, 25",date3,"15 €",(long)15);
    ArrayList<Order> orders = new ArrayList<Order>(Arrays.asList(ord1,ord2,ord3));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

    public boolean onOptionsItemSelected(MenuItem item) {
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