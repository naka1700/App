package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button sendButton = findViewById(R.id.ホームに戻る_button);
        // lambda式
        sendButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        });
    }


}
