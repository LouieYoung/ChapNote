package com.example.chapnote;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {
    Func f= new Func();

//    int black = getResources().getColor(R.color.black);
//    int back = getResources().getColor(R.color.back);
//    int green = getResources().getColor(R.color.green);
//    int purple = getResources().getColor(R.color.purple);
//    int yellow = getResources().getColor(R.color.yellow);
//    int red = getResources().getColor(R.color.red);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);

        toolbar.setTitle("小书笔记"); //设置标题
        toolbar.setLogo(R.mipmap.ic_launcher_round); //设置Logo
        setSupportActionBar(toolbar); //这里注意为固定写法
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // do something...
                return true;
            }
        });

    }
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
}
