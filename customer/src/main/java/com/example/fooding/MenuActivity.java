package com.example.fooding;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView rView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;
    ArrayList<Dish> dishes= new ArrayList<>();
    private int position;
    private Restaurant restaurant;
    private TextView name;
    private TextView address;
    private TextView workHours;
    private TextView info;
    private ImageView restaurantPhoto;
    public float total;
    private TextView total_tv;

    private Order order;
    private String address_customer;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String uid;
    private String uidcust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        total=0;

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        Intent i = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        position = i.getIntExtra("position",0);
        restaurant=(Restaurant) i.getSerializableExtra("restaurant");

        total_tv = findViewById(R.id.total_tv);
        address = findViewById(R.id.address);
        workHours = findViewById(R.id.work_hours);
        info = findViewById(R.id.info);
        name = findViewById(R.id.name);
        restaurantPhoto=findViewById(R.id.photo_iv);

        uidcust=FirebaseAuth.getInstance().getCurrentUser().getUid();
        uid=restaurant.getUid();
        name.setText(restaurant.getName());

        myRef.child("customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    address_customer=(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.child("restaurateur").child(uid).child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.getValue() == null))
                    address.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.child("restaurateur").child(uid).child("work_hours").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.getValue() == null))
                    workHours.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.child("restaurateur").child(uid).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.getValue() == null))
                    info.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        StorageReference photoRef= FirebaseStorage.getInstance().getReference().child(restaurant.getUri());
        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(restaurantPhoto);
            }
        });

        loadData();


        DatabaseReference orderRef=myRef.child("restaurateur").child(uid).child("orders").push();
        order=new Order(orderRef.getKey(), 0, new ArrayList<Dish>(), null, null, null, null, (long) 0);
        FloatingActionButton order_btn = findViewById(R.id.order_btn);

        order_btn.setOnClickListener(view -> {
            view=getLayoutInflater().inflate(R.layout.dialog_finish_order, null);
            final CharSequence[] choices = { "Yes", "No"};
            AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this).create();
            alertDialog.setTitle("Complete the order");

            final EditText notes_et=view.findViewById(R.id.notes_et);
            final DatePicker date_picker=view.findViewById(R.id.date_picker);
            final TimePicker time_picker=view.findViewById(R.id.time_picker);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "MAKE ORDER", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Calendar calendar=new GregorianCalendar(date_picker.getYear(), date_picker.getMonth(), date_picker.getDayOfMonth(), time_picker.getCurrentHour(), time_picker.getCurrentMinute());
                    orderRef.child("status").setValue("0");
                    orderRef.child("info").setValue(notes_et.getText().toString());
                    orderRef.child("address").setValue(address_customer);
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
                    orderRef.child("deliveryTime").setValue(sdf.format(calendar.getTime()));
                    orderRef.child("priceL").setValue(total_tv.getText().toString());
                    for(Dish dish : order.dishList){
                        orderRef.child("dishes").child(dish.getName()).setValue(dish.getQtySel());
                    }
                    alertDialog.dismiss();
                    Toast.makeText(MenuActivity.this, "Order sent!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            });


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });


            alertDialog.setView(view);
            alertDialog.show();
        });




    }

    private void loadData() {
        myRef.child("restaurateur").child(uid).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Dish fire = new Dish();
                    fire.setName(dataSnapshot1.getKey());
                    fire.setPrice(dataSnapshot1.child("price").getValue().toString());
                    fire.setPriceL(Long.parseLong(dataSnapshot1.child("priceL").getValue().toString()));
                    fire.setDescription(dataSnapshot1.child("description").getValue().toString());
                    fire.setPhotoUri(uid+"/"+dataSnapshot1.getKey()+".jpg");
                    fire.setQtySel(0);
                    dishes.add(fire);

                }
                buildRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    private void buildRecyclerView() {
        rView = findViewById(R.id.dishes_rView);
        rLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(rLayoutManager);
        adapter = new RecyclerViewAdapter(this, dishes, total, total_tv, order);
        rView.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }
}
