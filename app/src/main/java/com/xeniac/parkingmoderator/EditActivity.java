package com.xeniac.parkingmoderator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private ParkingDataSource parkingDataSource;
    private ParkingDataItem parking;

    private ScrollView scrollView;
    private MaterialCardView popUpCV;
    private MaterialCardView popUpQuestionCV;
    private TextView popUpTV;
    private MaterialCardView backCV;
    private MaterialCardView confirmCV;
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
    private LinearLayout editLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editInitializer();
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
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onNavigateUp();
    }

    private void editInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        SharedPreferences preferences =
                getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE);
        String parkingEmail = preferences.getString(LoginActivity.PARKING_EMAIL_KEY, null);
        parking = parkingDataSource.getParking(parkingEmail);
        editLayouts();
        questionsDropdown();
        validatePassword();
        editActionDone();
    }

    private void editLayouts() {
        scrollView = findViewById(R.id.sv_edit);
        popUpCV = findViewById(R.id.cv_edit_popup);
        popUpQuestionCV = findViewById(R.id.cv_edit_popup_question);
        popUpTV = findViewById(R.id.tv_edit_popup);
        backCV = findViewById(R.id.cv_edit_popup_back);
        confirmCV = findViewById(R.id.cv_edit_popup_confirm);
        shadowFL = findViewById(R.id.fl_edit_shadow);
        emailET = findViewById(R.id.ti_edit_edit_email);
        nameET = findViewById(R.id.ti_edit_edit_name);
        capacityET = findViewById(R.id.ti_edit_edit_capacity);
        chargeBaseET = findViewById(R.id.ti_edit_edit_charge_base);
        chargeExtraET = findViewById(R.id.ti_edit_edit_charge_extra);
        passwordET = findViewById(R.id.ti_edit_edit_password);
        rePasswordET = findViewById(R.id.ti_edit_edit_re_password);
        questionDD = findViewById(R.id.ti_edit_dropdown_question);
        answerET = findViewById(R.id.ti_edit_edit_answer);
        editLL = findViewById(R.id.ll_edit_edit);

        emailET.setText(parking.getEmail());
        nameET.setText(parking.getName());
        capacityET.setText(String.valueOf(parking.getCapacity()));
        chargeBaseET.setText(String.valueOf(parking.getChargeBase()));
        chargeExtraET.setText(String.valueOf(parking.getChargeExtra()));
        questionDD.setText(parking.getQuestion());
        answerET.setText(parking.getAnswer());
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

    private void editActionDone() {
        answerET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editOnClick(v);
            }
            return false;
        });
    }

    public void editOnClick(View view) {
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
        } else if (!TextUtils.isEmpty(rePasswordInput) && TextUtils.isEmpty(passwordInput)) {
            passwordET.requestFocus();
            InputMethodManager methodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(methodManager).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, 0);
        } else if (!TextUtils.isEmpty(passwordInput) && TextUtils.isEmpty(rePasswordInput)) {
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
        } else if (!TextUtils.isEmpty(passwordInput) || !TextUtils.isEmpty(rePasswordInput)) {
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
                setParking(emailInput, nameInput, capacityInput, chargeBaseInput,
                        chargeExtraInput, questionInput, answerInput);
            }
        } else {
            setParking(emailInput, nameInput, capacityInput, chargeBaseInput,
                    chargeExtraInput, questionInput, answerInput);
        }
    }

    private boolean isEmailValid(String email) {
        return (email.contains("@") || (TextUtils.isDigitsOnly(email) &&
                (email.startsWith("9") || email.startsWith("09")) && email.length() >= 10));
    }

    private boolean isPasswordValid(TextInputEditText editText) {
        return editText.length() >= 8;
    }

    private void setParking(String emailInput, String nameInput, String capacityInput, String chargeBaseInput,
                            String chargeExtraInput, String questionInput, String answerInput) {
        emailET.clearFocus();
        nameET.clearFocus();
        capacityET.clearFocus();
        chargeBaseET.clearFocus();
        chargeExtraET.clearFocus();
        answerET.clearFocus();

        parking.setName(nameInput);
        parking.setCapacity(Integer.parseInt(capacityInput));
        parking.setChargeBase(Integer.parseInt(chargeBaseInput));
        parking.setChargeExtra(Integer.parseInt(chargeExtraInput));
        parking.setQuestion(questionInput);
        parking.setAnswer(answerInput);

        if (!emailInput.equals(parking.getEmail())) {
            if (parkingDataSource.parkingExist(emailInput)) {
                popUpTV.setText(R.string.string_edit_tv_popup_message);
                backCV.setVisibility(View.VISIBLE);
                confirmCV.setVisibility(View.GONE);
                popUpMessage();
            } else {
                parking.setEmail(emailInput);
                popUpQuestion();
            }
        } else {
            popUpQuestion();
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
        editLL.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", distance);
        animator.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popUpQuestion() {
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
        editLL.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpQuestionCV, "translationY", distance);
        animator.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void backOnClick(View view) {
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
        editLL.setClickable(true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", 0);
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
        editLL.setClickable(true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpQuestionCV, "translationY", 0);
        animator.start();
    }

    public void yesOnClick(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpQuestionCV, "translationY", 0);
        animator.start();
        editParking(parking);
    }

    private void editParking(ParkingDataItem item) {
        parkingDataSource.open();
        parkingDataSource.updateParking(item);

        SharedPreferences.Editor editor =
                getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE).edit();
        editor.putString(LoginActivity.PARKING_EMAIL_KEY, parking.getEmail()).apply();

        backCV.setVisibility(View.GONE);
        confirmCV.setVisibility(View.VISIBLE);
        popUpTV.setText(R.string.string_edit_tv_popup_message_result);
        popUpMessage();
    }

    public void confirmOnClick(View view) {
        super.onNavigateUp();
    }
}