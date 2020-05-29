package com.xeniac.parkingmoderator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesAdapter;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesDataItem;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesDataSource;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class ExitActivity extends AppCompatActivity {

    private ParkingDataSource parkingDataSource;
    private VehiclesDataSource vehiclesDataSource;
    private ParkingDataItem parking;
    private List<VehiclesDataItem> vehiclesList;
    private VehiclesDataItem vehicle;

    private NestedScrollView scrollView;
    private FrameLayout shadowFL;
    private MaterialCardView popUpPanelCV;
    private MaterialCardView popUpResultCV;
    private TextView popUpResultTV;
    private MaterialCardView popUpResultRetryCV;
    private MaterialCardView popUpResultConfirmCV;
    private TextInputEditText plateNumber2ET;
    private TextInputEditText plateLetterET;
    private TextInputEditText plateNumber3ET;
    private TextInputEditText plateIranET;
    private TextInputEditText nameET;
    private LinearLayout searchLL;

    private LinearLayout exitLL;

    private int totalCharge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        Toolbar toolbar = findViewById(R.id.toolbar_exit);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        exitInitializer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parkingDataSource.open();
        vehiclesDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        parkingDataSource.close();
        vehiclesDataSource.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onSupportNavigateUp();
    }

    private void exitInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        vehiclesDataSource = new VehiclesDataSource(this);
        SharedPreferences preferences =
                getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE);
        String parkingEmail = preferences.getString(LoginActivity.PARKING_EMAIL_KEY, null);
        parking = parkingDataSource.getParking(parkingEmail);
        exitLayouts();
        plateHints();
        plateIranETActionDone();
        nameETActionDone();
    }

    private void exitLayouts() {
        scrollView = findViewById(R.id.sv_exit);
        shadowFL = findViewById(R.id.fl_exit_shadow);
        popUpPanelCV = findViewById(R.id.cv_exit_popup_panel);
        popUpResultCV = findViewById(R.id.cv_exit_popup_result);
        popUpResultTV = findViewById(R.id.tv_exit_popup_result);
        popUpResultRetryCV = findViewById(R.id.cv_exit_popup_result_retry);
        popUpResultConfirmCV = findViewById(R.id.cv_exit_popup_result_confirm);
        plateNumber2ET = findViewById(R.id.ti_exit_edit_plate_number_2);
        plateLetterET = findViewById(R.id.ti_exit_edit_plate_letter);
        plateNumber3ET = findViewById(R.id.ti_exit_edit_plate_number_3);
        plateIranET = findViewById(R.id.ti_exit_edit_plate_iran);
        nameET = findViewById(R.id.ti_exit_edit_name);
        searchLL = findViewById(R.id.ll_exit_search);
    }

    private void plateHints() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
        numberFormat.setGroupingUsed(false);

        plateNumber2ET.setHint(numberFormat.format(79));
        plateLetterET.setHint("ب");
        plateNumber3ET.setHint(numberFormat.format(426));

        plateNumber2ET.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                plateNumber2ET.setHint("");
            } else {
                plateNumber2ET.setHint(numberFormat.format(79));
            }
        });

        plateLetterET.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                plateLetterET.setHint("");
            } else {
                plateLetterET.setHint("ب");
            }
        });

        plateNumber3ET.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                plateNumber3ET.setHint("");
            } else {
                plateNumber3ET.setHint(numberFormat.format(426));
            }
        });
    }

    private void plateIranETActionDone() {
        plateIranET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchOnClick(v);
            }
            return false;
        });
    }

    private void nameETActionDone() {
        nameET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchOnClick(v);
            }
            return false;
        });
    }

    public void searchOnClick(View view) {
        String plateNumber2Input = Objects.requireNonNull(plateNumber2ET.getText()).toString();
        String plateLetterInput = Objects.requireNonNull(plateLetterET.getText()).toString();
        String plateNumber3Input = Objects.requireNonNull(plateNumber3ET.getText()).toString();
        String plateIranInput = Objects.requireNonNull(plateIranET.getText()).toString();
        String nameInput = Objects.requireNonNull(nameET.getText()).toString();

        if (TextUtils.isEmpty(nameInput)) {
            if (TextUtils.isEmpty(plateNumber2Input)) {
                plateNumber2ET.requestFocus();
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).toggleSoftInput(
                        InputMethodManager.SHOW_FORCED, 0);
            } else if (TextUtils.isEmpty(plateLetterInput)) {
                plateLetterET.requestFocus();
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).toggleSoftInput(
                        InputMethodManager.SHOW_FORCED, 0);
            } else if (TextUtils.isEmpty(plateNumber3Input)) {
                plateNumber3ET.requestFocus();
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).toggleSoftInput(
                        InputMethodManager.SHOW_FORCED, 0);
            } else if (TextUtils.isEmpty(plateIranInput)) {
                plateIranET.requestFocus();
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).toggleSoftInput(
                        InputMethodManager.SHOW_FORCED, 0);
            } else {
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);

                plateNumber2ET.clearFocus();
                plateLetterET.clearFocus();
                plateNumber3ET.clearFocus();
                plateIranET.clearFocus();
                nameET.clearFocus();

                searchByPlate(Integer.parseInt(plateNumber2Input), plateLetterInput,
                        Integer.parseInt(plateNumber3Input), Integer.parseInt(plateIranInput));
            }
        } else {
            if (TextUtils.isEmpty(plateNumber2Input) || TextUtils.isEmpty(plateLetterInput) ||
                    TextUtils.isEmpty(plateNumber3Input) || TextUtils.isEmpty(plateIranInput)) {
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);

                plateNumber2ET.clearFocus();
                plateLetterET.clearFocus();
                plateNumber3ET.clearFocus();
                plateIranET.clearFocus();
                nameET.clearFocus();

                searchByName(nameInput);
            } else if (!TextUtils.isEmpty(nameInput) && !TextUtils.isEmpty(plateNumber2Input) &&
                    !TextUtils.isEmpty(plateLetterInput) && !TextUtils.isEmpty(plateNumber3Input) &&
                    !TextUtils.isEmpty(plateIranInput)) {
                InputMethodManager methodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                Objects.requireNonNull(methodManager).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);

                plateNumber2ET.clearFocus();
                plateLetterET.clearFocus();
                plateNumber3ET.clearFocus();
                plateIranET.clearFocus();
                nameET.clearFocus();

                searchByBoth(Integer.parseInt(plateNumber2Input), plateLetterInput,
                        Integer.parseInt(plateNumber3Input), Integer.parseInt(plateIranInput), nameInput);
            }
        }
    }

    private void searchByPlate(int plateNumber2, String plateLetter, int plateNumber3, int plateIran) {
        vehiclesDataSource.open();
        vehiclesList = vehiclesDataSource.getVehiclesByPlate(plateNumber2, plateLetter,
                plateNumber3, plateIran);
        if (vehiclesList.size() > 0) {
            exitRecyclerView(vehiclesList);
        } else {
            popUpResultTV.setText(R.string.string_exit_tv_popup_result_title_error);
            popUpResultRetryCV.setVisibility(View.VISIBLE);
            popUpResultConfirmCV.setVisibility(View.GONE);
            popUpResult();
        }
    }

    private void searchByName(String name) {
        vehiclesDataSource.open();
        vehiclesList = vehiclesDataSource.getVehiclesByName(name);
        if (vehiclesList.size() > 0) {
            exitRecyclerView(vehiclesList);
        } else {
            popUpResultTV.setText(R.string.string_exit_tv_popup_result_title_error);
            popUpResultRetryCV.setVisibility(View.VISIBLE);
            popUpResultConfirmCV.setVisibility(View.GONE);
            popUpResult();
        }
    }

    private void searchByBoth(int plateNumber2, String plateLetter, int plateNumber3,
                              int plateIran, String name) {
        vehiclesDataSource.open();
        vehiclesList = vehiclesDataSource.getVehiclesByBoth(plateNumber2, plateLetter,
                plateNumber3, plateIran, name);
        if (vehiclesList.size() > 0) {
            exitRecyclerView(vehiclesList);
        } else {
            popUpResultTV.setText(R.string.string_exit_tv_popup_result_title_error);
            popUpResultRetryCV.setVisibility(View.VISIBLE);
            popUpResultConfirmCV.setVisibility(View.GONE);
            popUpResult();
        }
    }

    private void exitRecyclerView(List<VehiclesDataItem> itemsList) {
        VehiclesAdapter vehiclesAdapter = new VehiclesAdapter(this, itemsList, parking);
        RecyclerView exitRV = findViewById(R.id.rv_exit);
        exitRV.setVisibility(View.VISIBLE);
        exitRV.setAdapter(vehiclesAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popUpResult() {
        scrollView.setOnTouchListener((view1, motionEvent) -> true);
        shadowFL.setVisibility(View.VISIBLE);
        plateNumber2ET.setEnabled(false);
        plateLetterET.setEnabled(false);
        plateNumber3ET.setEnabled(false);
        plateIranET.setEnabled(false);
        nameET.setEnabled(false);
        searchLL.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animatorConfirm = ObjectAnimator.ofFloat(
                popUpResultCV, "translationY", distance);
        animatorConfirm.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void retryOnClick(View view) {
        plateNumber2ET.setText(null);
        plateLetterET.setText(null);
        plateNumber3ET.setText(null);
        plateIranET.setText(null);
        nameET.setText(null);

        scrollView.setOnTouchListener((view1, motionEvent) -> false);
        shadowFL.setVisibility(View.GONE);
        plateNumber2ET.setEnabled(true);
        plateLetterET.setEnabled(true);
        plateNumber3ET.setEnabled(true);
        plateIranET.setEnabled(true);
        nameET.setEnabled(true);
        searchLL.setClickable(true);
        ObjectAnimator animatorConfirm = ObjectAnimator.ofFloat(
                popUpResultCV, "translationY", 0);
        animatorConfirm.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void popUpExit(VehiclesDataItem itemVehicle, int baseCharge, int extraCharge, LinearLayout itemExitLL) {
        vehicle = itemVehicle;
        exitLL = itemExitLL;

        scrollView.setOnTouchListener((view1, motionEvent) -> true);
        shadowFL.setVisibility(View.VISIBLE);
        plateNumber2ET.setEnabled(false);
        plateLetterET.setEnabled(false);
        plateNumber3ET.setEnabled(false);
        plateIranET.setEnabled(false);
        nameET.setEnabled(false);
        searchLL.setClickable(false);
        exitLL.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpPanelCV, "translationY", distance);
        animator.start();

        TextView dayEnterDateTV = findViewById(R.id.tv_exit_popup_enter_date_day);
        TextView monthEnterDateTV = findViewById(R.id.tv_exit_popup_enter_date_month);
        TextView yearEnterDateTV = findViewById(R.id.tv_exit_popup_enter_date_year);
        TextView parkedTimeTV = findViewById(R.id.tv_exit_popup_parked_time);
        TextView chargeTV = findViewById(R.id.tv_exit_popup_charge);

        //Get enteredDate of the Vehicle
        PersianDate enterDate = null;
        PersianDateFormat dateFormat = new PersianDateFormat("yyyyMMddHHmmss");
        try {
            enterDate = dateFormat.parse(vehicle.getDateEnter());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*Calculate interval between enterDate and now.
        interval[0] = days
        interval[1] = hours
        interval[2] = minutes
        interval[3] = seconds*/
        long[] interval = Objects.requireNonNull(enterDate).untilToday();
        int hours = (int) (interval[1] + (interval[0] * 24));
        int minutes = (int) interval[2];

        if (minutes > 15) {
            hours += 1;
        }

        int enterDay = Objects.requireNonNull(enterDate).getShDay();
        String enterMonth = enterDate.monthName();
        int enterYear = enterDate.getShYear();

        if (hours == 0 || hours == 1) {
            totalCharge = baseCharge;
        } else {
            totalCharge = baseCharge + ((hours - 1) * extraCharge);
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
        numberFormat.setGroupingUsed(false);
        dayEnterDateTV.setText(numberFormat.format(enterDay));
        monthEnterDateTV.setText(enterMonth);
        yearEnterDateTV.setText(numberFormat.format(enterYear));
        parkedTimeTV.setText(numberFormat.format(hours));
        chargeTV.setText(numberFormat.format(totalCharge));
    }

    public void yesOnClick(View view) {
        exitVehicle();
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpPanelCV, "translationY", 0);
        animator.start();

        popUpResultTV.setText(R.string.string_exit_tv_popup_result_title);
        popUpResultRetryCV.setVisibility(View.GONE);
        popUpResultConfirmCV.setVisibility(View.VISIBLE);
        popUpResult();
    }

    private void exitVehicle() {
        parkingDataSource.open();
        vehiclesDataSource.open();

        PersianDate currentDate = new PersianDate();
        PersianDateFormat dateFormat = new PersianDateFormat("YmdHis");
        String exitDate = dateFormat.format(currentDate);
        vehicle.setDateExit(exitDate);
        vehicle.setExited(true);
        vehicle.setChargeTotal(totalCharge);
        vehiclesDataSource.updateVehicle(vehicle);

        parking.setOccupiedSpace(parking.getOccupiedSpace() - 1);
        parkingDataSource.updateParking(parking);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void noOnClick(View view) {
        vehicle = null;

        scrollView.setOnTouchListener((view1, motionEvent) -> false);
        shadowFL.setVisibility(View.GONE);
        plateNumber2ET.setEnabled(true);
        plateLetterET.setEnabled(true);
        plateNumber3ET.setEnabled(true);
        plateIranET.setEnabled(true);
        nameET.setEnabled(true);
        searchLL.setClickable(true);
        exitLL.setClickable(true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpPanelCV, "translationY", 0);
        animator.start();
    }

    public void confirmOnClick(View view) {
        super.onNavigateUp();
    }
}