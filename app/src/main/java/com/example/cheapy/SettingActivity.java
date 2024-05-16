package com.example.cheapy;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.example.cheapy.databinding.ActivitySettingBinding;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private EditText serverAddressEditText;
    private Switch darkModeSwitch;
    private SharedPreferences settingsSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isReturn.getInstance().setIsReturn(false);
        serverAddressEditText = binding.serverAddressEdittext;
        darkModeSwitch = binding.darkModeSwitch;

        settingsSharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        loadSavedSettings();
        Button saveButton = binding.saveSettingsButton;
        Intent intent = getIntent();
        String camefrom = intent.getStringExtra("camefrom");
        //saveButton.setOnClickListener(v -> saveServer(camefrom));

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleNightMode(isChecked);
            if (isChecked) {
                darkModeSwitch.setText("Disable Dark Mode");
            }
            else {
                darkModeSwitch.setText("Enable Dark Mode");
            }
            //updateThemeButtonLabel();
        });


        ImageButton backButton = binding.backButton;
        backButton.setOnClickListener(v -> onBackPressed());
        settingsSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default values for the preferences (before creating a listener!)
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, true);
        SharedPreferences.OnSharedPreferenceChangeListener listener = (preferences, key) -> {
            if (key.equals("dark_mode")) {
                changeTheme(preferences.getBoolean(key, false));
            }
        };
        settingsSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    private void changeTheme(boolean isNightMode) {
        if (isNightMode) {
            getDelegate().setLocalNightMode(MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(MODE_NIGHT_NO);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("dark_mode", isNightMode);
        editor.apply();
    }
/*
    private void saveServer(String s) {
        Pattern pattern = Pattern.compile("^(http|https)://");
        Matcher matcher = pattern.matcher(serverAddressEditText.getText().toString());

        if (!matcher.find()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid URL, has to start with http / https",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        AP2_SpeakEasy.urlServer.postValue(serverAddressEditText.getText().toString());
        isReturn.getInstance().setIsReturn(true);
        finish();
    }
*/
    private void loadSavedSettings() {
        SharedPreferences settingsSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkModeEnabled = settingsSharedPreferences.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(darkModeEnabled);
        if (darkModeEnabled) {
            darkModeSwitch.setText("Disable Dark Mode");
        }
        else {
            darkModeSwitch.setText("Enable Dark Mode");
        }
    }

    private void toggleNightMode(boolean isChecked) {
        SharedPreferences.Editor editor = settingsSharedPreferences.edit();
        editor.putBoolean("dark_mode", isChecked);
        editor.apply();
    }

}
