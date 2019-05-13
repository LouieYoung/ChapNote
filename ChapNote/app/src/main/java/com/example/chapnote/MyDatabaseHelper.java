package com.example.chapnote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final String CREATE_NOTE="create table note("+
            "id integer primary key autoincrement," +
            "firstid integer,"+
            "seconid integer,"+
            "thirdid integer,"+
            "text text,"+
            "color text,"+
            "time text,"+
            "open text"+
            ")";
    public MyDatabaseHelper(Context context){
        super(context,"mydate",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_NOTE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists note");
        onCreate(db);
    }

    public long allCaseNum(SQLiteDatabase db){
        String sql = "select count(*) from note";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

}
