package com.zl.tengine;

import android.graphics.Bitmap;

public class TengineTest {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

    public static native int TengineWrapperInit();

    public static native int RunMobilenetFromFile(String file);

    public static native int RunMobilenet(Bitmap bitmap);

    public static native String TengineWrapperGetTop1();
}
