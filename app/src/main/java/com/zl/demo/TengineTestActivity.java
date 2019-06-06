package com.zl.demo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.FileNotFoundException;

public class TengineTestActivity extends Activity {
    TextView tv = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tengine_test);


        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }


        tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("Wait for choosing a Picture");
        ImageView imageView = (ImageView) findViewById(R.id.image);
        //try init cash reopen finally click


//        if (TengineTest.TengineWrapperInit()>0)
//            tv.setText("Exit,try again");



        Button choose = (Button) findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {

            //choose Image
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

            }
            private void selectPic(){
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }


        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());

            ContentResolver cr = getContentResolver();
            try {

                //Uri originalUri=data.getData();
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));


                tv.setText("run App...");
//                if(TengineTest.RunMobilenet(bitmap)>0)
//                    tv.setText("run App error !!!!!!!");
//                else {
//                    tv.setText(TengineTest.TengineWrapperGetTop1());
//
//                }


                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }else {
            Log.i("MainActivity", "error");
        }
        TengineTestActivity.super.onActivityResult(requestCode,requestCode,data);


    }

    //hold app at background
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);


    }

}
