package com.example.admin;

public class ExtraFacility {
    private int id;
    private String extraFacilityName;
    private int extraFacilityPrice;


    public ExtraFacility() {
        this.id=id;
        this.extraFacilityName = extraFacilityName;
        this.extraFacilityPrice = extraFacilityPrice;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExtraFacilityName() {
        return extraFacilityName;
    }

    public void setExtraFacilityName(String extraFacilityName) {
        this.extraFacilityName = extraFacilityName;
    }

    public int getExtraFacilityPrice() {
        return  extraFacilityPrice;
    }

    public void setExtraFacilityPrice(int extraFacilityPrice) {
        this.extraFacilityPrice = extraFacilityPrice;
    }
}
