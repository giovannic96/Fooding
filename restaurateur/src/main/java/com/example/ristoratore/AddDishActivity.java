package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.ristoratore.menu.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AddDishActivity extends AppCompatActivity {

    private static final int GALLERY_REQCODE = 31;
    private static final int CAM_REQCODE = 32;
    private static final int STORAGE_PERM_CODE = 33;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference database;
    private StorageReference storage;
    private StorageReference photoref;

    private Dish dish;

    private ImageView photo;
    private EditText name_et;
    private EditText desc_et;
    private EditText qty_et;
    private CurrencyEditText price_et;
    private Uri selectedPhoto;
    private ImageButton qty_inc;
    private ImageButton qty_dec;
    private String uid;
    private String photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_descriptor);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        storage= FirebaseStorage.getInstance().getReference();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        photo = findViewById(R.id.dish_photo_iv);
        name_et = findViewById(R.id.dish_name_et);
        desc_et = findViewById(R.id.dish_desc_et);
        price_et = findViewById(R.id.dish_price_et);
        qty_et = findViewById(R.id.qty_et);
        ImageView add_image_btn = findViewById(R.id.add_image_btn);
        Button save_btn = findViewById(R.id.save_dish_btn);
        qty_inc = findViewById(R.id.qty_inc);
        qty_dec = findViewById(R.id.qty_dec);

        uid=currentUser.getUid();


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

        qty_inc.setOnClickListener(v -> {
            String value = qty_et.getText().toString();
            Integer val = Integer.parseInt(value);
            val++;
            qty_et.setText(String.valueOf(val));
        });

        qty_dec.setOnClickListener(v -> {
            String value = qty_et.getText().toString();
            Integer val = Integer.parseInt(value);
            if(!val.equals(0)) {
                val--;
                qty_et.setText(String.valueOf(val));
            }
        });

        qty_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if(enteredString.equals(""))
                    qty_et.setText("0");
                if (enteredString.startsWith("0") && enteredString.length() > 1) {
                    qty_et.setText(qty_et.getText().toString().substring(1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        save_btn.setOnClickListener(v -> {
            if(selectedPhoto==null)
            {
                Toast.makeText(this, "No photo selected!", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = name_et.getText().toString();
            if(name.isEmpty())
            {
                Toast.makeText(this, "Name field is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            String description = desc_et.getText().toString();
            if(description.isEmpty())
            {
                Toast.makeText(this, "Description field is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            database.child("restaurateur").child(uid).child("menu").child(name).child("description").setValue(description);
            ImageView photo = this.photo;
            Long priceLong = price_et.getRawValue();
            if(priceLong.toString().equals("0"))
            {
                Toast.makeText(this, "Price field is zero!", Toast.LENGTH_SHORT).show();
                return;
            }
            String price = price_et.formatCurrency(price_et.getRawValue());
            database.child("restaurateur").child(uid).child("menu").child(name).child("price").setValue(price);
            int qty;
            if(qty_et.getText().toString().matches("^-?\\d+$"))
                qty = Integer.parseInt(qty_et.getText().toString());
            else
                qty = 1;
            if(qty == 0) {
                Toast.makeText(this, "Quantity is zero!", Toast.LENGTH_SHORT).show();
                return;
            }
            database.child("restaurateur").child(uid).child("menu").child(name).child("quantity").setValue(qty);
            photoref=storage.child(uid+"/"+name+".jpg");
            photoUri=uid+"/"+name+".jpg";
            AssetFileDescriptor afd = null;
            try {
                afd = getContentResolver().openAssetFileDescriptor(selectedPhoto, "r");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            long fileSize = afd.getLength();



            if(fileSize>=1000000) {
                try {
                    Bitmap bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhoto), 640, 480, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = photoref.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(AddDishActivity.this, "Upload failure!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            Toast.makeText(AddDishActivity.this, "Upload successful!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            else {

                UploadTask uploadTask = photoref.putFile(selectedPhoto);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(AddDishActivity.this, "Upload failure.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Toast.makeText(AddDishActivity.this, "Upload successful!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            dish = new Dish(name, description, photo, price, priceLong, qty, photoUri != null ? photoUri : "");
            //finish();
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        currentUser =mAuth.getCurrentUser();
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
