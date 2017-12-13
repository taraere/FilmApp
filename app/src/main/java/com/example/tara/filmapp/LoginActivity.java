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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailLogIn;
    private EditText editTextPasswordLogIn;
    private Button   buttonSignIn;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmailLogIn = (EditText) findViewById(R.id.editTextEmailLogIn);
        editTextPasswordLogIn = (EditText) findViewById(R.id.editTextPasswordLogIn);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        // check user is not already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            // already logged in
            // start profile activity
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignIn) {
            // take to profile Activity if information is filled in correctly
            logInUser();
        }
        if (v == textViewSignUp) {
            // close this activity
            finish();
            // switch to MainActivity
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void logInUser() {
        String email = editTextEmailLogIn.getText().toString().trim();
        String password = editTextPasswordLogIn.getText().toString().trim();

        // check if filled in
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

        // valid for logging in
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        // begin login
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
