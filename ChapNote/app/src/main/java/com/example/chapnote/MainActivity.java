package com.example.chapnote;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    Func f= new Func();

    int black = getResources().getColor(R.color.black);
    int back = getResources().getColor(R.color.back);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
