package com.xeniac.parkingmoderator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataItem;
import com.xeniac.parkingmoderator.database.parkingsTable.ParkingDataSource;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesDataItem;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesDataSource;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {

    private ParkingDataSource parkingDataSource;
    private ParkingDataItem parking;
    private VehiclesDataSource vehiclesDataSource;

    private int enteredVehicles;
    private int exitedVehicles;
    private int totalIncome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = findViewById(R.id.toolbar_report);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        reportInitializer();
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
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onNavigateUp();
    }

    private void reportInitializer() {
        parkingDataSource = new ParkingDataSource(this);
        vehiclesDataSource = new VehiclesDataSource(this);
        SharedPreferences preferences =
                getSharedPreferences(LoginActivity.LOGIN_CHECK, MODE_PRIVATE);
        String parkingEmail = preferences.getString(LoginActivity.PARKING_EMAIL_KEY, null);
        parking = parkingDataSource.getParking(parkingEmail);
        latestMonthVehicles();
        reportLayouts();
    }

    private void reportLayouts() {
        TextView enteredTV = findViewById(R.id.tv_report_entered);
        TextView exitedTV = findViewById(R.id.tv_report_exited);
        TextView incomeTV = findViewById(R.id.tv_report_income);

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("fa", "IR"));
        enteredTV.setText(numberFormat.format(enteredVehicles));
        exitedTV.setText(numberFormat.format(exitedVehicles));
        incomeTV.setText(numberFormat.format(totalIncome));
    }

    private void latestMonthVehicles() {
        List<VehiclesDataItem> vehiclesList = vehiclesDataSource.getVehiclesOfMonth(parking.getId());
        enteredVehicles = vehiclesList.size();
        for (VehiclesDataItem vehicle : vehiclesList) {
            if (vehicle.isExited()) {
                exitedVehicles += 1;
                totalIncome += vehicle.getChargeTotal();
            }
        }
    }
}