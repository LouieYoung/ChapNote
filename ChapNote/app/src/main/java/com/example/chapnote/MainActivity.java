package com.example.chapnote;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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


public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    Button button;
    LayoutInflater layoutInflater;
    ArrayList<Data> arrayList;
    MyDatabase myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        //toolbar部分
        toolbar.setTitle("小书笔记"); //设置标题
        toolbar.setTitleMarginStart(72);
        changeColor(Color.color);//设置颜色
        setSupportActionBar(toolbar); //这里注意为固定写法
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) //得到被点击的item的itemId
                {
                    case R.id.search:
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.black:
                        changeColor("black");
                        Toast.makeText(MainActivity.this, "全部", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.red:
                        changeColor("red");
                        Toast.makeText(MainActivity.this, "红色", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.yellow:
                        changeColor("yellow");
                        Toast.makeText(MainActivity.this, "黄色", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.purple:
                        changeColor("purple");
                        Toast.makeText(MainActivity.this, "紫色", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.green:
                        changeColor("green");
                        Toast.makeText(MainActivity.this, "绿色", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


        listView = (ListView)findViewById(R.id.listView);
        button = (Button)findViewById(R.id.button);
        layoutInflater = getLayoutInflater();

        myDatabase = new MyDatabase(this);
        arrayList = myDatabase.getFirst();
        final MyAdapter adapter = new MyAdapter(MainActivity.this,layoutInflater,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList = myDatabase.getFirst();
                MyAdapter adapter = new MyAdapter(MainActivity.this,layoutInflater,arrayList);
                listView.setAdapter(adapter);
                Intent intent = new Intent(getApplicationContext(),FirstActivity.class);
                intent.putExtra("first",arrayList.get(position).getFirstid());
                intent.putExtra("color",arrayList.get(position).getColor());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                arrayList.clear();
                arrayList = myDatabase.getFirst();
                adapter.notifyDataSetChanged();
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("是否删除此条目？")
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toDeleteFirst(arrayList.get(position).getId(),arrayList.get(position).getFirstid());
                                arrayList = myDatabase.getarray();
                                MyAdapter adapter = new MyAdapter(MainActivity.this,layoutInflater,arrayList);
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
                int fid=maxFirstid()+1;
                Data a=new Data(id,fid,0,0,text,color,time,"开");
                myDatabase.toInsert(a);
                arrayList = myDatabase.getFirst();
                MyAdapter adapter = new MyAdapter(MainActivity.this,layoutInflater,arrayList);
                listView.setAdapter(adapter);
            }
        });
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

    //Menu部分
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tb,menu);
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
        Color.colorPic = ContextCompat.getDrawable(MainActivity.this,Color.getColorPicId(Color.color));
        Color.colorId = ContextCompat.getColor(MainActivity.this,Color.getColorId(Color.color));
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitleMarginStart(72);
        toolbar.setTitleTextColor(Color.colorId);
        toolbar.setOverflowIcon(Color.colorPic);
        toolbar.setLogo(Color.colorPic);
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
    public int maxFirstid(){
        int MaxFirstid=0;
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getarray();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getFirstid()>MaxFirstid){
                MaxFirstid=arr.get(i).getFirstid();
            }
        }
        return MaxFirstid;
    }


}
