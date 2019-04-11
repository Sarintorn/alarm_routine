package com.example.alarm_routine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignout, buttonMenu;


    //private static final String TAG = "ProfileActivity";

    private FirebaseAuth firebaseAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    //UI reference


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonMenu = findViewById(R.id.btn_menu);
        buttonSignout = findViewById(R.id.btn_signout);

        buttonMenu.setOnClickListener(this);
        buttonSignout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == buttonMenu){
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }

        if (view == buttonSignout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}