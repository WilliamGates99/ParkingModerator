package com.xeniac.parkingmoderator.database.vehiclesTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xeniac.parkingmoderator.database.DBOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class VehiclesDataSource {

    private SQLiteOpenHelper mDbOpenHelper;
    private SQLiteDatabase mDatabase;

    public VehiclesDataSource(Context context) {
        mDbOpenHelper = new DBOpenHelper(context);
        mDatabase = mDbOpenHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbOpenHelper.getWritableDatabase();
    }

    public void close() {
        mDbOpenHelper.close();
    }

    public void createVehicle(VehiclesDataItem item) {
        ContentValues values = item.toValues();
        mDatabase.insert(VehiclesTable.TABLE_VEHICLES, null, values);
    }

    public void updateVehicle(VehiclesDataItem item) {
        ContentValues values = item.toValues();
        String[] ids = {String.valueOf(item.getId())};
        mDatabase.update(VehiclesTable.TABLE_VEHICLES, values,
                VehiclesTable.COLUMN_ID + "=?", ids);
    }

    public boolean vehicleExist(int plateNumber2, String plateLetter, int plateNumber3, int plateIran) {
        String[] vehiclePlatesExited = {String.valueOf(plateNumber2), plateLetter,
                String.valueOf(plateNumber3), String.valueOf(plateIran), String.valueOf(0)};
        Cursor cursor = mDatabase.query(VehiclesTable.TABLE_VEHICLES, VehiclesTable.ALL_PLATES_EXITED,
                VehiclesTable.COLUMN_PLATE_NUMBER_2 + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_LETTER + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_NUMBER_3 + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_IRAN + "=?" + " AND "
                        + VehiclesTable.COLUMN_EXITED + "=?", vehiclePlatesExited,
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<VehiclesDataItem> getVehiclesOfMonth(int parkingId) {
        List<VehiclesDataItem> vehiclesList = new ArrayList<>();

        String[] vehicleParkingIds = {String.valueOf(parkingId)};

        Cursor cursor = mDatabase.query(VehiclesTable.TABLE_VEHICLES, VehiclesTable.ALL_COLUMNS,
                VehiclesTable.COLUMN_PARKING_ID + "=?", vehicleParkingIds,
                null, null, null);

        while (cursor.moveToNext()) {

            PersianDate enterDate = null;
            PersianDateFormat dateFormat = new PersianDateFormat("yyyyMMddHHmmss");
            try {
                enterDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_ENTER)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            /*Calculate interval between enterDate and now.
            interval[0] = days
            interval[1] = hours
            interval[2] = minutes
            interval[3] = seconds*/
            long[] interval = Objects.requireNonNull(enterDate).untilToday();
            if (interval[0] <= 31) {
                VehiclesDataItem item = new VehiclesDataItem();
                item.setId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_ID)));
                item.setName(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_NAME)));
                item.setModel(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_MODEL)));
                item.setPhoneNumber(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHONE_NUMBER)));
                item.setPlateNumber2(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_2)));
                item.setPlateLetter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_LETTER)));
                item.setPlateNumber3(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_3)));
                item.setPlateIran(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_IRAN)));
                item.setDateEnter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_ENTER)));
                item.setDateExit(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_EXIT)));
                item.setPhoto(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHOTO)));
                item.setExited(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_EXITED)) > 0);
                item.setChargeTotal(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_CHARGE_TOTAL)));
                item.setParkingId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PARKING_ID)));

                vehiclesList.add(item);
            }
        }

        cursor.close();
        return vehiclesList;
    }

    public List<VehiclesDataItem> getVehiclesByPlate(int plateNumber2, String plateLetter,
                                                     int plateNumber3, int plateIran) {
        List<VehiclesDataItem> vehiclesList = new ArrayList<>();

        String[] vehiclePlatesExited = {String.valueOf(plateNumber2), plateLetter,
                String.valueOf(plateNumber3), String.valueOf(plateIran), String.valueOf(0)};

        Cursor cursor = mDatabase.query(VehiclesTable.TABLE_VEHICLES, VehiclesTable.ALL_COLUMNS,
                VehiclesTable.COLUMN_PLATE_NUMBER_2 + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_LETTER + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_NUMBER_3 + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_IRAN + "=?" + " AND "
                        + VehiclesTable.COLUMN_EXITED + "=?", vehiclePlatesExited,
                null, null, null);

        while (cursor.moveToNext()) {
            VehiclesDataItem item = new VehiclesDataItem();
            item.setId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_NAME)));
            item.setModel(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_MODEL)));
            item.setPhoneNumber(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHONE_NUMBER)));
            item.setPlateNumber2(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_2)));
            item.setPlateLetter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_LETTER)));
            item.setPlateNumber3(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_3)));
            item.setPlateIran(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_IRAN)));
            item.setDateEnter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_ENTER)));
            item.setDateExit(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_EXIT)));
            item.setPhoto(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHOTO)));
            item.setExited(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_EXITED)) > 0);
            item.setChargeTotal(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_CHARGE_TOTAL)));
            item.setParkingId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PARKING_ID)));

            vehiclesList.add(item);
        }

        cursor.close();
        return vehiclesList;
    }

    public List<VehiclesDataItem> getVehiclesByName(String name) {
        List<VehiclesDataItem> vehiclesList = new ArrayList<>();

        String[] vehicleNamesExited = {"%" + name + "%", String.valueOf(0)};

        Cursor cursor = mDatabase.query(VehiclesTable.TABLE_VEHICLES, VehiclesTable.ALL_COLUMNS,
                VehiclesTable.COLUMN_NAME + " LIKE ?" + " AND "
                        + VehiclesTable.COLUMN_EXITED + "=?", vehicleNamesExited,
                null, null, null);

        while (cursor.moveToNext()) {
            VehiclesDataItem item = new VehiclesDataItem();
            item.setId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_NAME)));
            item.setModel(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_MODEL)));
            item.setPhoneNumber(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHONE_NUMBER)));
            item.setPlateNumber2(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_2)));
            item.setPlateLetter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_LETTER)));
            item.setPlateNumber3(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_3)));
            item.setPlateIran(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_IRAN)));
            item.setDateEnter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_ENTER)));
            item.setDateExit(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_EXIT)));
            item.setPhoto(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHOTO)));
            item.setExited(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_EXITED)) > 0);
            item.setChargeTotal(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_CHARGE_TOTAL)));
            item.setParkingId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PARKING_ID)));

            vehiclesList.add(item);
        }

        cursor.close();
        return vehiclesList;
    }

    public List<VehiclesDataItem> getVehiclesByBoth(int plateNumber2, String plateLetter,
                                                    int plateNumber3, int plateIran, String name) {
        List<VehiclesDataItem> vehiclesList = new ArrayList<>();

        String[] vehiclePlatesExitedNames = {String.valueOf(plateNumber2), plateLetter,
                String.valueOf(plateNumber3), String.valueOf(plateIran), String.valueOf(0), "%" + name + "%"};

        Cursor cursor = mDatabase.query(VehiclesTable.TABLE_VEHICLES, VehiclesTable.ALL_COLUMNS,
                VehiclesTable.COLUMN_PLATE_NUMBER_2 + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_LETTER + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_NUMBER_3 + "=?" + " AND "
                        + VehiclesTable.COLUMN_PLATE_IRAN + "=?" + " AND "
                        + VehiclesTable.COLUMN_EXITED + "=?" + " OR "
                        + VehiclesTable.COLUMN_NAME + " LIKE ?", vehiclePlatesExitedNames,
                null, null, null);

        while (cursor.moveToNext()) {
            VehiclesDataItem item = new VehiclesDataItem();
            item.setId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_NAME)));
            item.setModel(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_MODEL)));
            item.setPhoneNumber(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHONE_NUMBER)));
            item.setPlateNumber2(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_2)));
            item.setPlateLetter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_LETTER)));
            item.setPlateNumber3(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_NUMBER_3)));
            item.setPlateIran(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PLATE_IRAN)));
            item.setDateEnter(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_ENTER)));
            item.setDateExit(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_DATE_EXIT)));
            item.setPhoto(cursor.getString(cursor.getColumnIndex(VehiclesTable.COLUMN_PHOTO)));
            item.setExited(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_EXITED)) > 0);
            item.setChargeTotal(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_CHARGE_TOTAL)));
            item.setParkingId(cursor.getInt(cursor.getColumnIndex(VehiclesTable.COLUMN_PARKING_ID)));

            vehiclesList.add(item);
        }

        cursor.close();
        return vehiclesList;
    }
}