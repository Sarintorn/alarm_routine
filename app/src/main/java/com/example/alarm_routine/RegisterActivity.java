package com.example.alarm_routine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSignup;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private EditText editTextFullname, editTextUsername;
    private TextView textViewSignin;

    /*private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");*/

    private ImageView imageViewUserPhoto;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        imageViewUserPhoto = findViewById(R.id.imgUserPhoto);
        btnSignup = findViewById(R.id.btnSignup);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextFullname = findViewById(R.id.editTextFname);
        editTextUsername = findViewById(R.id.editTextUsername);
        textViewSignin = findViewById(R.id.textViewSignin);

        btnSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        imageViewUserPhoto.setOnClickListener(this);
    }

    /*private boolean validateEmail() {
        String emailInput = editTextEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            editTextEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = editTextUsername.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            editTextUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            editTextUsername.setError("Username too long");
            return false;
        } else {
            editTextUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = editTextPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            editTextPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            editTextPassword.setError("Password too weak");
            return false;
        } else {
            editTextPassword.setError(null);
            return true;
        }
    }*/

    private void signupUser() {

        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmpassword = editTextConfirmPassword.getText().toString().trim();

        if (email.isEmpty()) {
            //editTextEmail.setError("กรุณากรอกอีเมล");
            Toast.makeText(this, "กรุณากรอกอีเมล", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //editTextEmail.setError("กรุณาตรวจสอบอีเมล");
            Toast.makeText(this, "กรุณาตรวจสอบอีเมล", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()){
            //editTextPassword.setError("กรุณากรอกรหัสผ่าน");
            Toast.makeText(this, "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            return;
        }else if (password.length() >= 6){
            Toast.makeText(this, "รหัสผ่านต้องไม่ต่ำกว่า6ตัว", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(confirmpassword)){
            Toast.makeText(this, "กรุณากรอกยืนยันรหัสผ่าน", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmpassword)){
            Toast.makeText(this, "รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
            return;
        }

        //if validations are ok
        //we will first show a progressbar
        progressDialog.setMessage("กำลังลงทะเบียน...");
        progressDialog.show();

        final Context context = this;

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            if (filePath != null) {

                                final ProgressDialog progressDialog = new ProgressDialog(context);

                                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
                                final StorageReference imageFilePath = mStorage.child(filePath.getLastPathSegment());
                                imageFilePath.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        /*if (!validateEmail() | !validateUsername() | !validatePassword()) {
                                            return;
                                        }*/
                                        String email = editTextEmail.getText().toString().trim();
                                        String password = editTextPassword.getText().toString().trim();
                                        String confirmpassword = editTextConfirmPassword.getText().toString().trim();
                                        String fname = editTextFullname.getText().toString().trim();
                                        String username = editTextUsername.getText().toString().trim();

                                        if (TextUtils.isEmpty(email)) {
                                            Toast.makeText(context, "กรุณากรอกอีเมลแอดเดรส", Toast.LENGTH_SHORT).show();
                                        }
                                        if (TextUtils.isEmpty(password)){
                                            Toast.makeText(context,"กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
                                        }
                                        if (TextUtils.isEmpty(confirmpassword)){
                                            Toast.makeText(context,"กรุณายืนยันรหัสผ่าน", Toast.LENGTH_SHORT).show();
                                        }
                                        if (TextUtils.isEmpty(fname)){
                                            Toast.makeText(context,"กรุณากรอกชื่อ-นามสกุล", Toast.LENGTH_SHORT).show();
                                        }
                                        if (TextUtils.isEmpty(username)){
                                            Toast.makeText(context,"กรุณากรอกชื่อผู้ใช้", Toast.LENGTH_SHORT).show();
                                        }

                                        UserInformation userInformation = new UserInformation(email, fname, username);
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        //databaseReference.child(user.getUid()).setValue(userInformation);
                                        databaseReference.child("users").child(user.getUid());
                                        databaseReference.child("users").push();
                                        databaseReference.child("users").child(user.getUid()).setValue(userInformation);
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "อัพโหลดไฟล์สำเร็จ", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            Toast.makeText(RegisterActivity.this, "ลงทะเบียนสำเร็จ",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterActivity.this, "ไม่สามารถลงทะเบียนได้ กรุณาลองอีกครั้ง",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewUserPhoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View view) {

        if (view == btnSignup){
            signupUser();
        }

        if (view == imageViewUserPhoto){
            showFileChooser();
        }

        if (view == textViewSignin){
            startActivity(new Intent(this, LoginActivity.class));
        }

    }

}
