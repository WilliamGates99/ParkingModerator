package com.xeniac.parkingmoderator.database.parkingsTable;

import android.content.ContentValues;

public class ParkingDataItem {

    private int id;
    private String email;
    private String name;
    private int capacity;
    private int occupiedSpace;
    private int chargeBase;
    private int chargeExtra;
    private String password;
    private String question;
    private String answer;

    public ParkingDataItem() {
    }

    public ParkingDataItem(int id, String email,
                           String name, int capacity, int occupiedSpace, int chargeBase,
                           int chargeExtra, String password, String question, String answer) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.capacity = capacity;
        this.occupiedSpace = occupiedSpace;
        this.chargeBase = chargeBase;
        this.chargeExtra = chargeExtra;
        this.password = password;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupiedSpace() {
        return occupiedSpace;
    }

    public void setOccupiedSpace(int occupiedSpace) {
        this.occupiedSpace = occupiedSpace;
    }

    public int getChargeBase() {
        return chargeBase;
    }

    public void setChargeBase(int chargeBase) {
        this.chargeBase = chargeBase;
    }

    public int getChargeExtra() {
        return chargeExtra;
    }

    public void setChargeExtra(int chargeExtra) {
        this.chargeExtra = chargeExtra;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    ContentValues toValues() {
        ContentValues values = new ContentValues(9);

        values.put(ParkingTable.COLUMN_EMAIL, email);
        values.put(ParkingTable.COLUMN_NAME, name);
        values.put(ParkingTable.COLUMN_CAPACITY, capacity);
        values.put(ParkingTable.COLUMN_OCCUPIED_SPACE, occupiedSpace);
        values.put(ParkingTable.COLUMN_CHARGE_BASE, chargeBase);
        values.put(ParkingTable.COLUMN_CHARGE_EXTRA, chargeExtra);
        values.put(ParkingTable.COLUMN_PASSWORD, password);
        values.put(ParkingTable.COLUMN_QUESTION, question);
        values.put(ParkingTable.COLUMN_ANSWER, answer);

        return values;
    }

    @Override
    public String toString() {
        return "ParkingDataItem{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", occupiedSpace=" + occupiedSpace +
                ", chargeBase=" + chargeBase +
                ", chargeExtra=" + chargeExtra +
                ", password='" + password + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}