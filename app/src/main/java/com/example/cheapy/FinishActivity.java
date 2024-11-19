package com.example.cheapy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cheapy.Home_page.HomePageActivity;

public class FinishActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView checkmarkIcon;
    private TextView confirmationText;
    private String activeUserName;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        progressBar = findViewById(R.id.progress_loading);
        checkmarkIcon = findViewById(R.id.checkmark_icon);
        confirmationText = findViewById(R.id.confirmation_text);
        Intent intent = getIntent();
        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the loading spinner and show the checkmark icon and confirmation text
                progressBar.setVisibility(View.GONE);
                checkmarkIcon.setVisibility(View.VISIBLE);
                confirmationText.setVisibility(View.VISIBLE);
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(FinishActivity.this, HomePageActivity.class);
                homeIntent.putExtra("activeUserName", activeUserName);
                homeIntent.putExtra("token", userToken);
                startActivity(homeIntent);
            }
        }, 2500);

    }
}
