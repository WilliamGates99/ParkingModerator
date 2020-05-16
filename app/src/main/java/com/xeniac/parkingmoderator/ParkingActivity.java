package com.xeniac.parkingmoderator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;

import java.text.NumberFormat;
import java.util.Locale;

public class ParkingActivity extends AppCompatActivity {

    private ParkingDataSource parkingDataSource;
    private ParkingDataItem parking;

    private ScrollView scrollView;
    private MaterialCardView popUpCV;
    private FrameLayout shadowFL;
    private TextView popUpTV;
    private LinearLayout enterLL, exitLL;

    private int freeSpace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        Toolbar toolbar = findViewById(R.id.toolbar_parking);
        setSupportActionBar(toolbar);
        parkingInitializer();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parking, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_parking_report:
                startActivity(new Intent(this, ReportActivity.class));
                break;
            case R.id.menu_parking_edit:
                startActivity(new Intent(this, EditActivity.class));
                break;
            case R.id.menu_parking_logout:
                SharedPreferences.Editor editor =
                        getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE).edit();
                editor.putBoolean(LoginActivity.LOGIN_CHECK_KEY, false).apply();
                editor.putString(LoginActivity.PARKING_EMAIL_KEY, null).apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void parkingInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        SharedPreferences preferences =
                getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE);
        String parkingEmail = preferences.getString(LoginActivity.PARKING_EMAIL_KEY, null);
        parking = parkingDataSource.getParking(parkingEmail);
        parkingLayouts();
    }

    private void parkingLayouts() {
        setTitle(parking.getName());

        scrollView = findViewById(R.id.sv_parking);
        popUpCV = findViewById(R.id.cv_parking_popup);
        shadowFL = findViewById(R.id.fl_paring_shadow);
        popUpTV = findViewById(R.id.tv_parking_popup);
        TextView freeStatusTV = findViewById(R.id.tv_parking_status_free);
        TextView occupiedStatusTV = findViewById(R.id.tv_parking_status_occupied);
        ProgressBar progressBar = findViewById(R.id.pb_parking_percentage);
        TextView percentageTV = findViewById(R.id.tv_parking_percentage);
        enterLL = findViewById(R.id.ll_parking_enter);
        exitLL = findViewById(R.id.ll_parking_exit);

        freeSpace = parking.getCapacity() - parking.getOccupiedSpace();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
        numberFormat.setGroupingUsed(false);
        freeStatusTV.setText(numberFormat.format(freeSpace));
        occupiedStatusTV.setText(numberFormat.format(parking.getOccupiedSpace()));
        progressBar.setProgress((int) ((float) parking.getOccupiedSpace() * 100 / parking.getCapacity()));
        percentageTV.setText(numberFormat.format((int) ((float) freeSpace * 100 / parking.getCapacity())));
    }

    public void enterOnClick(View view) {
        if (freeSpace == 0) {
            popUpTV.setText(R.string.string_parking_tv_popup_message_enter);
            popUpMessage();
        } else {
            startActivity(new Intent(this, EnterActivity.class));
        }
    }

    public void exitOnClick(View view) {
        if (parking.getOccupiedSpace() == 0) {
            popUpTV.setText(R.string.string_parking_tv_popup_message_exit);
            popUpMessage();
        } else {
            startActivity(new Intent(this, ExitActivity.class));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popUpMessage() {
        scrollView.setOnTouchListener((view1, motionEvent) -> true);
        shadowFL.setVisibility(View.VISIBLE);
        enterLL.setClickable(false);
        exitLL.setClickable(false);

        float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                -300, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", distance);
        animator.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void backOnClick(View view) {
        scrollView.setOnTouchListener((view1, motionEvent) -> false);
        shadowFL.setVisibility(View.GONE);
        enterLL.setClickable(true);
        exitLL.setClickable(true);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                popUpCV, "translationY", 0);
        animator.start();
    }
}