package com.xeniac.parkingmoderator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ParkingDataSource parkingDataSource;

    private ScrollView scrollView;
    private MaterialCardView popUpCV;
    private FrameLayout shadowFL;
    private TextInputEditText emailET;
    private TextInputEditText nameET;
    private TextInputEditText capacityET;
    private TextInputEditText chargeBaseET;
    private TextInputEditText chargeExtraET;
    private TextInputEditText passwordET;
    private TextInputEditText rePasswordET;
    private AutoCompleteTextView questionDD;
    private TextInputEditText answerET;
    private LinearLayout registerLL;
    private TextView loginTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerInitializer();
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

    private void registerInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        registerLayouts();
        questionsDropdown();
        validatePassword();
        registerActionDone();
    }

    private void registerLayouts() {
        scrollView = findViewById(R.id.sv_register);
        popUpCV = findViewById(R.id.cv_register_popup);
        shadowFL = findViewById(R.id.fl_register_shadow);
        emailET = findViewById(R.id.ti_register_edit_email);
        nameET = findViewById(R.id.ti_register_edit_name);
        capacityET = findViewById(R.id.ti_register_edit_capacity);
        chargeBaseET = findViewById(R.id.ti_register_edit_charge_base);
        chargeExtraET = findViewById(R.id.ti_register_edit_charge_extra);
        passwordET = findViewById(R.id.ti_register_edit_password);
        rePasswordET = findViewById(R.id.ti_register_edit_re_password);
        questionDD = findViewById(R.id.ti_register_dropdown_question);
        answerET = findViewById(R.id.ti_register_edit_answer);
        registerLL = findViewById(R.id.ll_register_register);
        loginTV = findViewById(R.id.tv_register_login);
    }

    private void questionsDropdown() {
        String[] questionsList = new String[]{
                getString(R.string.string_list_recovery_questions_1),
                getString(R.string.string_list_recovery_questions_2),
                getString(R.string.string_list_recovery_questions_3),
                getString(R.string.string_list_recovery_questions_4),
                getString(R.string.string_list_recovery_questions_5)};

        ArrayAdapter<String> questionsAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, questionsList);
        questionDD.setAdapter(questionsAdapter);
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

    private void registerActionDone() {
        answerET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                registerOnclick(v);
            }
            return false;
        });
    }

    public void registerOnclick(View view) {
        String emailInput = Objects.requireNonNull(emailET.getText()).toString().toLowerCase();
        String nameInput = Objects.requireNonNull(nameET.getText()).toString();
        String capacityInput = Objects.requireNonNull(capacityET.getText()).toString();
        String chargeBaseInput = Objects.requireNonNull(chargeBaseET.getText()).toString();
        String chargeExtraInput = Objects.requireNonNull(chargeExtraET.getText()).toString();
        String passwordInput = Objects.requireNonNull(passwordET.getText()).toString();
        String rePasswordInput = Objects.requireNonNull(rePasswordET.getText()).toString();
        String questionInput = Objects.requireNonNull(questionDD.getText()).toString();
        String answerInput = Objects.requireNonNull(answerET.getText()).toString().toLowerCase();

        if (TextUtils.isEmpty(emailInput) || !isEmailValid(emailInput)) {
            emailET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(nameInput)) {
            nameET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(capacityInput)) {
            capacityET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(chargeBaseInput)) {
            chargeBaseET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(chargeExtraInput)) {
            chargeExtraET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (TextUtils.isEmpty(passwordInput) || !isPasswordValid(passwordET)) {
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
        } else if (TextUtils.isEmpty(questionInput)) {
            questionDD.showDropDown();
        } else if (TextUtils.isEmpty(answerInput)) {
            answerET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            emailET.clearFocus();
            nameET.clearFocus();
            capacityET.clearFocus();
            chargeBaseET.clearFocus();
            chargeExtraET.clearFocus();
            passwordET.clearFocus();
            rePasswordET.clearFocus();
            answerET.clearFocus();

            ParkingDataItem parking = new ParkingDataItem();
            parking.setEmail(emailInput);
            parking.setName(nameInput);
            parking.setCapacity(Integer.parseInt(capacityInput));
            parking.setChargeBase(Integer.parseInt(chargeBaseInput));
            parking.setChargeExtra(Integer.parseInt(chargeExtraInput));
            parking.setPassword(passwordInput);
            parking.setQuestion(questionInput);
            parking.setAnswer(answerInput);

            registerParking(parking, view);
        }
    }

    private boolean isEmailValid(String email) {
        return (email.contains("@") || (TextUtils.isDigitsOnly(email) &&
                (email.startsWith("9") || email.startsWith("09")) && email.length() >= 10));
    }

    private boolean isPasswordValid(TextInputEditText editText) {
        return editText.length() >= 8;
    }

    private void registerParking(ParkingDataItem item, View view) {
        parkingDataSource.open();
        if (!parkingDataSource.parkingExist(item.getEmail())) {
            parkingDataSource.createParking(item);
            loginOnClick(view);
        } else {
            popUpMessage();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popUpMessage() {
        scrollView.setOnTouchListener((view1, motionEvent) -> true);
        shadowFL.setVisibility(View.VISIBLE);
        emailET.setEnabled(false);
        nameET.setEnabled(false);
        capacityET.setEnabled(false);
        chargeBaseET.setEnabled(false);
        chargeExtraET.setEnabled(false);
        passwordET.setEnabled(false);
        rePasswordET.setEnabled(false);
        questionDD.setEnabled(false);
        answerET.setEnabled(false);
        registerLL.setClickable(false);
        loginTV.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", distance);
        animator.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void noOnClick(View view) {
        scrollView.setOnTouchListener((view1, motionEvent) -> false);
        shadowFL.setVisibility(View.GONE);
        emailET.setEnabled(true);
        nameET.setEnabled(true);
        capacityET.setEnabled(true);
        chargeBaseET.setEnabled(true);
        chargeExtraET.setEnabled(true);
        passwordET.setEnabled(true);
        rePasswordET.setEnabled(true);
        questionDD.setEnabled(true);
        answerET.setEnabled(true);
        registerLL.setClickable(true);
        loginTV.setClickable(true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", 0);
        animator.start();

        emailET.requestFocus();
        InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        Objects.requireNonNull(methodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void loginOnClick(View view) {
        super.onBackPressed();
    }
}