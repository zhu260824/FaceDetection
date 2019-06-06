package com.zl.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.face.YSQFaceInfo;
import com.zl.face.YSQFaceUtil;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class MainActivity extends AppCompatActivity {
    private ImageView ivT1, ivT2;
    private Button btnT1, btnT2, btnAll;
    private TextView tvT1, tvT2, tvAll;
//    private MTCNN mtcnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivT1 = findViewById(R.id.iv_t1);
        btnT1 = findViewById(R.id.btn_t1);
        tvT1 = findViewById(R.id.tv_t1);
        ivT2 = findViewById(R.id.iv_t2);
        btnT2 = findViewById(R.id.btn_t2);
        tvT2 = findViewById(R.id.tv_t2);
        btnAll = findViewById(R.id.btn_all);
        tvAll = findViewById(R.id.tv_all);
        ivT1.post(() -> ivT1.setImageBitmap(ImageUtil.getBitmapByAssets(ivT1.getContext(), "test.jpg")));
        ivT2.post(() -> ivT2.setImageBitmap(ImageUtil.getBitmapByAssets(ivT2.getContext(), "test2.jpg")));
        btnT1.setOnClickListener(v -> {
            String msg = detect(v.getContext(), "test.jpg", ivT1);
            tvT1.setText(msg);
        });
        btnT2.setOnClickListener(v -> {
            String msg = detect(v.getContext(), "test2.jpg", ivT2);
            tvT2.setText(msg);
        });
        YSQFaceUtil.init(MainActivity.this);
//        mtcnn = new MTCNN();
//        try {
//            mtcnn.init(MainActivity.this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private String detect(Context mContext, String assetsPath, ImageView imageView) {
        String msg = "";
        Bitmap bitmap = ImageUtil.getBitmapByAssets(mContext, assetsPath);
        msg = msg + "image size = " + bitmap.getWidth() + "x" + bitmap.getHeight() + "\n";
        Mat mat = new MatOfRect();
        Bitmap cb = bitmap.copy(bitmap.getConfig(), true);
        Utils.bitmapToMat(bitmap, mat);
        Scalar FACE_RECT_COLOR = new Scalar(255.0, 0.0, 0.0);
        int FACE_RECT_THICKNESS = 3;
        long startTime = System.currentTimeMillis();
        YSQFaceInfo[] faces = YSQFaceUtil.detectCNN(mat.nativeObj);
        msg = msg + "face num = " + faces.length + "\n";
        msg = msg + "detectTime = " + (System.currentTimeMillis() - startTime) + "ms";
        for (YSQFaceInfo faceInfo : faces) {
            Imgproc.rectangle(mat, RectUtil.androidRect2OpencvRect(faceInfo.getFaceRect()), FACE_RECT_COLOR, FACE_RECT_THICKNESS);
        }
        Utils.matToBitmap(mat, cb);
        imageView.post(() -> imageView.setImageBitmap(cb));
        return msg;
    }

    private String detectMTCNN(Context mContext, String assetsPath, ImageView imageView) {
        String msg = "";
        Bitmap bitmap = ImageUtil.getBitmapByAssets(mContext, assetsPath);
        msg = msg + "image size = " + bitmap.getWidth() + "x" + bitmap.getHeight() + "\n";
        Bitmap cb = bitmap.copy(bitmap.getConfig(), true);
        Mat mat = new MatOfRect();
        Utils.bitmapToMat(bitmap, mat);
        Scalar FACE_RECT_COLOR = new Scalar(255.0, 0.0, 0.0);
        int FACE_RECT_THICKNESS = 3;
        long startTime = System.currentTimeMillis();
//        MTFaceInfo[] faces = mtcnn.detect(bitmap);
//        msg = msg + "face num = " + faces.length + "\n";
        msg = msg + "detectTime = " + (System.currentTimeMillis() - startTime) + "ms";
//        for (MTFaceInfo face : faces) {
//            Log.i("MTCNN",face.toString());
//            Imgproc.rectangle(mat, RectUtil.androidRect2OpencvRect(face.getRect()), FACE_RECT_COLOR, FACE_RECT_THICKNESS);
//        }
        Utils.matToBitmap(mat, cb);
        imageView.post(() -> imageView.setImageBitmap(cb));
        return msg;
    }
}

