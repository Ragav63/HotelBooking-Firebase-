package com.example.hrbapp;

public class Coupon {

    private String promoDiscountName;
    private int promoDiscountPercent;

    public Coupon() {
        // Default constructor required for calls to DataSnapshot.getValue(Coupon.class)
    }

    public String getPromoDiscountName() {
        return promoDiscountName;
    }

    public void setPromoDiscountName(String promoDiscountName) {
        this.promoDiscountName = promoDiscountName;
    }

    public int getPromoDiscountPercent() {
        return promoDiscountPercent;
    }

    public void setPromoDiscountPercent(int promoDiscountPercent) {
        this.promoDiscountPercent = promoDiscountPercent;
    }

    @Override
    public String toString() {
        return promoDiscountName;
    }
}
