package com.xeniac.parkingmoderator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.xeniac.parkingmoderator.database.parkingsTable.ParkingTable;
import com.xeniac.parkingmoderator.database.vehiclesTable.VehiclesTable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "parking_moderator.db";
    private static int DB_VERSION = 1;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ParkingTable.SQL_CREATE);
        db.execSQL(VehiclesTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        oldVersion = DB_VERSION;
        newVersion = oldVersion + 1;
        DB_VERSION = newVersion;
    }
}