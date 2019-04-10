package com.example.ristoratore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button profile_btn;
    private Button dailyoffer_btn;
    private Button orders_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_btn = findViewById(R.id.profile_btn);
        dailyoffer_btn = findViewById(R.id.dailyoffer_btn);
        orders_btn = findViewById(R.id.order_btn);

        profile_btn.setOnClickListener(e -> {
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
        });

        dailyoffer_btn.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DailyOfferActivity.class);
            startActivity(i);
        });
    }
}
