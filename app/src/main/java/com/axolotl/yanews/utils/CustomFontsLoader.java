package com.axolotl.yanews.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

public class CustomFontsLoader {

    private static HashMap<String, Typeface> fontsMap = new HashMap<>();

    /**
     * Returns a loaded custom font based on it's identifier.
     *
     * @param context        - the current context
     * @param fontIdentifier = the identifier of the requested font
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, String fontIdentifier) {
        Log.i("font", "get typeface " + fontIdentifier);
        Typeface typeface = fontsMap.get(fontIdentifier);
        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getAssets(), fontIdentifier);
            fontsMap.put(fontIdentifier, typeface);
        }
        return typeface;
    }

}