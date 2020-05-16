package com.xeniac.parkingmoderator;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;

import java.util.Objects;

public class RecoveryActivity extends AppCompatActivity {

    private ParkingDataSource parkingDataSource;

    private MaterialCardView popUpCV;
    private TextView popUpTV;
    private FrameLayout shadowFL;
    private TextInputLayout emailIL;
    private TextInputEditText emailET;
    private MaterialCardView showCV;
    private LinearLayout showLL;
    private TextView recoveryTV;
    private TextInputLayout answerIL;
    private TextInputEditText answerET;
    private MaterialCardView recoverCV;
    private LinearLayout recoverLL;
    private TextInputLayout passwordIL;
    private TextInputEditText passwordET;
    private TextInputLayout rePasswordIL;
    private TextInputEditText rePasswordET;
    private MaterialCardView saveCV;
    private LinearLayout saveLL;
    private TextView backTV;

    private ParkingDataItem parking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        recoveryInitializer();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void recoveryInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        recoveryLayouts();
        showActionDone();
        recoverActionDone();
        validatePassword();
        saveActionDone();
    }

    private void recoveryLayouts() {
        popUpCV = findViewById(R.id.cv_recovery_popup);
        popUpTV = findViewById(R.id.tv_recovery_popup);
        shadowFL = findViewById(R.id.fl_recovery_shadow);
        emailIL = findViewById(R.id.ti_recovery_layout_email);
        emailET = findViewById(R.id.ti_recovery_edit_email);
        showCV = findViewById(R.id.cv_recovery_show);
        showLL = findViewById(R.id.ll_recovery_show);
        recoveryTV = findViewById(R.id.tv_recovery);
        answerIL = findViewById(R.id.ti_recovery_layout_answer);
        answerET = findViewById(R.id.ti_recovery_edit_answer);
        recoverCV = findViewById(R.id.cv_recovery_recover);
        recoverLL = findViewById(R.id.ll_recovery_recover);
        backTV = findViewById(R.id.tv_recovery_back);
        passwordIL = findViewById(R.id.ti_recovery_layout_password);
        passwordET = findViewById(R.id.ti_recovery_edit_password);
        rePasswordIL = findViewById(R.id.ti_recovery_layout_re_password);
        rePasswordET = findViewById(R.id.ti_recovery_edit_re_password);
        saveCV = findViewById(R.id.cv_recovery_save);
        saveLL = findViewById(R.id.ll_recovery_save);
    }

    private void showActionDone() {
        emailET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showOnclick(v);
            }
            return false;
        });
    }

    public void showOnclick(View view) {
        String emailInput = Objects.requireNonNull(emailET.getText()).toString().toLowerCase();

        if (TextUtils.isEmpty(emailInput)) {
            emailET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            emailET.clearFocus();
            emailCheck(emailInput);
        }
    }

    private void emailCheck(String email) {
        if (parkingDataSource.parkingExist(email)) {
            parking = parkingDataSource.getParking(email);

            if (parking.getQuestion() != null) {
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).hideSoftInputFromWindow(
                        emailET.getWindowToken(), 0);

                emailIL.setVisibility(View.GONE);
                showCV.setVisibility(View.GONE);
                recoveryTV.setVisibility(View.VISIBLE);
                answerIL.setVisibility(View.VISIBLE);
                recoverCV.setVisibility(View.VISIBLE);
                recoveryTV.setText(parking.getQuestion());
            } else {
                popUpTV.setText(R.string.string_recovery_tv_popup_message_question);
                popUpMessage();
            }
        } else {
            popUpTV.setText(R.string.string_recovery_tv_popup_message_email);
            popUpMessage();
        }
    }

    private void recoverActionDone() {
        answerET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                recoverOnclick(v);
            }
            return false;
        });
    }

    public void recoverOnclick(View view) {
        String answerInput = Objects.requireNonNull(answerET.getText()).toString().toLowerCase();

        if (TextUtils.isEmpty(answerInput)) {
            answerET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            answerET.clearFocus();
            answerCheck(answerInput);
        }
    }

    private void answerCheck(String answer) {
        if (answer.equals(parking.getAnswer())) {
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).hideSoftInputFromWindow(
                    emailET.getWindowToken(), 0);

            answerIL.setVisibility(View.GONE);
            recoverCV.setVisibility(View.GONE);
            passwordIL.setVisibility(View.VISIBLE);
            rePasswordIL.setVisibility(View.VISIBLE);
            saveCV.setVisibility(View.VISIBLE);
            recoveryTV.setText(R.string.string_recovery_tv);
        } else {
            popUpTV.setText(R.string.string_recovery_tv_popup_message_answer);
            popUpMessage();
        }
    }

    private void validatePassword() {
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordET.length() > 0) {
                    if (isPasswordValid(passwordET)) {
                        passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_pw_valid, 0, 0, 0);
                    } else {
                        passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_pw_invalid, 0, 0, 0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rePasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordET.length() > 0 && rePasswordET.length() > 0) {
                    if (Objects.requireNonNull(rePasswordET.getText()).toString().
                            equals(Objects.requireNonNull(passwordET.getText()).toString())) {
                        rePasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_pw_valid, 0, 0, 0);
                    } else {
                        rePasswordET.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_pw_invalid, 0, 0, 0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void saveActionDone() {
        rePasswordET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveOnclick(v);
            }
            return false;
        });
    }

    public void saveOnclick(View view) {
        String passwordInput = Objects.requireNonNull(passwordET.getText()).toString();
        String rePasswordInput = Objects.requireNonNull(rePasswordET.getText()).toString();

        if (TextUtils.isEmpty(passwordInput) || !isPasswordValid(passwordET)) {
            passwordET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(rePasswordInput) || !rePasswordInput.equals(passwordInput)) {
            rePasswordET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            passwordET.clearFocus();
            rePasswordET.clearFocus();

            parking.setPassword(passwordInput);
            editParking(parking);
        }
    }

    private boolean isPasswordValid(TextInputEditText editText) {
        return editText.length() >= 8;
    }

    private void editParking(ParkingDataItem item) {
        parkingDataSource.open();
        parkingDataSource.updateParking(item);

        MaterialCardView backCV = findViewById(R.id.cv_recovery_popup_back);
        MaterialCardView confirmCV = findViewById(R.id.cv_recovery_popup_confirm);
        backCV.setVisibility(View.GONE);
        confirmCV.setVisibility(View.VISIBLE);
        popUpTV.setText(R.string.string_recovery_tv_popup_message_result);
        popUpMessage();
    }

    private void popUpMessage() {
        shadowFL.setVisibility(View.VISIBLE);
        emailET.setEnabled(false);
        showLL.setClickable(false);
        answerET.setEnabled(false);
        recoverLL.setClickable(false);
        passwordET.setEnabled(false);
        rePasswordET.setEnabled(false);
        saveLL.setClickable(false);
        backTV.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", distance);
        animator.start();
    }

    public void backOnClick(View view) {
        shadowFL.setVisibility(View.GONE);
        emailET.setEnabled(true);
        showLL.setClickable(true);
        answerET.setEnabled(true);
        recoverLL.setClickable(true);
        passwordET.setEnabled(true);
        rePasswordET.setEnabled(true);
        saveLL.setClickable(true);
        backTV.setClickable(true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", 0);
        animator.start();
    }

    public void loginOnClick(View view) {
        super.onBackPressed();
    }
}