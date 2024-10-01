package com.example.hrbapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinAdapterFragDiscounts extends ArrayAdapter<Coupon> {
    private Context context;
    private List<Coupon> items;
    private int textColor;

    public CustomSpinAdapterFragDiscounts(Context context, int resource, List<Coupon> items, int textColor) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.textColor = textColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(items.get(position).getPromoDiscountName());
        textView.setTextColor(textColor);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(items.get(position).getPromoDiscountName());
        textView.setTextColor(textColor);
        return textView;
    }
}
