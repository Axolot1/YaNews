package com.axolotl.yanews.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

public class TextViewUtil {
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setTextViewAlpha(View tv, float alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tv.setAlpha(alpha);
        } else {
            int precolor = ((TextView) tv).getCurrentTextColor();
            precolor = (precolor & 0x00ffffff) | ((int) (alpha*255))<<24;
            ((TextView) tv).setTextColor(precolor);
        }
    }
    
    /**
     * 给TextView设置字体颜色
     * @param v
     * @param color
     */
    public static void setTextColor(View v, int color) {
        if (v != null) {
            ((TextView) v).setTextColor(color);
        }
    }

    /**
     * 给TextView设置文本
     * @param v
     * @param text
     */
    public static void setText(View v, String text) {
        if (v != null) {
            ((TextView) v).setText(text);
        }
    }

    /**
     * 获取TextView的文本
     * @param v
     * @return text
     */
    public static String getText(View v) {
        String text = null;
        try {
            if (v != null) {
                text = ((TextView) v).getText().toString();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (null == text) {
            text = "";
        }
        return text;
    }
}
