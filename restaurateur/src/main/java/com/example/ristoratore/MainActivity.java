package com.example.ristoratore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String URI_PREFS = "uri_prefs";
    public static final String NAME_PREFS = "name_prefs";

    /*private Button profile_btn;
    private Button dailyoffer_btn;
    private Button orders_btn;*/
    SharedPreferences preferences;
    private CircleImageView avatar;
    private TextView name_tv;
    private DrawerLayout mDrawerLayout;
    private NavigationView nv;
    ActionBarDrawerToggle abdToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(navSelectListener);
        View header=nv.getHeaderView(0);
        avatar=header.findViewById(R.id.avatar);
        name_tv=header.findViewById(R.id.textView);
        if(preferences.contains(EditActivity.URI_PREFS)) {
            avatar.setImageURI(Uri.parse(preferences.getString(EditActivity.URI_PREFS, "")));
            if (avatar.getDrawable() == null)
                avatar.setImageResource(R.drawable.ic_launcher_foreground);
        }
        if(preferences.contains(EditActivity.NAME_PREFS))
            name_tv.setText(preferences.getString(NAME_PREFS, ""));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        abdToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        R.string.drawer_open,
                        R.string.drawer_close
                )
        {
        };
        mDrawerLayout.addDrawerListener(abdToggle);
        abdToggle.syncState();



        /*OLD VERSION*/
        /*profile_btn = findViewById(R.id.profile_btn);
        dailyoffer_btn = findViewById(R.id.dailyoffer_btn);
        orders_btn = findViewById(R.id.order_btn);

        profile_btn.setOnClickListener(e -> {
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
        });

        dailyoffer_btn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DailyOfferActivity.class);
            startActivity(i);
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main_drawer);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(navSelectListener);
        View header=nv.getHeaderView(0);
        avatar=header.findViewById(R.id.avatar);
        name_tv=header.findViewById(R.id.textView);
        if(preferences.contains(EditActivity.URI_PREFS)) {
            avatar.setImageURI(Uri.parse(preferences.getString(EditActivity.URI_PREFS, "")));
            if (avatar.getDrawable() == null)
                avatar.setImageResource(R.drawable.ic_launcher_foreground);
        }
        if(preferences.contains(EditActivity.NAME_PREFS))
            name_tv.setText(preferences.getString(NAME_PREFS, ""));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        abdToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        R.string.drawer_open,
                        R.string.drawer_close
                )
        {
        };
        mDrawerLayout.addDrawerListener(abdToggle);
        abdToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (abdToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private NavigationView.OnNavigationItemSelectedListener navSelectListener = new NavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            choiceActivity((String)item.getTitle());
            item.setChecked(true);
            mDrawerLayout.closeDrawer(nv);
            return true;
        }
    };

    private void choiceActivity(String title) {
        if(title.equals("PROFILE")){
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
        }
        else if(title.equals("DAILY OFFER")){
            Intent i = new Intent(getApplicationContext(), DailyOfferActivity.class);
            startActivity(i);
        }

        else if(title.equals("ORDERS")){
            Intent i = new Intent(getApplicationContext(), OrderActivity.class);
            startActivity(i);
        }
    }
}
