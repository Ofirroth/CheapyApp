package com.example.cheapy;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView checkmarkIcon;
    private TextView confirmationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        progressBar = findViewById(R.id.progress_loading);
        checkmarkIcon = findViewById(R.id.checkmark_icon);
        confirmationText = findViewById(R.id.confirmation_text);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the loading spinner and show the checkmark icon and confirmation text
                progressBar.setVisibility(View.GONE);
                checkmarkIcon.setVisibility(View.VISIBLE);
                confirmationText.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }
}
