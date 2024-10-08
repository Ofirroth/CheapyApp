package com.example.cheapy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Home_page.HomePageActivity;

public class MainActivity extends AppCompatActivity {

    private static final int DELAY_TIME_MS = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use a Handler to introduce a delay
        new Handler().postDelayed(() -> {
            // Start the LoginActivity after the delay
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);

            startActivity(intent);
            finish();
        }, DELAY_TIME_MS);
    }
}
