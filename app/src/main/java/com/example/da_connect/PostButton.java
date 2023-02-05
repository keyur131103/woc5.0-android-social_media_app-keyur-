package com.example.da_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

public class PostButton extends AppCompatActivity {

    MaterialButton post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_button);

        post = findViewById(R.id.post_button);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPostActivity();

            }
        });

    }

    private void openPostActivity() {

        Intent intent = new Intent(this,PostActivity.class);
        startActivity(intent);

    }
}