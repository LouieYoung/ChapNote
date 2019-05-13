package com.example.chapnote;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirstActivity extends AppCompatActivity {
    private int MAX=8;
    private int[] first= new int[]{1,1,1,1,1,1,3,2};
    private int[] secon= new int[]{0,1,1,2,2,1,0,0};
    private int[] third= new int[]{0,0,1,1,0,2,0,0};
    private String[] te =
            new String[]{"苹果","柠檬", "梨", "菠萝","西瓜","草莓","香蕉","卢奕阳"};
    private String[] op =
            new String[]{"开","闭", "闭", "闭","闭", "闭", "开", "开"};
    private boolean out = true;
    private int inid=0;
    public static final String CONTENT_URI = "content://com.example.chapnote";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
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

        for(int i=0;i<MAX;i++){
            ContentValues values = new ContentValues();
            values.put("firstid",first[i]);
            values.put("seconid",secon[i]);
            values.put("thirdid",third[i]);
            values.put("text",te[i]);
            values.put("open",op[i]);
            getContentResolver().insert(Uri.parse(CONTENT_URI),values);
            values.clear();
        }
        final TextView tv=(TextView)findViewById(R.id.textView2);
        final Button btn1=(Button)findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str="";
                Cursor cursor1=getContentResolver().query(Uri.parse(CONTENT_URI),null,null,null,"firstid asc,seconid asc,thirdid asc");
                if(cursor1.moveToFirst()){
                    do{
                        int firstid=cursor1.getInt(cursor1.getColumnIndex("firstid"));
                        String str1 = String.format("%d",firstid);
                        int seconid=cursor1.getInt(cursor1.getColumnIndex("seconid"));
                        String str2=String.format("%d",seconid);
                        int thirdid=cursor1.getInt(cursor1.getColumnIndex("thirdid"));
                        String str3=String.format("%d",thirdid);
                        String str4 =cursor1.getString(cursor1.getColumnIndex("text"));
                        String str5 =cursor1.getString(cursor1.getColumnIndex("open"));
                        str+=str1+str2+str3+str4+str5+"\n";
                    }while(cursor1.moveToNext());
                }
                cursor1.close();
                tv.setText(str);
            }
        });

        final Button btn3=(Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(inid);
                show();
            }
        });
        show();
    }
    public void show(){
        List<Map<String,Object>> listItems =new ArrayList<Map<String,Object>>();
        Cursor cursor1=getContentResolver().query(Uri.parse(CONTENT_URI),null,null,null,"firstid asc,seconid asc,thirdid asc");
        if(cursor1.moveToFirst()){
            do{
                int firstid=cursor1.getInt(cursor1.getColumnIndex("firstid"));
                int seconid=cursor1.getInt(cursor1.getColumnIndex("seconid"));
                int thirdid=cursor1.getInt(cursor1.getColumnIndex("thirdid"));
                String text =cursor1.getString(cursor1.getColumnIndex("text"));
                String open =cursor1.getString(cursor1.getColumnIndex("open"));

                if(open.equals("闭")){
                    continue;
                }
                Map<String,Object> map =new HashMap<String, Object>();
                if(seconid==0&&thirdid==0)
                {
                    map.put("blank", "");
                }
                if(seconid!=0&&thirdid==0)
                {
                    map.put("blank", "丨   ");
                }
                if(seconid!=0&&thirdid!=0)
                {
                    map.put("blank", "丨   丨   ");
                }
                map.put("text",text);
                listItems.add(map);
            }while(cursor1.moveToNext());
        }
        cursor1.close();
        SimpleAdapter adapter= new SimpleAdapter(this,
                listItems,
                R.layout.listcontent,
                new String[]{"blank","text"},
                new int[]{R.id.blank,R.id.text}){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final int p = position;
                final View view=super.getView(position,convertView,parent);
                Button btn1=(Button)view.findViewById(R.id.button);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int changeopenid=findWhoIsSelected(p);
                        changeopen(changeopenid);
                        show();
                    }
                });
                Button btn2=(Button)view.findViewById(R.id.button2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int deleteid=findWhoIsSelected(p);
                        delete(deleteid);
                        show();
                    }
                });
                Button btn3=(Button)view.findViewById(R.id.button3);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int moveid=findWhoIsSelected(p);
                        move(moveid);
                        show();
                    }
                });
                return view;
            }
        };
        ListView lv1=(ListView)findViewById(R.id.listView);
        lv1.setAdapter(adapter);
    }

    public int findWhoIsSelected(int a){
        int count=0;
        int changeid=0;
        Cursor cursor1=getContentResolver().query(Uri.parse(CONTENT_URI),null,null,null,"firstid asc,seconid asc,thirdid asc");
        if(cursor1.moveToFirst()){
            do{
                int id=cursor1.getInt(cursor1.getColumnIndex("id"));
                int firstid=cursor1.getInt(cursor1.getColumnIndex("firstid"));
                int seconid=cursor1.getInt(cursor1.getColumnIndex("seconid"));
                int thirdid=cursor1.getInt(cursor1.getColumnIndex("thirdid"));
                String text =cursor1.getString(cursor1.getColumnIndex("text"));
                String open =cursor1.getString(cursor1.getColumnIndex("open"));
                if(open.equals("开")&&count==a){
                    changeid=id;
                    break;
                }
                if(open.equals("开")&&count!=a){
                    count=count+1;
                }
            }while(cursor1.moveToNext());
        }
        cursor1.close();
        return changeid;
    }

    public void changeopen(int a){
        int firstid=0;
        int seconid=0;
        int thirdid=0;
        String text ="";
        String open ="";
        String idString=String.format("%d",a);
        Cursor cursor1=getContentResolver().query(Uri.parse(CONTENT_URI),null,"id=?",new String[]{idString},null);
        if(cursor1.moveToFirst()){
            do{
                firstid=cursor1.getInt(cursor1.getColumnIndex("firstid"));
                seconid=cursor1.getInt(cursor1.getColumnIndex("seconid"));
                thirdid=cursor1.getInt(cursor1.getColumnIndex("thirdid"));
                text=cursor1.getString(cursor1.getColumnIndex("text"));
                open=cursor1.getString(cursor1.getColumnIndex("open"));
            }while(cursor1.moveToNext());
        }
        cursor1.close();
        if(thirdid==0&&seconid!=0){
            Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,"firstid=? and seconid=?",new String[]{String.format("%d",firstid),String.format("%d",seconid)},null);
            if(cursor2.moveToFirst()){
                do{
                    int str1=cursor2.getInt(cursor2.getColumnIndex("id"));
                    int str2=cursor2.getInt(cursor2.getColumnIndex("firstid"));
                    int str3=cursor2.getInt(cursor2.getColumnIndex("seconid"));
                    int str4=cursor2.getInt(cursor2.getColumnIndex("thirdid"));
                    String str5=cursor2.getString(cursor2.getColumnIndex("text"));
                    String str6=cursor2.getString(cursor2.getColumnIndex("open"));
                    ContentValues values = new ContentValues();
                    values.put("firstid",str2);
                    values.put("seconid",str3);
                    values.put("thirdid",str4);
                    values.put("text",str5);
                    if(str4==0){
                        values.put("open","开");
                    }
                    if(str6.equals("开")&&str4!=0){
                        values.put("open","闭");
                    }
                    if(str6.equals("闭")&&str4!=0){
                        values.put("open","开");
                    }
                    getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{String.format("%d",str1)});
                    values.clear();
                }while(cursor2.moveToNext());
            }
            cursor2.close();
        }
        if(thirdid==0&&seconid==0){
            if(out){
                Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,"thirdid=?",new String[]{"0"},null);
                if(cursor2.moveToFirst()){
                    do{
                        int str1=cursor2.getInt(cursor2.getColumnIndex("id"));
                        int str2=cursor2.getInt(cursor2.getColumnIndex("firstid"));
                        int str3=cursor2.getInt(cursor2.getColumnIndex("seconid"));
                        int str4=cursor2.getInt(cursor2.getColumnIndex("thirdid"));
                        String str5=cursor2.getString(cursor2.getColumnIndex("text"));
                        String str6=cursor2.getString(cursor2.getColumnIndex("open"));
                        ContentValues values = new ContentValues();
                        values.put("firstid",str2);
                        values.put("seconid",str3);
                        values.put("thirdid",str4);
                        values.put("text",str5);
                        if(str2==firstid){
                            values.put("open","开");
                        }else{
                            values.put("open","闭");
                        }
                        String str1String=String.format("%d",str1);
                        getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{str1String});
                    }while(cursor2.moveToNext());
                }
                cursor2.close();
                out=false;
                inid=firstid;
            }else{
                Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,null,null,null);
                if(cursor2.moveToFirst()){
                    do{
                        int str1=cursor2.getInt(cursor2.getColumnIndex("id"));
                        int str2=cursor2.getInt(cursor2.getColumnIndex("firstid"));
                        int str3=cursor2.getInt(cursor2.getColumnIndex("seconid"));
                        int str4=cursor2.getInt(cursor2.getColumnIndex("thirdid"));
                        String str5=cursor2.getString(cursor2.getColumnIndex("text"));
                        String str6=cursor2.getString(cursor2.getColumnIndex("open"));
                        ContentValues values = new ContentValues();
                        values.put("firstid",str2);
                        values.put("seconid",str3);
                        values.put("thirdid",str4);
                        values.put("text",str5);
                        if(str3==0&&str4==0){
                            values.put("open","开");
                        }else{
                            values.put("open","闭");
                        }
                        String str1String=String.format("%d",str1);
                        getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{str1String});
                    }while(cursor2.moveToNext());
                }
                cursor2.close();
                out=true;
                inid=0;
            }
        }
        if(thirdid!=0){
            Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,"firstid=? and seconid=?",new String[]{String.format("%d",firstid),String.format("%d",seconid)},null);
            if(cursor2.moveToFirst()){
                do{int str1=cursor2.getInt(cursor2.getColumnIndex("id"));
                    int str2=cursor2.getInt(cursor2.getColumnIndex("firstid"));
                    int str3=cursor2.getInt(cursor2.getColumnIndex("seconid"));
                    int str4=cursor2.getInt(cursor2.getColumnIndex("thirdid"));
                    String str5=cursor2.getString(cursor2.getColumnIndex("text"));
                    String str6=cursor2.getString(cursor2.getColumnIndex("open"));
                    ContentValues values = new ContentValues();
                    values.put("firstid",str2);
                    values.put("seconid",str3);
                    if(str4>thirdid){
                        str4=str4+1;
                    }
                    values.put("thirdid",str4);
                    values.put("text",str5);
                    values.put("open",str6);
                    String str1String=String.format("%d",str1);
                    getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{str1String});
                    values.clear();
                }while(cursor2.moveToNext());
            }
            cursor2.close();
            ContentValues values = new ContentValues();
            values.put("firstid",firstid);
            values.put("seconid",seconid);
            values.put("thirdid",thirdid+1);
            values.put("text","");
            values.put("open",open);
            getContentResolver().insert(Uri.parse(CONTENT_URI),values);
            values.clear();
        }
    }
    public void insert(int a){
        if(inid==0){
            int maxfirstid=0;
            Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,null,null,null);
            if(cursor2.moveToFirst()){
                do{int str1=cursor2.getInt(cursor2.getColumnIndex("id"));
                    int str2=cursor2.getInt(cursor2.getColumnIndex("firstid"));
                    int str3=cursor2.getInt(cursor2.getColumnIndex("seconid"));
                    int str4=cursor2.getInt(cursor2.getColumnIndex("thirdid"));
                    String str5=cursor2.getString(cursor2.getColumnIndex("text"));
                    String str6=cursor2.getString(cursor2.getColumnIndex("open"));
                    if(str2>maxfirstid){
                        maxfirstid=str2;
                    }
                }while(cursor2.moveToNext());
            }
            cursor2.close();
            ContentValues values = new ContentValues();
            values.put("firstid",maxfirstid+1);
            values.put("seconid",0);
            values.put("thirdid",0);
            values.put("text","");
            values.put("open","开");
            getContentResolver().insert(Uri.parse(CONTENT_URI),values);
            values.clear();
        }
        if(inid!=0){
            int maxseconid=0;
            Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,"firstid=?",new String[]{String.format("%d",inid)},null);
            if(cursor2.moveToFirst()){
                do{
                    int str1=cursor2.getInt(cursor2.getColumnIndex("id"));
                    int str2=cursor2.getInt(cursor2.getColumnIndex("firstid"));
                    int str3=cursor2.getInt(cursor2.getColumnIndex("seconid"));
                    int str4=cursor2.getInt(cursor2.getColumnIndex("thirdid"));
                    String str5=cursor2.getString(cursor2.getColumnIndex("text"));
                    String str6=cursor2.getString(cursor2.getColumnIndex("open"));
                    if(str3>maxseconid){
                        maxseconid=str3;
                    }
                }while(cursor2.moveToNext());
            }
            cursor2.close();
            ContentValues values = new ContentValues();
            values.put("firstid",inid);
            values.put("seconid",maxseconid+1);
            values.put("thirdid",0);
            values.put("text","");
            values.put("open","开");
            getContentResolver().insert(Uri.parse(CONTENT_URI),values);
            values.clear();
            ContentValues values2 = new ContentValues();
            values2.put("firstid",inid);
            values2.put("seconid",maxseconid+1);
            values2.put("thirdid",1);
            values2.put("text","");
            values2.put("open","闭");
            getContentResolver().insert(Uri.parse(CONTENT_URI),values2);
            values2.clear();
        }
    }
    public void delete(int a){
        int firstid=0;
        int seconid=0;
        int thirdid=0;
        String text ="";
        String open ="";
        String idString=String.format("%d",a);
        Cursor cursor1=getContentResolver().query(Uri.parse(CONTENT_URI),null,"id=?",new String[]{idString},null);
        if(cursor1.moveToFirst()){
            do{
                firstid=cursor1.getInt(cursor1.getColumnIndex("firstid"));
                seconid=cursor1.getInt(cursor1.getColumnIndex("seconid"));
                thirdid=cursor1.getInt(cursor1.getColumnIndex("thirdid"));
                text=cursor1.getString(cursor1.getColumnIndex("text"));
                open=cursor1.getString(cursor1.getColumnIndex("open"));
            }while(cursor1.moveToNext());
        }
        cursor1.close();
        if(seconid!=0&&thirdid!=0){
            getContentResolver().delete(Uri.parse(CONTENT_URI),"id=?",new String[]{String.format("%d",a)});
            return;
        }
        if(seconid!=0&&thirdid==0){
            getContentResolver().delete(Uri.parse(CONTENT_URI),"seconid=?",new String[]{String.format("%d",seconid)});
            return;
        }
        if(seconid==0&&out){
            getContentResolver().delete(Uri.parse(CONTENT_URI),"firstid=?",new String[]{String.format("%d",firstid)});
            return;
        }
    }
    public void move(int a){
        int firstid=0;
        int seconid=0;
        int thirdid=0;
        String text ="";
        String open ="";
        String idString=String.format("%d",a);
        Cursor cursor1=getContentResolver().query(Uri.parse(CONTENT_URI),null,"id=?",new String[]{idString},null);
        if(cursor1.moveToFirst()){
            do{
                firstid=cursor1.getInt(cursor1.getColumnIndex("firstid"));
                seconid=cursor1.getInt(cursor1.getColumnIndex("seconid"));
                thirdid=cursor1.getInt(cursor1.getColumnIndex("thirdid"));
                text=cursor1.getString(cursor1.getColumnIndex("text"));
                open=cursor1.getString(cursor1.getColumnIndex("open"));
            }while(cursor1.moveToNext());
        }
        cursor1.close();
        if(thirdid==0&&seconid==0&&out){
            int maxsmallerfirstid=0;
            Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,null,null,null);
            if(cursor2.moveToFirst()){
                do{
                    int str2=cursor2.getInt(cursor2.getColumnIndex("firstid"));
                    if(str2>maxsmallerfirstid&&str2<firstid){
                        maxsmallerfirstid=str2;
                    }
                }while(cursor2.moveToNext());
            }
            cursor2.close();
            if(maxsmallerfirstid!=0){
                Cursor cursor3=getContentResolver().query(Uri.parse(CONTENT_URI),null,null,null,null);
                if(cursor3.moveToFirst()){
                    do{
                        ContentValues values = new ContentValues();
                        int str1=cursor3.getInt(cursor3.getColumnIndex("id"));
                        int str2=cursor3.getInt(cursor3.getColumnIndex("firstid"));
                        int str3=cursor3.getInt(cursor3.getColumnIndex("seconid"));
                        int str4=cursor3.getInt(cursor3.getColumnIndex("thirdid"));
                        String str5=cursor3.getString(cursor3.getColumnIndex("text"));
                        String str6=cursor3.getString(cursor3.getColumnIndex("open"));
                        if(str2==firstid){
                            values.put("firstid",maxsmallerfirstid);
                            values.put("seconid",str3);
                            values.put("thirdid",str4);
                            values.put("text",str5);
                            values.put("open",str6);
                            getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{String.format("%d",str1)});
                            values.clear();
                        }
                        if(str2==maxsmallerfirstid){
                            values.put("firstid",firstid);
                            values.put("seconid",str3);
                            values.put("thirdid",str4);
                            values.put("text",str5);
                            values.put("open",str6);
                            getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{String.format("%d",str1)});
                            values.clear();
                        }
                    }while(cursor3.moveToNext());
                }
                cursor3.close();
            }
        }
        if(thirdid==0&&seconid!=0){
            int maxsmallerseconid=0;
            Cursor cursor2=getContentResolver().query(Uri.parse(CONTENT_URI),null,"firstid=?",new String[]{String.format("%d",firstid)},null);
            if(cursor2.moveToFirst()){
                do{
                    int str3=cursor2.getInt(cursor2.getColumnIndex("seconid"));
                    if(str3>maxsmallerseconid&&str3<seconid){
                        maxsmallerseconid=str3;
                    }
                }while(cursor2.moveToNext());
            }
            cursor2.close();
            if(maxsmallerseconid!=0){
                Cursor cursor3=getContentResolver().query(Uri.parse(CONTENT_URI),null,"firstid=?",new String[]{String.format("%d",firstid)},null);
                if(cursor3.moveToFirst()){
                    do{
                        ContentValues values = new ContentValues();
                        int str1=cursor3.getInt(cursor3.getColumnIndex("id"));
                        int str2=cursor3.getInt(cursor3.getColumnIndex("firstid"));
                        int str3=cursor3.getInt(cursor3.getColumnIndex("seconid"));
                        int str4=cursor3.getInt(cursor3.getColumnIndex("thirdid"));
                        String str5=cursor3.getString(cursor3.getColumnIndex("text"));
                        String str6=cursor3.getString(cursor3.getColumnIndex("open"));
                        if(str3==seconid){
                            values.put("firstid",str2);
                            values.put("seconid",maxsmallerseconid);
                            values.put("thirdid",str4);
                            values.put("text",str5);
                            values.put("open",str6);
                            getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{String.format("%d",str1)});
                            values.clear();
                        }
                        if(str3==maxsmallerseconid){
                            values.put("firstid",str2);
                            values.put("seconid",seconid);
                            values.put("thirdid",str4);
                            values.put("text",str5);
                            values.put("open",str6);
                            getContentResolver().update(Uri.parse(CONTENT_URI),values,"id=?",new String[]{String.format("%d",str1)});
                            values.clear();
                        }
                    }while(cursor3.moveToNext());
                }
                cursor3.close();
            }
        }
        /*
        if(thirdid!=0){

        }
        */
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
        Color.colorPic = ContextCompat.getDrawable(FirstActivity.this,Color.getColorPicId(Color.color));
        Color.colorId = ContextCompat.getColor(FirstActivity.this,Color.getColorId(Color.color));
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitleMarginStart(72);
        toolbar.setTitleTextColor(Color.colorId);
        toolbar.setOverflowIcon(Color.colorPic);
        toolbar.setLogo(Color.colorPic);
    }




}
