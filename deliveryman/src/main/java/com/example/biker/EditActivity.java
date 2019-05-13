package com.example.biker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public static final String TEL_PREFS = "tel_prefs";
    public static final String MAIL_PREFS = "mail_prefs";
    public static final String HOUR_PREFS = "hour_prefs";
    public static final String AREA_PREFS = "area_prefs";
    public static final String INFO_PREFS = "info_prefs";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference database;
    private StorageReference storage;
    private StorageReference photoref;
    private CircleImageView avatar;
    private ImageView addImage;
    private Button save_btn;
    private EditText name_et;
    private EditText tel_et;
    private EditText mail_et;
    private EditText hour_et;
    private EditText area_et;
    private EditText info_et;
    private Uri selectedImage;
    private String uid;
    //SharedPreferences preferences;
    //SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        storage= FirebaseStorage.getInstance().getReference();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        avatar = findViewById(R.id.avatar);
        save_btn = findViewById(R.id.avatar_btn);
        name_et = findViewById(R.id.name_et);
        tel_et = findViewById(R.id.tel_et);
        mail_et = findViewById(R.id.mail_et);
        hour_et = findViewById(R.id.hour_et);
        area_et = findViewById(R.id.area_et);
        info_et = findViewById(R.id.info_et);
        addImage = findViewById(R.id.add_image_btn);

        //////OLD VERSION WITH SHARED PREFERENCES ////////

        /*preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        if(preferences.contains(EditActivity.URI_PREFS)) {
            avatar.setImageURI(Uri.parse(preferences.getString(EditActivity.URI_PREFS, "")));
            if (avatar.getDrawable() == null)
                avatar.setImageResource(R.mipmap.deliveryman);
        }
        if(preferences.contains(EditActivity.NAME_PREFS))
            name_et.setText(preferences.getString(NAME_PREFS, ""));
        if(preferences.contains(EditActivity.TEL_PREFS))
            tel_et.setText(preferences.getString(TEL_PREFS, ""));
        if(preferences.contains(EditActivity.MAIL_PREFS))
            mail_et.setText(preferences.getString(MAIL_PREFS, ""));
        if(preferences.contains(EditActivity.HOUR_PREFS))
            hour_et.setText(preferences.getString(HOUR_PREFS, ""));
        if(preferences.contains(EditActivity.AREA_PREFS))
            area_et.setText(preferences.getString(AREA_PREFS, ""));
        if(preferences.contains(EditActivity.INFO_PREFS))
            info_et.setText(preferences.getString(INFO_PREFS, ""));*/

        uid=currentUser.getUid();
        photoref=storage.child(uid+"/photo.jpg");
        photoref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(avatar);
            }
        });
        mail_et.setText(currentUser.getEmail());
        database.child("biker").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    name_et.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("work_hours").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    hour_et.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("telephone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    tel_et.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("work_area").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    area_et.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child("biker").child(uid).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.getValue()==null))
                    info_et.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

            ////////OLD VERSION WITH SHARED PREFERENCES//////////

            /*if(!(name_et.getText().toString().equals(preferences.getString(NAME_PREFS, "")))) {
                editor.putString(NAME_PREFS, name_et.getText().toString());
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
            if(!(area_et.getText().toString().equals(preferences.getString(AREA_PREFS, "")))) {
                editor.putString(AREA_PREFS, area_et.getText().toString());
                editor.apply();
            }
            if(!(info_et.getText().toString().equals(preferences.getString(INFO_PREFS, "")))) {
                editor.putString(INFO_PREFS, info_et.getText().toString());
                editor.apply();
            }
            if(selectedImage != null && !(selectedImage.toString().equals(preferences.getString(URI_PREFS, "")))) {
                editor.putString(URI_PREFS, selectedImage.toString());
                editor.apply();
            }*/

            currentUser.updateEmail(mail_et.getText().toString());
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name_et.getText().toString()).build();
            currentUser.updateProfile(profileUpdates);

            String description = info_et.getText().toString();
            if(description.isEmpty())
            {
                Toast.makeText(this, "Description field is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            String tel = tel_et.getText().toString();
            if(tel.isEmpty())
            {
                Toast.makeText(this, "Telephone field is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            String work_hours = hour_et.getText().toString();
            if(work_hours.isEmpty())
            {
                Toast.makeText(this, "Work hours field is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            String work_area = area_et.getText().toString();
            if(work_area.isEmpty())
            {
                Toast.makeText(this, "Address field is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            database.child("biker").child(uid).child("work_hours").setValue(hour_et.getText().toString());
            database.child("biker").child(uid).child("telephone").setValue(tel_et.getText().toString());
            database.child("biker").child(uid).child("work_area").setValue(area_et.getText().toString());
            database.child("biker").child(uid).child("info").setValue(info_et.getText().toString());
            database.child("biker").child(uid).child("name").setValue(name_et.getText().toString());

            if (selectedImage != null){
                AssetFileDescriptor afd = null;
                try {
                    afd = getContentResolver().openAssetFileDescriptor(selectedImage, "r");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                long fileSize = afd.getLength();



                if(fileSize>=1000000) {
                    try {
                        Bitmap bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage), 640, 480, true);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = photoref.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(EditActivity.this, "Upload failure!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                Toast.makeText(EditActivity.this, "Upload successful!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                else {

                    UploadTask uploadTask = photoref.putFile(selectedImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(EditActivity.this, "Upload failure.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            Toast.makeText(EditActivity.this, "Upload successful!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //finish();
            }
            else{
                Toast.makeText(EditActivity.this, "Upload successful!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        currentUser =mAuth.getCurrentUser();
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //if(selectedImage != null && !(selectedImage.toString().equals(preferences.getString(URI_PREFS, ""))))
            //outState.putParcelable("uri", selectedImage);
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
