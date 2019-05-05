package com.example.chapnote;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    Func f= new Func();

    int black = getResources().getColor(R.color.black);
    int back = getResources().getColor(R.color.back);
    int green = getResources().getColor(R.color.green);
    int purple = getResources().getColor(R.color.purple);
    int yellow = getResources().getColor(R.color.yellow);
    int red = getResources().getColor(R.color.red);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
