package com.example.fooding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    private CircleImageView avatar;
    private TextView name_tv;
    private TextView addr_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        avatar = findViewById(R.id.avatar);
        name_tv = findViewById(R.id.name_text);
        addr_tv = findViewById(R.id.address_text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(preferences.contains(EditActivity.URI_PREFS))
            avatar.setImageURI(Uri.parse(preferences.getString(EditActivity.URI_PREFS, "")));
        if(preferences.contains(EditActivity.NAME_PREFS))
            name_tv.setText(preferences.getString(EditActivity.NAME_PREFS, ""));
        if(preferences.contains(EditActivity.ADDR_PREFS))
            addr_tv.setText(preferences.getString(EditActivity.ADDR_PREFS, ""));
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
        return true;
    }
}
