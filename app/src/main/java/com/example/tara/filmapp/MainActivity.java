package com.example.tara.filmapp;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    // Firebase authentication object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialise objects

        editTextEmail       = (EditText)  findViewById(R.id.editTextEmail);
        editTextPassword    = (EditText)  findViewById(R.id.editTextPassword);
        buttonSignUp        = (Button)    findViewById(R.id.buttonSignUp);
        textViewSignIn      = (TextView)  findViewById(R.id.textViewSignIn);

        progressDialog      = new ProgressDialog(this);
        firebaseAuth        = FirebaseAuth.getInstance();

        // check user is not already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            // already logged in then start profile activity
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        buttonSignUp.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignUp) {
            registerUser();
        }

        if (v == textViewSignIn) {
            // open signin activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            // email is empty
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            // stop from executing any further
            return;
        }

        if(TextUtils.isEmpty(password)) {
            // password is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        // valid to register

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        // register user to server. Creates user with email and password info
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // user is successfully logged in
                            // start profile activity of user
                            Toast.makeText(MainActivity.this, "Successful "+ task.toString(), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
