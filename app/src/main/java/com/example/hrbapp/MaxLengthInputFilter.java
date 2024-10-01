package com.example.hrbapp;

import android.text.InputFilter;
import android.text.Spanned;

public class MaxLengthInputFilter implements InputFilter {

    private int maxLength;

    public MaxLengthInputFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = maxLength - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return ""; // Remove characters from the incoming input
        } else if (keep >= end - start) {
            return null; // Keep all characters from the incoming input
        } else {
            return source.subSequence(start, start + keep); // Truncate characters from the incoming input
        }
    }
}

