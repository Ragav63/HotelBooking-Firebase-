package com.example.hrbapp;

public class RecItOtFacRoom {

    //Recycler items for Room items recycler in Room fragment

    private String extraFacilityName;
    private int extraFacilityPrice;
    private int id;
    private boolean isChecked; // Add a field to track checkbox state

    //default constructor
    public RecItOtFacRoom() {

    }

    public RecItOtFacRoom(String extraFacilityName, int extraFacilityPrice, int id ) {
        this.id=id;
        this.extraFacilityName = extraFacilityName;
        this.extraFacilityPrice = extraFacilityPrice;
        this.isChecked=false;
    }

    public String getExtraFacilityName() {
        return extraFacilityName;
    }

    public void setExtraFacilityName(String extraFacilityName) {
        this.extraFacilityName = extraFacilityName;
    }

    public int getExtraFacilityPrice() {
        return extraFacilityPrice;
    }

    public void setExtraFacilityPrice(int extraFacilityPrice) {
        this.extraFacilityPrice = extraFacilityPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
