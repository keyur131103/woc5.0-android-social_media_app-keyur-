package com.example.da_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    EditText name,contact,email,password;
    Button signup,signin;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = name.getText().toString();
                String ContactNo = contact.getText().toString();
                String EMail = email.getText().toString();
                String Password = password.getText().toString();

                if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(ContactNo) || TextUtils.isEmpty(EMail) || TextUtils.isEmpty(Password)) {

                    mAuth.createUserWithEmailAndPassword(EMail,Password).addOnCompleteListener(new MediaPlayer.OnCompletionListener<AuthResult>())
                            @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendtoMain();
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SignUpActivity.this, "Error : "+error, Toast.LENGTH_SHORT).show();
                            }

                    }

                }else {

                    Toast.makeText(SignUpActivity.this, "Please fill All Fields!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void sendtoMain() {

        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            sendtoMain();
        }

    }

}
