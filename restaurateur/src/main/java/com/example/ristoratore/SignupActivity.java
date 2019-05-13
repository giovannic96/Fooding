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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email_et;
    private EditText password_et;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        signup_btn = findViewById(R.id.register_btn);

        signup_btn.setOnClickListener(e -> {
            if (password_et.getText().toString().length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (email_et.getText().toString().isEmpty()) {
                Toast.makeText(this, "E-mail field is empty.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email_et.getText().toString(), password_et.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid=user.getUid();
                        DatabaseReference database= FirebaseDatabase.getInstance().getReference();
                        database.child("restaurateur").child(uid).setValue("R");
                        Toast.makeText(SignupActivity.this, "Authentication successful.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser =mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, "User already logged in.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
