package com.xeniac.parkingmoderator.database.vehiclesTable;

import com.xeniac.parkingmoderator.database.parkingsTable.ParkingTable;

public class VehiclesTable {
    static final String TABLE_VEHICLES = "vehicles";
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_MODEL = "model";
    static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    static final String COLUMN_PLATE_NUMBER_2 = "plateNumber2";
    static final String COLUMN_PLATE_LETTER = "plateLetter";
    static final String COLUMN_PLATE_NUMBER_3 = "plateNumber3";
    static final String COLUMN_PLATE_IRAN = "plateIran";
    static final String COLUMN_DATE_ENTER = "dateEnter";
    static final String COLUMN_DATE_EXIT = "dateExit";
    static final String COLUMN_PHOTO = "photo";
    static final String COLUMN_EXITED = "exited";
    static final String COLUMN_CHARGE_TOTAL = "chargeTotal";
    static final String COLUMN_PARKING_ID = "parkingId";

    static final String[] ALL_PLATES_EXITED = {COLUMN_PLATE_NUMBER_2, COLUMN_PLATE_LETTER,
            COLUMN_PLATE_NUMBER_3, COLUMN_PLATE_IRAN, COLUMN_EXITED};

    static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_MODEL, COLUMN_PHONE_NUMBER,
            COLUMN_PLATE_NUMBER_2, COLUMN_PLATE_LETTER, COLUMN_PLATE_NUMBER_3, COLUMN_PLATE_IRAN,
            COLUMN_DATE_ENTER, COLUMN_DATE_EXIT, COLUMN_PHOTO, COLUMN_EXITED,
            COLUMN_CHARGE_TOTAL, COLUMN_PARKING_ID};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_VEHICLES + "(" +
                    COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_MODEL + " TEXT," +
                    COLUMN_PHONE_NUMBER + " TEXT," +
                    COLUMN_PLATE_NUMBER_2 + " INTEGER NOT NULL," +
                    COLUMN_PLATE_LETTER + " TEXT NOT NULL," +
                    COLUMN_PLATE_NUMBER_3 + " INTEGER NOT NULL," +
                    COLUMN_PLATE_IRAN + " INTEGER NOT NULL," +
                    COLUMN_DATE_ENTER + " TEXT NOT NULL," +
                    COLUMN_DATE_EXIT + " TEXT," +
                    COLUMN_PHOTO + " TEXT," +
                    COLUMN_EXITED + " INTEGER NOT NULL DEFAULT 0," +
                    COLUMN_CHARGE_TOTAL + " INTEGER NOT NULL DEFAULT 0," +
                    COLUMN_PARKING_ID + " INTEGER NOT NULL," +
                    "FOREIGN KEY (" + COLUMN_PARKING_ID + ") REFERENCES parking(" + ParkingTable.COLUMN_ID + "));";
}