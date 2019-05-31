package com.zl.demo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    public static Bitmap getBitmapByAssets(Context mContext, String assetsPath) {
        Bitmap bitmap = null;
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream is = assetManager.open(assetsPath);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
