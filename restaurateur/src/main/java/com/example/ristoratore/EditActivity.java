package com.example.ristoratore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    private static final int CAM_REQ = 10;
    private static final int GALLERY_REQ = 11;
    private static final int STORAGE_PERMISSION_CODE = 100;
    public static final String URI_PREFS = "uri_prefs";
    public static final String NAME_PREFS = "name_prefs";
    public static final String ADDR_PREFS = "address_prefs";
    public static final String TEL_PREFS = "tel_prefs";
    public static final String MAIL_PREFS = "mail_prefs";
    public static final String HOUR_PREFS = "hour_prefs";
    public static final String INFO_PREFS = "info_prefs";

    private CircleImageView avatar;
    private Button addImage;
    private Button save_btn;
    private EditText name_et;
    private EditText addr_et;
    private EditText tel_et;
    private EditText mail_et;
    private EditText hour_et;
    private EditText info_et;
    private Uri selectedImage;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        avatar = findViewById(R.id.avatar);
        save_btn = findViewById(R.id.avatar_btn);
        name_et = findViewById(R.id.name_et);
        addr_et = findViewById(R.id.address_et);
        tel_et = findViewById(R.id.tel_et);
        mail_et = findViewById(R.id.mail_et);
        hour_et = findViewById(R.id.hour_et);
        info_et = findViewById(R.id.info_et);
        addImage = findViewById(R.id.add_image_btn);

        if(preferences.contains(EditActivity.URI_PREFS)) {
            avatar.setImageURI(Uri.parse(preferences.getString(EditActivity.URI_PREFS, "")));
            if (avatar.getDrawable() == null)
                avatar.setImageResource(R.drawable.ic_launcher_foreground);
        }
        if(preferences.contains(EditActivity.NAME_PREFS))
            name_et.setText(preferences.getString(NAME_PREFS, ""));
        if(preferences.contains(EditActivity.ADDR_PREFS))
            addr_et.setText(preferences.getString(ADDR_PREFS, ""));
        if(preferences.contains(EditActivity.TEL_PREFS))
            tel_et.setText(preferences.getString(TEL_PREFS, ""));
        if(preferences.contains(EditActivity.MAIL_PREFS))
            mail_et.setText(preferences.getString(MAIL_PREFS, ""));
        if(preferences.contains(EditActivity.HOUR_PREFS))
            hour_et.setText(preferences.getString(HOUR_PREFS, ""));
        if(preferences.contains(EditActivity.INFO_PREFS))
            info_et.setText(preferences.getString(INFO_PREFS, ""));

        if (savedInstanceState != null){
            if(savedInstanceState.containsKey("uri"))
            {
                selectedImage = savedInstanceState.getParcelable("uri");
                avatar.setImageURI(selectedImage);
            }
        }

        addImage.setOnClickListener(e -> {
            if(isStoragePermissionGranted()) {
                selectImage();
            }
        });

        save_btn.setOnClickListener(e -> {
            if(!(name_et.getText().toString().equals(preferences.getString(NAME_PREFS, "")))) {
                editor.putString(NAME_PREFS, name_et.getText().toString());
                editor.apply();
            }
            if(!(addr_et.getText().toString().equals(preferences.getString(ADDR_PREFS, "")))) {
                editor.putString(ADDR_PREFS, addr_et.getText().toString());
                editor.apply();
            }
            if(!(tel_et.getText().toString().equals(preferences.getString(TEL_PREFS, "")))) {
                editor.putString(TEL_PREFS, tel_et.getText().toString());
                editor.apply();
            }
            if(!(mail_et.getText().toString().equals(preferences.getString(MAIL_PREFS, "")))) {
                editor.putString(MAIL_PREFS, mail_et.getText().toString());
                editor.apply();
            }
            if(!(hour_et.getText().toString().equals(preferences.getString(HOUR_PREFS, "")))) {
                editor.putString(HOUR_PREFS, hour_et.getText().toString());
                editor.apply();
            }
            if(!(info_et.getText().toString().equals(preferences.getString(INFO_PREFS, "")))) {
                editor.putString(INFO_PREFS, info_et.getText().toString());
                editor.apply();
            }
            if(selectedImage != null && !(selectedImage.toString().equals(preferences.getString(URI_PREFS, "")))) {
                editor.putString(URI_PREFS, selectedImage.toString());
                editor.apply();
            }
            finish();
        });
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(selectedImage != null && !(selectedImage.toString().equals(preferences.getString(URI_PREFS, ""))))
            outState.putParcelable("uri", selectedImage);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission())
                return true;
            else
                requestStoragePermission();
        }
        else {
            return true;
        }
        return false;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
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
        startActivityForResult(intent, GALLERY_REQ);
    }

    private void cameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        selectedImage = FileProvider.getUriForFile(EditActivity.this, BuildConfig.APPLICATION_ID + ".provider", Objects.requireNonNull(getOutputMediaFile()));
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(takePicture, CAM_REQ);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case CAM_REQ:
                if (resultCode == RESULT_OK) {
                    avatar.setImageURI(selectedImage);
                }
                break;
            case GALLERY_REQ:
                if (imageReturnedIntent != null && resultCode == RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    avatar.setImageURI(selectedImage);
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
        int result = ContextCompat.checkSelfPermission(EditActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to store images")
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(EditActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
