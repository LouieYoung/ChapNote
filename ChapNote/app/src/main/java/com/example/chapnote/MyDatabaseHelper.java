package com.example.chapnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final String CREATE_NOTE="create table note("+
            "id integer primary key,"+
            "firstid integer,"+
            "seconid integer,"+
            "thirdid integer,"+
            "text text,"+
            "color text,"+
            "time text,"+
            "open integer"+
            ")";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_NOTE);
        db.execSQL("");
        Toast.makeText(mContext,"数据已保存",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists student");
        onCreate(db);
    }
}
