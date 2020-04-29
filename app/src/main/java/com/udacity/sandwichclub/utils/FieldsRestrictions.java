package com.udacity.sandwichclub.utils;

import android.graphics.Color;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.udacity.sandwichclub.R;

public class FieldsRestrictions {

    public static boolean editTextIsEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            setEditTextRestrictionsIndicators(editText);
            return true;
        } else return false;
    }

    private static void setEditTextRestrictionsIndicators(EditText editText) {
        editText.setHintTextColor(Color.parseColor(Constants.HINT_TEXT_COLOR));
        editText.setHint(R.string.cannot_be_empty);
    }
}
