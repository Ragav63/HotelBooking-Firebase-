package com.example.admin;

public class PromoCodeDiscount {
    private int id;
    private String promoDiscountName;
    private int promoDiscountPercent;


    public PromoCodeDiscount() {
        this.id=id;
        this.promoDiscountName = promoDiscountName;
        this.promoDiscountPercent = promoDiscountPercent;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
