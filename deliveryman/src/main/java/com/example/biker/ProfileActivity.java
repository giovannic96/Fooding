package com.example.biker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference database;
    private StorageReference storage;
    SharedPreferences preferences;
    private CircleImageView avatar;
    private TextView name_tv;
    private TextView tel_tv;
    private TextView mail_tv;
    private TextView hour_tv;
    private TextView area_tv;
    private TextView info_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        database= FirebaseDatabase.getInstance().getReference();
        storage= FirebaseStorage.getInstance().getReference();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        avatar = findViewById(R.id.avatar);
        name_tv = findViewById(R.id.name_text);
        tel_tv = findViewById(R.id.tel_text);
        mail_tv = findViewById(R.id.mail_text);
        hour_tv = findViewById(R.id.hour_text);
        area_tv = findViewById(R.id.area_text);
        info_tv = findViewById(R.id.info_text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //OLD VERSION WITH PREFERENCES
        /*if(preferences.contains(EditActivity.URI_PREFS)) {
            avatar.setImageURI(Uri.parse(preferences.getString(EditActivity.URI_PREFS, "")));
            if(avatar.getDrawable() == null)
                avatar.setImageResource(R.mipmap.deliveryman);
        }
        if(preferences.contains(EditActivity.NAME_PREFS))
            name_tv.setText(preferences.getString(EditActivity.NAME_PREFS, ""));
        if(preferences.contains(EditActivity.TEL_PREFS))
            tel_tv.setText(preferences.getString(EditActivity.TEL_PREFS, ""));
        if(preferences.contains(EditActivity.MAIL_PREFS))
            mail_tv.setText(preferences.getString(EditActivity.MAIL_PREFS, ""));
        if(preferences.contains(EditActivity.HOUR_PREFS))
            hour_tv.setText(preferences.getString(EditActivity.HOUR_PREFS, ""));
        if(preferences.contains(EditActivity.AREA_PREFS))
            area_tv.setText(preferences.getString(EditActivity.AREA_PREFS, ""));
        if(preferences.contains(EditActivity.INFO_PREFS))
            info_tv.setText(preferences.getString(EditActivity.INFO_PREFS, ""));*/


        String uid=currentUser.getUid();
        mail_tv.setText(currentUser.getEmail());
        database.child("biker").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    name_tv.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("work_hours").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    hour_tv.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("telephone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    tel_tv.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("work_area").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    area_tv.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    info_tv.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_mode) {
            Intent i = new Intent(getApplicationContext(), EditActivity.class);
            startActivity(i);
        }
        else
            finish();
        return true;
    }
}
