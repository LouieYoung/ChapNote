package com.example.hui.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    final public int CODE = 007;

    View view;
    File file;
    Bitmap temBitmap;
    String activityTitle;
    String msgTitle;
    String msgText;
    String imgPath;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        initPhotoError();
        //popShotSrceenDialog();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            //File f = new File(imgPath);
            if ( file!= null && file.exists() && file.isFile()) {
                intent.setType("image/png");
                Uri u = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM,u);
            }
        }
//        intent.setType("image/jpg");
//        Uri u = Uri.fromFile(file);
//        intent.putExtra(Intent.EXTRA_STREAM,u);
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
//    public void shareMsg(){
//        Uri pa=Uri.fromFile(new File(imgPath));//根据路径转化为uri
//        Intent imageIntent = new Intent(Intent.ACTION_SEND);//调用系统的ACTION_SEND
//        imageIntent.setType("image/jpg");
//        imageIntent.putExtra(Intent.EXTRA_STREAM, pa);//EXTRA_STREAM对应转化为uri的path
//        imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(imageIntent,"分享给"));
//    }
    private void popShotSrceenDialog(){
        final AlertDialog cutDialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.show_cut_screen_layout, null);
        ImageView showImg = (ImageView) dialogView.findViewById(R.id.show_cut_screen_img);
        dialogView.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cutDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.share_img).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                //shareMsg();
                shareMsg(activityTitle,msgTitle,msgText,imgPath);
                //Toast.makeText(MainActivity.this,"点击了share按钮",Toast.LENGTH_SHORT).show();
            }
        });
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        temBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        //找到当前页面的跟布局
        view = getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
        if (temBitmap != null)
        {
            try {
                // 获取内存路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径,获取系统时间
                long time=System.currentTimeMillis();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                java.util.Date date=new java.util.Date(time);
                String str=sdf.format(date);
                imgPath = sdCardPath + File.separator +str+"screenshot.png";

                file = new File(imgPath);
                FileOutputStream os = new FileOutputStream(file);
                temBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this,"保存失败，请检查权限或清理内存",Toast.LENGTH_SHORT).show();
            }
        }
        showImg.setImageBitmap(temBitmap);

        cutDialog.setView(dialogView);
        Window window = cutDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6
        p.gravity = Gravity.CENTER;//设置弹出框位置
        window.setAttributes(p);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        cutDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_1=(Button)findViewById(R.id.cut);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popShotSrceenDialog();
            }
        });
    }
}