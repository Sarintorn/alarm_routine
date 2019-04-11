package com.example.alarm_routine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSignup;
    private EditText editTextEmail, editTextPassword, editTextPassword2;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        btnSignup = (Button) findViewById(R.id.btnSignup);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);

        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        btnSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void signupUser() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword.getText().toString().trim();

        //checkinf if email and password are empty

        /*if (password.equals(password2)){
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
                //email and password is empty or password and password2 not equals
                Toast.makeText(this, "Please Verify all Fields", Toast.LENGTH_SHORT).show();
                //Stopping the function execute further
                return;
            }else(!password.equals(password2)) {
                Toast.makeText(this, "Password Not Matching", Toast.LENGTH_SHORT).show();
            }
        //}*/

        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //Stopping the function execute further
            return;
        }

        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //Stopping the function execute further
            return;
        }

        if (TextUtils.isEmpty(password2)){
            //password is empty
            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            //Stopping the function execute further
            return;
        }

        if (password.equals(password2)){
            Toast.makeText(this, "Password and Confirm password not match", Toast.LENGTH_SHORT).show();
            //Stopping the function execute further
            return;
        }

        //if validations are ok
        //we will first show a progressbar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            //user is successfully registed and logged in
                            //we will start the profile activity here
                            //right now lets display a toast only
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                            /*Toast.makeText(MainActivity.this, "Registered Successfully",
                                    Toast.LENGTH_SHORT).show();*/
                        }else {
                            Toast.makeText(MainActivity.this, "Could not register. please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }


    @Override
    public void onClick(View view) {

        if (view == btnSignup){

            signupUser();

        }

        if (view == textViewSignin){

            //will open login activity here
            startActivity(new Intent(this, LoginActivity.class));

        }

    }

}
