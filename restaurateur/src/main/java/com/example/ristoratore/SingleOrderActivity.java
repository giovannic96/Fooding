package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.example.ristoratore.menu.Dish;
import com.example.ristoratore.menu.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private SimpleRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;
    ArrayList<Dish> dishes=new ArrayList<>();
    private int position;
    private Order order;
    private TextView address;
    private TextView deliveryTime;
    private TextView info;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
    private ImageView orderStatus;
    private ImageButton switch_status_btn;

    private DatabaseReference database;
    private DatabaseReference orderRef;
    private String uid;
    private String restname;
    private String restaddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_order);

        Intent i = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        database= FirebaseDatabase.getInstance().getReference();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("restaurateur").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    restname=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("restaurateur").child(uid).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    restaddr=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        position = i.getIntExtra("position",0);
        order = (Order)i.getSerializableExtra("order");
        orderRef=database.child("restaurateur").child(uid).child("orders").child(order.getOrderId());
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
        //buildRecyclerView();

        switch_status_btn.setOnClickListener(e -> {
            final CharSequence[] items = { "New Order", "Cooking", "Ready","In Delivery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(SingleOrderActivity.this);
            builder.setTitle("Change Order Status");
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals("New Order")) {
                    order.setStatus(0);
                    orderRef.child("status").setValue("0");
                    orderStatus.setImageResource(R.mipmap.new_order);
                } else if (items[item].equals("Cooking")) {
                    order.setStatus(1);
                    orderRef.child("status").setValue("1");
                    orderStatus.setImageResource(R.mipmap.cooking);
                } else if (items[item].equals("Ready")) {
                    order.setStatus(2);
                    orderRef.child("status").setValue("2");
                    orderStatus.setImageResource(R.mipmap.ready);
                } else if (items[item].equals("In Delivery")) {
                    DatabaseReference bikerRef=database.child("biker");
                    bikerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int flag=0;
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren() ){
                                if(dataSnapshot1.child("status").getValue().toString().equals("True"))
                                {
                                    bikerRef.child(dataSnapshot1.getKey()).child("status").setValue("False");
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("restaurantName").setValue(restname);
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("restaurantAddress").setValue(restaddr);
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("deliveryAddress").setValue(order.getAddress());
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("deliveryHour").setValue(sdf.format(order.getDeliveryTime().getTime()));
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("price").setValue(order.getPrice());
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("info").setValue(order.getInfo());
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("restid").setValue(uid);
                                    bikerRef.child(dataSnapshot1.getKey()).child("currentOrder").child("orderid").setValue(order.getOrderId());
                                    order.setStatus(3);
                                    orderRef.child("status").setValue("3");
                                    orderStatus.setImageResource(R.mipmap.in_delivery);
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==1){
                                Toast.makeText(SingleOrderActivity.this, "Order has been sent to a biker!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SingleOrderActivity.this, "No biker available.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
            builder.show();

        });

    }


    private void loadData() {
        DatabaseReference dishesRef=database.child("restaurateur").child(uid).child("orders").child(order.getOrderId()).child("dishes");
        dishesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Dish fire = new Dish();
                    fire.setName(dataSnapshot1.getKey());
                    fire.setQtySel(Integer.parseInt(dataSnapshot1.getValue().toString()));
                    fire.setPhotoUri(uid+"/"+dataSnapshot1.getKey()+".jpg");
                    dishes.add(fire);

                }
                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void buildRecyclerView() {
        rView = findViewById(R.id.dishes_rView);
        rLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(rLayoutManager);
        adapter = new SimpleRecyclerViewAdapter(this, dishes);
        rView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadData();
        //buildRecyclerView();
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