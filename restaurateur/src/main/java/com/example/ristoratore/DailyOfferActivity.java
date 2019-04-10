package com.example.ristoratore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class DailyOfferActivity extends AppCompatActivity {

    private Button addDish_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyoffer);

        addDish_btn.findViewById(R.id.add_dish_btn);
        addDish_btn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DailyOfferActivity.class);
            startActivity(i);
        });
    }

}
