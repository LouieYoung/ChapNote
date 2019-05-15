package com.example.chapnote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;


public class MyDatabase {
    Context context;
    MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    public MyDatabase(Context context){
        this.context = context;
        dbHelper =new MyDatabaseHelper(context);
    }

    public ArrayList<Data> getarray(){
        ArrayList<Data> arr = new ArrayList<Data>();
        dbHelper =new MyDatabaseHelper(context);
        db= dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,firstid,secondid,thirdid,text,color,time,open from note",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int firstid = cursor.getInt(cursor.getColumnIndex("firstid"));
            int secondid = cursor.getInt(cursor.getColumnIndex("secondid"));
            int thirdid = cursor.getInt(cursor.getColumnIndex("thirdid"));
            String text = cursor.getString(cursor.getColumnIndex("text"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String open = cursor.getString(cursor.getColumnIndex("open"));
            Data data = new Data(id,firstid,secondid,thirdid,text,color,time,open);
            db.close();

            arr.add(data);
            cursor.moveToNext();
        };
        db.close();
        Collections.sort(arr,new DataComparator());//按firstid、secondid、thirdid依次排序
        return arr;
    }
    public ArrayList<Data> getFirst(){
        ArrayList<Data> arr = new ArrayList<Data>();
        dbHelper =new MyDatabaseHelper(context);
        db= dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,firstid,secondid,thirdid,text,color,time,open from note",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int firstid = cursor.getInt(cursor.getColumnIndex("firstid"));
            int secondid = cursor.getInt(cursor.getColumnIndex("secondid"));
            int thirdid = cursor.getInt(cursor.getColumnIndex("thirdid"));
            String text = cursor.getString(cursor.getColumnIndex("text"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String open = cursor.getString(cursor.getColumnIndex("open"));
            Data data = new Data(id,firstid,secondid,thirdid,text,color,time,open);
            db.close();
            if(firstid!=0&&secondid==0&&thirdid==0){
                arr.add(data);
            }
            cursor.moveToNext();
        };
        db.close();
        Collections.sort(arr,new DataComparator());
        return arr;
    }
    public ArrayList<Data> getMore(int first){
        ArrayList<Data> arr = new ArrayList<Data>();
        dbHelper =new MyDatabaseHelper(context);
        db= dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,firstid,secondid,thirdid,text,color,time,open from note",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int firstid = cursor.getInt(cursor.getColumnIndex("firstid"));
            int secondid = cursor.getInt(cursor.getColumnIndex("secondid"));
            int thirdid = cursor.getInt(cursor.getColumnIndex("thirdid"));
            String text = cursor.getString(cursor.getColumnIndex("text"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String open = cursor.getString(cursor.getColumnIndex("open"));
            Data data = new Data(id,firstid,secondid,thirdid,text,color,time,open);
            db.close();
            if(firstid==first){
                arr.add(data);
            }
            cursor.moveToNext();
        };
        db.close();
        Collections.sort(arr,new DataComparator());
        return arr;
    }
    public ArrayList<Data> getOpen(int first){
        ArrayList<Data> arr = new ArrayList<Data>();
        dbHelper =new MyDatabaseHelper(context);
        db= dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,firstid,secondid,thirdid,text,color,time,open from note",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int firstid = cursor.getInt(cursor.getColumnIndex("firstid"));
            int secondid = cursor.getInt(cursor.getColumnIndex("secondid"));
            int thirdid = cursor.getInt(cursor.getColumnIndex("thirdid"));
            String text = cursor.getString(cursor.getColumnIndex("text"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String open = cursor.getString(cursor.getColumnIndex("open"));
            Data data = new Data(id,firstid,secondid,thirdid,text,color,time,open);
            db.close();
            if(firstid==first){
                if(thirdid!=0&&open.equals("开")){
                    arr.add(data);
                }else if(thirdid!=0&&open.equals("闭")){

                }
                else{
                    arr.add(data);
                }
            }
            cursor.moveToNext();
        };
        db.close();
        Collections.sort(arr,new DataComparator());
        return arr;
    }
    public Data getDataFromId(int alforId){
        Data a=null;
        dbHelper =new MyDatabaseHelper(context);
        db= dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,firstid,secondid,thirdid,text,color,time,open from note",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            if(id==alforId){

                int firstid = cursor.getInt(cursor.getColumnIndex("firstid"));
                int secondid = cursor.getInt(cursor.getColumnIndex("secondid"));
                int thirdid = cursor.getInt(cursor.getColumnIndex("thirdid"));
                String text = cursor.getString(cursor.getColumnIndex("text"));
                String color = cursor.getString(cursor.getColumnIndex("color"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String open = cursor.getString(cursor.getColumnIndex("open"));
                Data data = new Data(id,firstid,secondid,thirdid,text,color,time,open);
                a=data;
                break;
            }
            cursor.moveToNext();
        };
        db.close();
        return a;
    }
    public void toUpdate(Data data){
        db = dbHelper.getWritableDatabase();
        db.execSQL(
                "update note set firstid='" +data.getFirstid()+
                        "',secondid='"+data.getSecondid()+
                        "',thirdid='"+data.getThirdid()+
                        "',text='"+data.getText()+
                        "',color='"+data.getColor()+
                        "',time='"+data.getTime()+
                        "',open='"+data.getOpen()+
                        "'where id='"+data.getId()+"'");
        db.close();
    }
    public void toInsert(Data data){
        db = dbHelper.getWritableDatabase();
        db.execSQL(
                "insert into note(firstid,secondid,thirdid,text,color,time,open)values('"
                        +data.getFirstid()+"','"
                        +data.getSecondid()+"','"
                        +data.getThirdid()+"','"
                        +data.getText()+"','"
                        +data.getColor()+"','"
                        +data.getTime()+"','"
                        +data.getOpen()+"')");
                db.close();
    }
    public void toDelete(int id){
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from note where id="+id+"");
        db.close();
    }

}
