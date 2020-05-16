package com.xeniac.parkingmoderator.database.parkingsTable;

public class ParkingTable {
    static final String TABLE_PARKING = "parking";
    public static final String COLUMN_ID = "id";
    static final String COLUMN_EMAIL = "emailPhone";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_CAPACITY = "capacity";
    static final String COLUMN_OCCUPIED_SPACE = "occupiedSpace";
    static final String COLUMN_CHARGE_BASE = "chargeBase";
    static final String COLUMN_CHARGE_EXTRA = "chargeExtra";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_QUESTION = "question";
    static final String COLUMN_ANSWER = "answer";

    static final String[] ALL_EMAILS = {COLUMN_EMAIL};

    static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_EMAIL, COLUMN_NAME, COLUMN_CAPACITY,
            COLUMN_OCCUPIED_SPACE, COLUMN_CHARGE_BASE, COLUMN_CHARGE_EXTRA,
            COLUMN_PASSWORD, COLUMN_QUESTION, COLUMN_ANSWER};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_PARKING + "(" +
                    COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_CAPACITY + " INTEGER NOT NULL," +
                    COLUMN_OCCUPIED_SPACE + " INTEGER NOT NULL DEFAULT 0," +
                    COLUMN_CHARGE_BASE + " INTEGER NOT NULL," +
                    COLUMN_CHARGE_EXTRA + " INTEGER NOT NULL," +
                    COLUMN_PASSWORD + " TEXT NOT NULL," +
                    COLUMN_QUESTION + " TEXT NOT NULL," +
                    COLUMN_ANSWER + " TEXT NOT NULL" + ");";
}