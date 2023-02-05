package com.example.da_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logout(View view) {
        auth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

    }

}