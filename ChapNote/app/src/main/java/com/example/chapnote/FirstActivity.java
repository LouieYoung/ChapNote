package com.example.chapnote;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FirstActivity extends AppCompatActivity {
    ListView listView;
    EditText editText;
    Button button;
    Button button2;
    LayoutInflater layoutInflater;
    ArrayList<Data> arrayList;
    MyDatabase myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        Intent  intent = getIntent();
        final int first = intent.getIntExtra("first",1);
        String color=intent.getStringExtra("color");
        listView = (ListView)findViewById(R.id.listView);
        button = (Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        layoutInflater = getLayoutInflater();

        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        //toolbar部分
        toolbar.setTitle("笔记详情"); //设置标题
        toolbar.setTitleMarginStart(72);
        changeColor(Color.color);//设置颜色
        setSupportActionBar(toolbar); //这里注意为固定写法
        //获取ActionBar
        ActionBar bar=getSupportActionBar();
        //允许返回指定的父Activity
        bar.setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) //得到被点击的item的itemId
                {
                    case R.id.share:
                        popShotSrceenDialog();
                        break;
                    case R.id.black:
                        changeColor("black");
                        Toast.makeText(FirstActivity.this, "全部",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.red:
                        changeColor("red");
                        Toast.makeText(FirstActivity.this, "红色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.yellow:
                        changeColor("yellow");
                        Toast.makeText(FirstActivity.this, "黄色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.purple:
                        changeColor("purple");
                        Toast.makeText(FirstActivity.this, "紫色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.green:
                        changeColor("green");
                        Toast.makeText(FirstActivity.this, "绿色",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


        myDatabase = new MyDatabase(this);
        arrayList = myDatabase.getOpen(first);
        final MyAdapterFirst adapter = new MyAdapterFirst(FirstActivity.this,layoutInflater,arrayList,first);
        changeColor(color);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList = myDatabase.getMore(first);
                if(arrayList.get(position).getThirdid()==0){
                    int secondid=arrayList.get(position).getSecondid();
                    for(int i=0;i<arrayList.size();i++){
                        if(arrayList.get(i).getSecondid()==secondid){
                            if(arrayList.get(i).getOpen().equals("开")){
                                arrayList.get(i).setOpen("闭");
                                myDatabase.toUpdate(arrayList.get(i));
                                continue;
                            }else if(!arrayList.get(i).getOpen().equals("开")){
                                arrayList.get(i).setOpen("开");
                                myDatabase.toUpdate(arrayList.get(i));
                                continue;
                            }
                        }
                    }
                }


                arrayList = myDatabase.getOpen(first);
                MyAdapterFirst adapter = new MyAdapterFirst(FirstActivity.this,layoutInflater,arrayList,first);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                arrayList.clear();
                arrayList = myDatabase.getOpen(first);
                adapter.notifyDataSetChanged();
                new AlertDialog.Builder(FirstActivity.this)
                        .setMessage("是否删除此条目？")
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(arrayList.get(position).getThirdid()!=0){
                                    toDeleteThird(arrayList.get(position).getId(),arrayList.get(position).getFirstid(),arrayList.get(position).getSecondid(),arrayList.get(position).getThirdid());
                                }else if(arrayList.get(position).getSecondid()!=0){
                                    toDeleteSecond(arrayList.get(position).getId(),arrayList.get(position).getFirstid(),arrayList.get(position).getSecondid());
                                }else{
                                    toDeleteFirst(arrayList.get(position).getId(),arrayList.get(position).getFirstid());
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    FirstActivity.this.finish();
                                }
                                myDatabase.toDelete(arrayList.get(position).getId());
                                arrayList = myDatabase.getOpen(first);
                                MyAdapterFirst adapter = new MyAdapterFirst(FirstActivity.this,layoutInflater,arrayList,first);
                                listView.setAdapter(adapter);
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat myFmt=new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date(System.currentTimeMillis());
                String time=myFmt.format(date);
                editText=(EditText)findViewById(R.id.editText);
                String text=editText.getText().toString();
                String color=Color.color;
                int id=maxId()+1;
                int sid=maxSecondid(first)+1;
                Data a=new Data(id,first,sid,0,text,color,time,"开");
                myDatabase.toInsert(a);
                arrayList = myDatabase.getMore(first);
                MyAdapterFirst adapter = new MyAdapterFirst(FirstActivity.this,layoutInflater,arrayList,first);
                listView.setAdapter(adapter);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat myFmt=new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date(System.currentTimeMillis());
                String time=myFmt.format(date);
                editText=(EditText)findViewById(R.id.editText);
                String text=editText.getText().toString();
                String color=Color.color;
                int id=maxId()+1;
                int sid=maxSecondid(first);
                int tid=maxThirdid(first)+1;

                String open="开";
                    ArrayList<Data> arr=new ArrayList<Data>();
                    arr=myDatabase.getMore(first);
                    for(int i=0;i<arr.size();i++){
                        if(arr.get(i).getSecondid()==sid&&arr.get(i).getThirdid()==0){
                            open=arr.get(i).getOpen();
                        }
                    }

                Data a=new Data(id,first,sid,tid,text,color,time,open);
                if(sid==0){
                    a=new Data(id,first,1,0,text,color,time,"开");
                }
                if(a.getOpen().equals("闭")){
                    Toast.makeText(FirstActivity.this,"新建了一条隐藏状态的子项",Toast.LENGTH_SHORT).show();
                }
                myDatabase.toInsert(a);
                arrayList = myDatabase.getOpen(first);
                MyAdapterFirst adapter = new MyAdapterFirst(FirstActivity.this,layoutInflater,arrayList,first);
                listView.setAdapter(adapter);
            }
        });

    }



    //Menu部分
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tb_first,menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    public void changeColor(String color){
        Color.color = color;
        Color.colorPic = ContextCompat.getDrawable(FirstActivity.this,Color.getColorPicId(Color.color));
        Color.colorId = ContextCompat.getColor(FirstActivity.this,Color.getColorId(Color.color));
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitleMarginStart(72);
        toolbar.setTitleTextColor(Color.colorId);
        toolbar.setOverflowIcon(Color.colorPic);
    }

    public void toDeleteFirst(int id,int firstid){
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getarray();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getFirstid()==firstid){
                myDatabase.toDelete(arr.get(i).getId());
            }else if(arr.get(i).getFirstid()>firstid){
                arr.get(i).setFirstid(arr.get(i).getFirstid()-1);
                myDatabase.toUpdate(arr.get(i));
            }
        }
    }
    public void toDeleteSecond(int id,int firstid,int secondid){
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getarray();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getFirstid()==firstid){
                if(arr.get(i).getSecondid()==secondid){
                    myDatabase.toDelete(arr.get(i).getId());
                }else if(arr.get(i).getSecondid()>secondid){
                    arr.get(i).setSecondid(arr.get(i).getSecondid()-1);
                    myDatabase.toUpdate(arr.get(i));
                }
            }

        }
    }
    public void toDeleteThird(int id,int firstid,int secondid,int thirdid){
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getarray();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getFirstid()==firstid&&arr.get(i).getSecondid()==secondid){
                if(arr.get(i).getThirdid()==thirdid){
                    myDatabase.toDelete(arr.get(i).getId());
                }else if(arr.get(i).getThirdid()>thirdid){
                    arr.get(i).setThirdid(arr.get(i).getThirdid()-1);
                    myDatabase.toUpdate(arr.get(i));
                }
            }

        }
    }

    public int maxId(){
        int MaxId=0;
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getarray();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getId()>MaxId){
                MaxId=arr.get(i).getId();
            }
        }
        return MaxId;
    }
    public int maxSecondid(int first){
        int MaxSecondid=0;
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getMore(first);
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getSecondid()>MaxSecondid){
                MaxSecondid=arr.get(i).getSecondid();
            }
        }
        return MaxSecondid;
    }
    public int maxThirdid(int first){
        int MaxThirdid=0;
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getMore(first);
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getSecondid()==maxSecondid(first)&&arr.get(i).getThirdid()>MaxThirdid){
                MaxThirdid=arr.get(i).getThirdid();
            }
        }
        return MaxThirdid;
    }

    //分享模块
    final public int CODE = 007;

    View view;
    File file;
    Bitmap temBitmap;
    String activityTitle;
    String msgTitle;
    String msgText;
    String imgPath;
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
                cutDialog.dismiss();
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
                Toast.makeText(FirstActivity.this,"保存失败，请检查权限或清理内存",Toast.LENGTH_SHORT).show();
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

}
