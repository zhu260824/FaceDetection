package com.zl.demo;

import android.graphics.Rect;

public class RectUtil {

    public static Rect opencvRect2AndroidRect(org.opencv.core.Rect rect) {
        if (null == rect) return null;
        int left = rect.x;
        int top = rect.y;
        int right = rect.x + rect.width;
        int bottom = rect.y + rect.height;
        return new Rect(left, top, right, bottom);
    }

    public static org.opencv.core.Rect androidRect2OpencvRect(Rect rect) {
        if (null == rect) return null;
        int x = rect.left;
        int y = rect.top;
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;
        return new org.opencv.core.Rect(x, y, width, height);
    }
}
