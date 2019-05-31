package com.zl.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class MTCNN {
    static {
        System.loadLibrary("mtcnn");
    }

    public void init(Context mContext) throws IOException {
        try {
            copyBigDataToSD(mContext, "det1.bin");
            copyBigDataToSD(mContext, "det2.bin");
            copyBigDataToSD(mContext, "det3.bin");
            copyBigDataToSD(mContext, "det1.param");
            copyBigDataToSD(mContext, "det2.param");
            copyBigDataToSD(mContext, "det3.param");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //模型初始化
        File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        String sdPath = sdDir.toString() + "/mtcnn/";
        initModelPath(sdPath);
    }


    //根据模型初始化
    public native boolean initModelPath(String modelPath);

    //注销
    public native boolean unInit();

    //检测的最小人脸设置
    public native boolean setMinFace(int size);

    //线程设置
    public native boolean setDetectThread(int threadNumber);

    //循环测试次数
    public native boolean setTimeCount(int count);

    public native MTFaceInfo[] detect(Bitmap bitmap);

    public native MTFaceInfo[] maxDetect(Bitmap bitmap);


    private void copyBigDataToSD(Context mContext, String strOutFileName) throws IOException {
        Log.i(TAG, "start copy file " + strOutFileName);
        File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        File file = new File(sdDir.toString() + "/mtcnn/");
        if (!file.exists()) {
            file.mkdir();
        }
        String tmpFile = sdDir.toString() + "/mtcnn/" + strOutFileName;
        File f = new File(tmpFile);
        if (f.exists()) {
            Log.i(TAG, "file exists " + strOutFileName);
            return;
        }
        InputStream myInput;
        java.io.OutputStream myOutput = new FileOutputStream(sdDir.toString() + "/mtcnn/" + strOutFileName);
        myInput = mContext.getAssets().open(strOutFileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
        Log.i(TAG, "end copy file " + strOutFileName);
    }
}
