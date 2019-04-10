package com.example.ristoratore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ristoratore.menu.Dish;

public class AddDishActivity extends AppCompatActivity {

    private Dish dish;

    private ImageView photo;
    private EditText desc_et;
    private EditText price_et;
    private EditText qty_et;
    private Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_descriptor);

        photo = findViewById(R.id.dish_photo_iv);
        desc_et = findViewById(R.id.dish_desc_et);
        price_et = findViewById(R.id.dish_price_et);
        qty_et = findViewById(R.id.dish_qty_et);
        save_btn = findViewById(R.id.save_dish_btn);

        save_btn.setOnClickListener(v -> {
            String description = desc_et.toString();
            ImageView photo = this.photo;
            float price = Float.parseFloat(price_et.getText().toString());
            int qty = Integer.parseInt(qty_et.getText().toString());

            dish = new Dish(description, photo, price, qty);
            Log.d("MAD-dish", "Created dish: " + dish.getPhoto().toString() + dish.getDescription() +
                                                            dish.getPrice() + dish.getAvailable_qty());
        });
    }

}
