package com.xeniac.parkingmoderator.database.parkingsTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xeniac.parkingmoderator.database.DBOpenHelper;

public class ParkingDataSource {

    private SQLiteOpenHelper mDbOpenHelper;
    private SQLiteDatabase mDatabase;

    public ParkingDataSource(Context context) {
        mDbOpenHelper = new DBOpenHelper(context);
        mDatabase = mDbOpenHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbOpenHelper.getWritableDatabase();
    }

    public void close() {
        mDbOpenHelper.close();
    }

    public void createParking(ParkingDataItem item) {
        ContentValues values = item.toValues();
        mDatabase.insert(ParkingTable.TABLE_PARKING, null, values);
    }

    public void updateParking(ParkingDataItem item) {
        ContentValues values = item.toValues();
        String[] ids = {String.valueOf(item.getId())};
        mDatabase.update(ParkingTable.TABLE_PARKING, values,
                ParkingTable.COLUMN_ID + "=?", ids);
    }

    public boolean parkingExist(String parkingEmail) {
        String[] parkingEmails = {String.valueOf(parkingEmail)};
        Cursor cursor = mDatabase.query(ParkingTable.TABLE_PARKING, ParkingTable.ALL_EMAILS,
                ParkingTable.COLUMN_EMAIL + "=?", parkingEmails,
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public ParkingDataItem getParking(String parkingEmail) {
        ParkingDataItem item = new ParkingDataItem();

        String[] parkingEmails = {String.valueOf(parkingEmail)};

        Cursor cursor = mDatabase.query(ParkingTable.TABLE_PARKING,
                ParkingTable.ALL_COLUMNS, ParkingTable.COLUMN_EMAIL + "=?",
                parkingEmails, null, null, null);

        while (cursor.moveToNext()) {
            item.setId(cursor.getInt(cursor.getColumnIndex(ParkingTable.COLUMN_ID)));
            item.setEmail(cursor.getString(cursor.getColumnIndex(ParkingTable.COLUMN_EMAIL)));
            item.setName(cursor.getString(cursor.getColumnIndex(ParkingTable.COLUMN_NAME)));
            item.setCapacity(cursor.getInt(cursor.getColumnIndex(ParkingTable.COLUMN_CAPACITY)));
            item.setOccupiedSpace(cursor.getInt(cursor.getColumnIndex(ParkingTable.COLUMN_OCCUPIED_SPACE)));
            item.setChargeBase(cursor.getInt(cursor.getColumnIndex(ParkingTable.COLUMN_CHARGE_BASE)));
            item.setChargeExtra(cursor.getInt(cursor.getColumnIndex(ParkingTable.COLUMN_CHARGE_EXTRA)));
            item.setPassword(cursor.getString(cursor.getColumnIndex(ParkingTable.COLUMN_PASSWORD)));
            item.setQuestion(cursor.getString(cursor.getColumnIndex(ParkingTable.COLUMN_QUESTION)));
            item.setAnswer(cursor.getString(cursor.getColumnIndex(ParkingTable.COLUMN_ANSWER)));
        }

        cursor.close();
        return item;
    }
}