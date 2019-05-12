package com.example.lenovo.work999;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

public class MyCP extends ContentProvider {
    private MyDatabaseHelper dbHelper;
    public MyCP() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("Book",selection,selectionArgs);
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.insert("Book",null,values);
        values.clear();
        return null;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper=new MyDatabaseHelper(getContext(),"BookStore.db",null,1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("Book",null,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.update("Book",values,selection,selectionArgs);
        values.clear();
        return 0;
    }
    public class MyDatabaseHelper extends SQLiteOpenHelper {
        public static final String CREATE_BOOK="create table Book(id integer primary key autoincrement," +
                "firstid integer," +
                "seconid integer," +
                "thirdid integer," +
                "text text," +
                "open text)";
        private Context mContext;
        public MyDatabaseHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version){
            super(context,name,factory,version);
            mContext=context;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_BOOK);
            Toast.makeText(mContext,"数据已保存",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
