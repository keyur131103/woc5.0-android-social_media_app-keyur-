package com.example.da_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class DashBoard extends AppCompatActivity {

    MaterialButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        MaterialButton logout = (MaterialButton) findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSignInActivity();

            }
        });

    }

    private void openSignInActivity() {
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }
}