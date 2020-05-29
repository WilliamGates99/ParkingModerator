package com.xeniac.parkingmoderator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences =
                getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE);
        boolean loginCheck = preferences.getBoolean(LoginActivity.LOGIN_CHECK_KEY, false);

        if (loginCheck) {
            startActivity(new Intent(this, ParkingActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}