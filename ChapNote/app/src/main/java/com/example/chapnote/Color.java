package com.example.chapnote;

import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;

public class Color extends Func {
    public static String color="black";
    public static Drawable colorPic;
    public static int colorId;
    public static int getColorId(String variableName) {
        int color = getId(variableName,R.color.class);
        return color;
    }
    public static int getColorPicId(String variableName) {
        int colorPicId = getId(variableName,R.drawable.class);
        return colorPicId;
    }
    public static int getId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
