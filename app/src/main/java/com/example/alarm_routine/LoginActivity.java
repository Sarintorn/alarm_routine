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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignin;
    private EditText editTextEmail, editTextPassword;
    private TextView textViewSignup;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }

        editTextEmail = (EditText) findViewById(R.id.edt_email_login);
        editTextPassword = (EditText) findViewById(R.id.edt_password_login);

        btnSignin = (Button) findViewById(R.id.btnSignin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);

        progressDialog = new ProgressDialog(this);

        btnSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }

    private void userSignin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "กรุณากรอกอีเมล", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            return;
        }
        //if validations are ok
        //we will first show a progressbar
        progressDialog.setMessage("เข้าสู่ระบบ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == btnSignin){
            userSignin();
        }

        if (view == textViewSignup){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

}
