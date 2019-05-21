package com.example.chapnote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import jackmego.com.jieba_android.JiebaSegmenter;

public class SearchActivity extends AppCompatActivity {

    private Button search;
    private EditText editText;
    private TextView idText;
    private TextView resultText;
    private MyDatabaseHelper dbHelper;//= new MyDataBaseHelper(this,"mymoney1.db",null,1);
    private SQLiteDatabase db;
    private String mcolor;
    private Timer timer;
    private ArrayList<Integer> alforId;//存放取到的所有id
    private String sid="";
    LayoutInflater layoutInflater;
    MyAdapterSearch adapter;
    ListView listView;
    MyDatabase myDatabase;
    ArrayList<Data> array=new ArrayList<Data>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                alforId = searchAll();
                if(!alforId.isEmpty()){
                    for(int i=0;i<alforId.size();i++){
                        array.clear();
                        myDatabase=new MyDatabase(SearchActivity.this);
                        for(int j=0;j<alforId.size();j++){
                            Data a=myDatabase.getDataFromId(alforId.get(j));
                            array.add(a);
                        }
                        adapter = new MyAdapterSearch(SearchActivity.this,layoutInflater,array);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                        sid = sid+alforId.get(i)+",";
                    }
                    idText.setText(sid);
                    sid="";
                }else{
                    idText.setText(sid);
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        JiebaSegmenter.init(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper= new MyDatabaseHelper(SearchActivity.this);
        db = dbHelper.getWritableDatabase();
        //dataIn();
        //数据库初始化测试
        listView=(ListView)findViewById(R.id.listView);
        layoutInflater = getLayoutInflater();



        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        //toolbar部分
        toolbar.setTitle("搜索"); //设置标题
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
                    case R.id.black:
                        changeColor("black");
                        Toast.makeText(SearchActivity.this, "全部",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.red:
                        changeColor("red");
                        Toast.makeText(SearchActivity.this, "红色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.yellow:
                        changeColor("yellow");
                        Toast.makeText(SearchActivity.this, "黄色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.purple:
                        changeColor("purple");
                        Toast.makeText(SearchActivity.this, "紫色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.green:
                        changeColor("green");
                        Toast.makeText(SearchActivity.this, "绿色",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        search = findViewById(R.id.search);
        editText = findViewById(R.id.editText);
        idText = findViewById(R.id.idText);
        resultText = findViewById(R.id.resultText);

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // (1) 使用handler发送消息
                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        },0,2000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),FirstActivity.class);
                intent.putExtra("first",array.get(position).getFirstid());
                intent.putExtra("color",array.get(position).getColor());
                startActivity(intent);
                SearchActivity.this.finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().trim().isEmpty()){
                    ArrayList<String> wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(editText.getText().toString());
                    String ss = "";
                    for(int i=0;i<wordList.size();i++){
                        ss = ss+String.valueOf(wordList.get(i))+",";
                    }
                    resultText.setText(ss);


                    alforId = searchAll();
                    String s="";
                    if(!alforId.isEmpty()){
                        for(int i=0;i<alforId.size();i++){
                            s = s+String.valueOf(alforId.get(i))+",";

                        }
                    }

                    idText.setText(s);

                }

            }
        });
    }
    public ArrayList searchAll(){
        if(!editText.getText().toString().trim().isEmpty()){
            ArrayList<String> wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(editText.getText().toString().trim());
            ArrayList arrayList1 = search(wordList.get(0),mcolor);
            ArrayList arrayList3;
            for(int i=0;i<wordList.size()-1;i++){
                ArrayList arrayList2 = search(wordList.get(i+1),mcolor);
                arrayList3 = forSame(arrayList1,arrayList2);
                if(arrayList3.isEmpty()){
                    arrayList1.clear();
                    break;
                }
                arrayList1.clear();
                arrayList1 = (ArrayList) arrayList3.clone();
                arrayList3.clear();
            }
            return arrayList1;
        }else{
            ArrayList a = new ArrayList();
            return a;
        }
    }

    public ArrayList search(String s,String color){
        Cursor cursor = db.query("note", null, null, null, null, null, null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tag = cursor.getString(cursor.getColumnIndex("text"));
                String col = cursor.getString(cursor.getColumnIndex("color"));
                tag = tag.toLowerCase();
                if(!color.equals("black"))
                {
                    if(tag.indexOf(s)!=-1 & col.equals(color))
                    {
                        arrayList.add(id);
                    }
                }else{
                    if(tag.indexOf(s)!=-1)
                    {
                        arrayList.add(id);
                    }
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList forSame(ArrayList a,ArrayList b){
        ArrayList arrayList3 = new ArrayList();
        for(int j=0;j<a.size();++j){
            if(b.contains(a.get(j))){
                arrayList3.add(a.get(j));
            }
        }
        return arrayList3;
    }

    //Menu部分
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tb_search,menu);
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
        mcolor = color;
        Color.color = color;
        Color.colorPic = ContextCompat.getDrawable(SearchActivity.this,Color.getColorPicId(Color.color));
        Color.colorId = ContextCompat.getColor(SearchActivity.this,Color.getColorId(Color.color));
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitleTextColor(Color.colorId);
        toolbar.setOverflowIcon(Color.colorPic);
    }

//    public void dataIn(){

//        ContentValues values = new ContentValues();
//
//        values.put("text","我在哈尔滨工业大学威海");
//        values.put("color","black");
//        values.put("id","1");
//        db.insert("note",null,values);
//        values.clear();
//
//        values.put("text","我爱哈尔滨大连");
//        values.put("color","red");
//        values.put("id","2");
//        db.insert("note",null,values);
//        values.clear();
//
//        values.put("text","戴尔android什么东西");
//        values.put("id","3");
//        values.put("color","red");
//        db.insert("note",null,values);
//        values.clear();
//
//        values.put("text","第三次的Android项目报告");
//        values.put("id","4");
//        values.put("color","yellow");
//        db.insert("note",null,values);
//        values.clear();
//
//        values.put("text","项目的第三次报告");
//        values.put("id","5");
//        values.put("color","red");
//        db.insert("note",null,values);
//        values.clear();
//
//        values.put("text","我爱中国");
//        values.put("id","6");
//        values.put("color","purple");
//        db.insert("note",null,values);
//        values.clear();
//    }
}