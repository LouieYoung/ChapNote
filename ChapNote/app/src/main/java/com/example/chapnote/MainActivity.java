package com.example.chapnote;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);

        String color = "black";
        int colorPicId = Color.getColorPicId(color);
        int colorId = getResources().getColor(Color.getColorId(color));

        //toolbar部分
        toolbar.setTitle("小书笔记"); //设置标题
        toolbar.setTitleMarginStart(72);
        toolbar.setTitleTextColor(colorId);
        toolbar.setLogo(colorPicId); //设置Logo
        setSupportActionBar(toolbar); //这里注意为固定写法
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) //得到被点击的item的itemId
                {
                    case R.id.search:
                        Toast.makeText(MainActivity.this, "搜索",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.black:
                        Toast.makeText(MainActivity.this, "全部",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.red:
                        Toast.makeText(MainActivity.this, "红色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.yellow:
                        Toast.makeText(MainActivity.this, "黄色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.purple:
                        Toast.makeText(MainActivity.this, "紫色",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.green:
                        Toast.makeText(MainActivity.this, "绿色",Toast.LENGTH_SHORT).show();
                        break;
                }
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
