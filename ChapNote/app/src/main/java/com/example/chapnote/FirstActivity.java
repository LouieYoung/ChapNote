package com.example.chapnote;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;

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
                    case R.id.search:
                        Intent intent = new Intent(FirstActivity.this, SearchActivity.class);
                        startActivity(intent);
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

        Intent  intent = getIntent();
        final int first = intent.getIntExtra("first",1);
        String color=intent.getStringExtra("color");
        listView = (ListView)findViewById(R.id.listView);
        button = (Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        layoutInflater = getLayoutInflater();

        myDatabase = new MyDatabase(this);
        arrayList = myDatabase.getOpen(first);
        final MyAdapterFirst adapter = new MyAdapterFirst(FirstActivity.this,layoutInflater,arrayList,first);
        changeColor(color);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList = myDatabase.getMore(first);
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

}
