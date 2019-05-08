package com.example.fooding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef ;
    List<Restaurant> list = new ArrayList<>();
    RecyclerView recycle;
    BrowseAdapter adapter;
    private RecyclerView.LayoutManager rLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        recycle = (RecyclerView) findViewById(R.id.restaurant_rView);
        rLayoutManager = new LinearLayoutManager(this);
        recycle.setLayoutManager(rLayoutManager);

        //adapter.setClickListener(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("restaurateur").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Restaurant fire = new Restaurant();
                    fire.setName(dataSnapshot1.child("name").getValue().toString());
                    fire.setType(dataSnapshot1.child("type").getValue().toString());
                    fire.setPhotoUrl("prova");
                    list.add(fire);

                }

                adapter = new BrowseAdapter(BrowseActivity.this, list);
                recycle.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });





    }
}