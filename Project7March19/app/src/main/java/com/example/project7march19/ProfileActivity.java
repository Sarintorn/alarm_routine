package com.example.alarm_routine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private TextView textViewUser;
    private Button btnSignout;
    private EditText editTextFname, editTextNname, editTextUsername;
    private Button btnSave;

    ImageView ImgUserPhoto;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        editTextFname = (EditText) findViewById(R.id.editTextFname);
        editTextNname = (EditText) findViewById(R.id.editTextNname);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        btnSave = (Button) findViewById(R.id.btnSave);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUser = (TextView) findViewById(R.id.textViewUser);
        textViewUser.setText("Welcome " + user.getEmail());

        btnSignout = (Button) findViewById(R.id.btnSignout);
        ImgUserPhoto = findViewById(R.id.imgUserPhoto);

        btnSignout.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        ImgUserPhoto.setOnClickListener(this);
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
                ImgUserPhoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadfile() {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
            mStorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //if the upload is successfull
                    //hiding the progress dialog
                    progressDialog.dismiss();

                    //and displaying a success toast
                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void  saveUserInformation(){
        String fname = editTextFname.getText().toString().trim();
        String nname = editTextNname.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        UserInformation userInformation = new UserInformation(fname, nname, username);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information saved...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view == btnSignout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (view == btnSave){
            saveUserInformation();
            uploadfile();
        }

        if (view == ImgUserPhoto){
            showFileChooser();
        }

    }
}