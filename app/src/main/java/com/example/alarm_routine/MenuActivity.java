package com.example.alarm_routine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardView_Profile, cardView_Alarm, cardView_Routine, cardView_Games;
    private TextView textViewShowEmail;

    //private TextView textViewSignout;

    //private Button buttonLogout;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        textViewShowEmail = (TextView) findViewById(R.id.tv_show_email);
        //buttonLogout = findViewById(R.id.btn_logout);

        cardView_Profile = (CardView) findViewById(R.id.CardView_Profile);
        cardView_Alarm = (CardView) findViewById(R.id.CardView_Alarm);
        cardView_Routine = (CardView) findViewById(R.id.CardView_Routine);
        cardView_Games = (CardView) findViewById(R.id.CardView_Games);

        //textViewSignout = (TextView) findViewById(R.id.tv_signout);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        cardView_Profile.setOnClickListener(this);
        cardView_Alarm.setOnClickListener(this);
        cardView_Routine.setOnClickListener(this);
        cardView_Games.setOnClickListener(this);

        textViewShowEmail.setOnClickListener(this);
        textViewShowEmail.setText(user.getEmail());
        //buttonLogout.setOnClickListener(this);

        //textViewSignout.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {

        if (view == cardView_Profile){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        if (view == cardView_Alarm){
            finish();
            startActivity(new Intent(getApplicationContext(), AlarmMainActivity.class));
        }

        if (view == cardView_Routine){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        if (view == cardView_Games){
            finish();
            startActivity(new Intent(getApplicationContext(), PuzzleMainActivity.class));
        }

        if (view == textViewShowEmail){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        /*if (view == textViewSignout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }*/

    }
}
