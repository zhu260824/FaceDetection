package com.zl.face;

import android.content.Context;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class YSQFaceUtil {

    static {
        System.loadLibrary("facedetection");
    }


    public static void init(Context mContext) {
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(mContext) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
            }
        };
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, mContext, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    public static native YSQFaceInfo[] detectCNN(long address);
}
