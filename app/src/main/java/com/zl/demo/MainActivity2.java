package com.zl.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import zeusees.tracking.Face;
import zeusees.tracking.Hyper;

public class MainActivity2 extends Activity {
    private ImageView ivT1, ivT2;
    private Button btnT1, btnT2, btnAll;
    private TextView tvT1, tvT2, tvAll;
    private Hyper hyper;

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
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ncnn";
        hyper = new Hyper(MainActivity2.this, path);
    }

    private String detect(Context mContext, String assetsPath, ImageView imageView) {
        String msg = "";
        Bitmap bitmap = ImageUtil.getBitmapByAssets(mContext, assetsPath);
        msg = msg + "image size = " + bitmap.getWidth() + "x" + bitmap.getHeight() + "\n";
        Bitmap cb = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(cb);
        //图像上画矩形
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充
        paint.setStrokeWidth(10);  //线的宽度

        long startTime = System.currentTimeMillis();
        byte[] data = ImageUtil.bitmapToNv21(bitmap, bitmap.getWidth(), bitmap.getHeight());
        hyper.detect(data, bitmap.getWidth(), bitmap.getHeight());
        List<Face> faces = hyper.getTrackingInfo();
        msg = msg + "face num = " + faces.size() + "\n";
        msg = msg + "detectTime = " + (System.currentTimeMillis() - startTime) + "ms";
        for (Face face : faces) {
            Log.i("canshu", face.toString());
            canvas.drawRect(face.getLeft(), face.getTop(), face.getRight(), face.getBottom(), paint);
        }
        imageView.post(() -> imageView.setImageBitmap(cb));
        return msg;
    }
}
