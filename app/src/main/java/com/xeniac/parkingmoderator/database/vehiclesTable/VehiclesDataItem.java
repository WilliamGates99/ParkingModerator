package com.xeniac.parkingmoderator.database.vehiclesTable;

import android.content.ContentValues;

public class VehiclesDataItem {

    private int id;
    private String name;
    private String model;
    private String phoneNumber;
    private int plateNumber2;
    private String plateLetter;
    private int plateNumber3;
    private int plateIran;
    private String dateEnter;
    private String dateExit;
    private String photo;
    private boolean exited;
    private int chargeTotal;
    private int parkingId;

    public VehiclesDataItem() {
    }

    public VehiclesDataItem(int id, String name, String model, String phoneNumber, int plateNumber2,
                            String plateLetter, int plateNumber3, int plateIran, String dateEnter,
                            String dateExit, String photo, boolean exited, int chargeTotal, int parkingId) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.phoneNumber = phoneNumber;
        this.plateNumber2 = plateNumber2;
        this.plateLetter = plateLetter;
        this.plateNumber3 = plateNumber3;
        this.plateIran = plateIran;
        this.dateEnter = dateEnter;
        this.dateExit = dateExit;
        this.photo = photo;
        this.exited = exited;
        this.chargeTotal = chargeTotal;
        this.parkingId = parkingId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPlateNumber2() {
        return plateNumber2;
    }

    public void setPlateNumber2(int plateNumber2) {
        this.plateNumber2 = plateNumber2;
    }

    public String getPlateLetter() {
        return plateLetter;
    }

    public void setPlateLetter(String plateLetter) {
        this.plateLetter = plateLetter;
    }

    public int getPlateNumber3() {
        return plateNumber3;
    }

    public void setPlateNumber3(int plateNumber3) {
        this.plateNumber3 = plateNumber3;
    }

    public int getPlateIran() {
        return plateIran;
    }

    public void setPlateIran(int plateIran) {
        this.plateIran = plateIran;
    }

    public String getDateEnter() {
        return dateEnter;
    }

    public void setDateEnter(String dateEnter) {
        this.dateEnter = dateEnter;
    }

    public String getDateExit() {
        return dateExit;
    }

    public void setDateExit(String dateExit) {
        this.dateExit = dateExit;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    public int getChargeTotal() {
        return chargeTotal;
    }

    public void setChargeTotal(int chargeTotal) {
        this.chargeTotal = chargeTotal;
    }

    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    ContentValues toValues() {
        ContentValues values = new ContentValues(13);

        values.put(VehiclesTable.COLUMN_NAME, name);
        values.put(VehiclesTable.COLUMN_MODEL, model);
        values.put(VehiclesTable.COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(VehiclesTable.COLUMN_PLATE_NUMBER_2, plateNumber2);
        values.put(VehiclesTable.COLUMN_PLATE_LETTER, plateLetter);
        values.put(VehiclesTable.COLUMN_PLATE_NUMBER_3, plateNumber3);
        values.put(VehiclesTable.COLUMN_PLATE_IRAN, plateIran);
        values.put(VehiclesTable.COLUMN_DATE_ENTER, dateEnter);
        values.put(VehiclesTable.COLUMN_DATE_EXIT, dateExit);
        values.put(VehiclesTable.COLUMN_PHOTO, photo);
        values.put(VehiclesTable.COLUMN_EXITED, exited);
        values.put(VehiclesTable.COLUMN_CHARGE_TOTAL, chargeTotal);
        values.put(VehiclesTable.COLUMN_PARKING_ID, parkingId);

        return values;
    }

    @Override
    public String toString() {
        return "VehiclesDataItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", plateNumber2=" + plateNumber2 +
                ", plateLetter='" + plateLetter + '\'' +
                ", plateNumber3=" + plateNumber3 +
                ", plateIran=" + plateIran +
                ", dateEnter='" + dateEnter + '\'' +
                ", dateExit='" + dateExit + '\'' +
                ", photo='" + photo + '\'' +
                ", exited=" + exited +
                ", chargeTotal=" + chargeTotal +
                ", parkingId=" + parkingId +
                '}';
    }
}