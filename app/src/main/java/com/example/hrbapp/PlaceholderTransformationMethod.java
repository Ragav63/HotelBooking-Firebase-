package com.example.hrbapp;

import android.text.SpannableStringBuilder;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class PlaceholderTransformationMethod extends PasswordTransformationMethod {

    private char placeholderChar;

    public PlaceholderTransformationMethod(char placeholderChar) {
        this.placeholderChar = placeholderChar;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {

        private CharSequence source;

        public PasswordCharSequence(CharSequence source) {
            this.source = source;
        }

        @Override
        public int length() {
            return source.length();
        }

        @Override
        public char charAt(int index) {
            return placeholderChar;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new SpannableStringBuilder(source, start, end);
        }
    }
}

