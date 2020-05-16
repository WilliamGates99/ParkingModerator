package com.xeniac.parkingmoderator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_CHECK = "login_check";
    public static final String LOGIN_CHECK_KEY = "login_check_key";
    public static final String PARKING_EMAIL_KEY = "parking_email_key";

    private ParkingDataSource parkingDataSource;

    private MaterialCardView popUpCV;
    private FrameLayout shadowFL;
    private TextInputEditText emailET;
    private TextInputEditText passwordET;
    private LinearLayout loginLL;
    private TextView registerTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInitializer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parkingDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        parkingDataSource.close();
    }

    private void loginInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        loginLayouts();
        loginActionDone();
    }

    private void loginLayouts() {
        popUpCV = findViewById(R.id.cv_login_popup);
        shadowFL = findViewById(R.id.fl_login_shadow);
        emailET = findViewById(R.id.ti_login_edit_email);
        passwordET = findViewById(R.id.ti_login_edit_password);
        loginLL = findViewById(R.id.ll_login_login);
        registerTV = findViewById(R.id.tv_login_register);
    }

    private void loginActionDone() {
        passwordET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginOnClick(v);
            }
            return false;
        });
    }

    public void loginOnClick(View view) {
        String emailInput = Objects.requireNonNull(emailET.getText()).toString().toLowerCase();
        String passwordInput = Objects.requireNonNull(passwordET.getText()).toString();

        if (TextUtils.isEmpty(emailInput)) {
            emailET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(passwordInput)) {
            passwordET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            emailET.clearFocus();
            passwordET.clearFocus();
            loginCheck(emailInput, passwordInput);
        }
    }

    private void loginCheck(String email, String password) {
        if (parkingDataSource.parkingExist(email)) {
            ParkingDataItem parking = parkingDataSource.getParking(email);
            if (password.equals(parking.getPassword())) {
                SharedPreferences.Editor editor =
                        getSharedPreferences(LOGIN_CHECK, MODE_PRIVATE).edit();
                editor.putBoolean(LOGIN_CHECK_KEY, true).apply();
                editor.putString(PARKING_EMAIL_KEY, parking.getEmail()).apply();
                startActivity(new Intent(this, ParkingActivity.class));
                finish();
            } else {
                popUpMessage();
            }
        } else {
            popUpMessage();
        }
    }

    private void popUpMessage() {
        shadowFL.setVisibility(View.VISIBLE);
        emailET.setEnabled(false);
        passwordET.setEnabled(false);
        loginLL.setClickable(false);
        registerTV.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", distance);
        animator.start();
    }

    public void backOnClick(View view) {
        shadowFL.setVisibility(View.GONE);
        emailET.setEnabled(true);
        passwordET.setEnabled(true);
        loginLL.setClickable(true);
        registerTV.setClickable(false);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", 0);
        animator.start();
    }

    public void forgotOnClick(View view) {
        startActivity(new Intent(this, RecoveryActivity.class));
    }

    public void registerOnClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}