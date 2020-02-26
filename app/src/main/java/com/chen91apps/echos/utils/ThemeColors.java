package com.chen91apps.echos.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

public class ThemeColors {

    public static int getColor(Context context, int colorResId) {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[] {colorResId});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        return color;
    }
}
