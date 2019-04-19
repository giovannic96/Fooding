package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ristoratore.menu.Dish;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddDishActivity extends AppCompatActivity {

    private static final int GALLERY_REQCODE = 31;
    private static final int CAM_REQCODE = 32;
    private static final int STORAGE_PERM_CODE = 33;

    private Dish dish;

    private ImageView photo;
    private EditText name_et;
    private EditText desc_et;
    private CurrencyEditText price_et;
    private Uri selectedPhoto;
    private ElegantNumberButton qty_inc_dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_descriptor);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        photo = findViewById(R.id.dish_photo_iv);
        name_et = findViewById(R.id.dish_name_et);
        desc_et = findViewById(R.id.dish_desc_et);
        price_et = findViewById(R.id.dish_price_et);
        ImageView add_image_btn = findViewById(R.id.add_image_btn);
        Button save_btn = findViewById(R.id.save_dish_btn);
        qty_inc_dec = findViewById(R.id.qty_inc_dec);

        //name_et.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        //desc_et.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        if (savedInstanceState != null) {
            if(savedInstanceState.containsKey("uri_photo")) {
                selectedPhoto = savedInstanceState.getParcelable("uri");
                photo.setImageURI(selectedPhoto);
            }
        }

        add_image_btn.setOnClickListener(e -> {
            if(isStoragePermissionGranted())
                selectImage();
        });

        qty_inc_dec.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
        });

        save_btn.setOnClickListener(v -> {
            String name = name_et.getText().toString();
            String description = desc_et.getText().toString();
            ImageView photo = this.photo;
            Long priceLong = price_et.getRawValue();
            String price = price_et.formatCurrency(price_et.getRawValue());
            int qty_tv = Integer.parseInt(qty_inc_dec.getNumber());

            dish = new Dish(name, description, photo, price, priceLong, qty_tv, selectedPhoto != null ? selectedPhoto.toString() : "");
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(selectedPhoto != null)
            outState.putParcelable("uri_photo", selectedPhoto);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23)
            if (checkPermission())
                return true;
            else
                requestStoragePermission();
        else
            return true;
        return false;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDishActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                cameraIntent();
            } else if (items[item].equals("Choose from Library")) {
                galleryIntent();
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @SuppressLint("IntentReset")
    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, GALLERY_REQCODE);
    }

    private void cameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        selectedPhoto = FileProvider.getUriForFile(AddDishActivity.this, BuildConfig.APPLICATION_ID + ".provider", Objects.requireNonNull(getOutputMediaFile()));
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhoto);
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(takePicture, CAM_REQCODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case CAM_REQCODE:
                if (resultCode == RESULT_OK) {
                    photo.setImageURI(selectedPhoto);
                }
                break;
            case GALLERY_REQCODE:
                if (imageReturnedIntent != null && resultCode == RESULT_OK) {
                    selectedPhoto = imageReturnedIntent.getData();
                    photo.setImageURI(selectedPhoto);
                }
                break;
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MAD-photo", "Oops! Failed create directory");
                return null;
            }
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg");
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddDishActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to store images")
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(AddDishActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERM_CODE))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERM_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("dish_item", dish);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }
}
