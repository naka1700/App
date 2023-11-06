package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button sendButton = findViewById(R.id.send_button);
        // lambda式
        sendButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MapsActivity.class);
            startActivity(intent);
        });

        Button searchButton = findViewById(R.id.search_button);
        // lambda式
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), SearchActivity.class);
            startActivity(intent);
        });

        Button myrouteButton = findViewById(R.id.myroute_button);
        // lambda式
        myrouteButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MyrouteActivity.class);
            startActivity(intent);
        });
    }


}
