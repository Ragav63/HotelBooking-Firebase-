package com.example.hrbapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapterFragments extends ArrayAdapter<String> {

    private Context context;
    private String[] items;
    private int textColor;

    public CustomSpinnerAdapterFragments(Context context, int resource, String[] items, int textColor) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.textColor = textColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setTextColor(textColor);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setTextColor(textColor);
        return textView;
    }
}