package com.example.chapnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;

public class MyDatabase {
    Context context;
    MyDatabaseHelper myDatabaseHelper;
    SQLiteDatabase mydatebase;
    public MyDatabase(Context context){
        this.context = context;
        myDatabaseHelper =new MyDatabaseHelper(context);
    }

    public ArrayList<Data> getarray(){
        ArrayList<Data> arr = new ArrayList<Data>();
        mydatebase = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = mydatebase.rawQuery("select id,firstid,secondid,thirdid,text,color,time,open from note",null);
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
            mydatebase.close();
            arr.add(data);
            cursor.moveToNext();
        }
        mydatebase.close();
        return arr;
    }

    public void toUpdate(Data data){
        mydatebase = myDatabaseHelper.getWritableDatabase();
        mydatebase.execSQL(
                "update note set firstid='" +data.getFirstid()+
                        "'secondid'"+data.getSecondid()+
                        "'thirdid'"+data.getThirdid()+
                        "',text='"+data.getText()+
                        "',color='"+data.getColor()+
                        "',time='"+data.getTime()+
                        "',open='"+data.getOpen()+
                        "'where id='"+data.getId()+"'");
        mydatebase.close();
    }
    public void toInsert(Data data){
        mydatebase = myDatabaseHelper.getWritableDatabase();
        mydatebase.execSQL(
                "insert into note(firstid,secondid,thirdid,text,color,time,open)values('"
                        +data.getFirstid()+"','"
                        +data.getSecondid()+"','"
                        +data.getThirdid()+"','"
                        +data.getText()+"','"
                        +data.getColor()+"','"
                        +data.getTime()+"','"
                        +data.getOpen()+"')");
                mydatebase.close();
    }
    public void toDelete(int id){
        mydatebase = myDatabaseHelper.getWritableDatabase();
        mydatebase.execSQL("delete from note where id="+id+"");
        mydatebase.close();
    }

}
