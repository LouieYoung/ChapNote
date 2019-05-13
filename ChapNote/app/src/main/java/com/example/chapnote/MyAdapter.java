package com.example.chapnote;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.chapnote.MyApplication.getContext;

public class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Data> array;
    MyDatabase myDatabase;

    public MyAdapter(LayoutInflater inf,ArrayList<Data> arry){
        this.inflater=inf;
        this.array=arry;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {  //代码块中包含了对listview的效率优化
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=inflater.inflate(R.layout.listcontent_main,null);//加载listview子项
            vh.text=(TextView) convertView.findViewById(R.id.text);
            vh.time=(TextView) convertView.findViewById(R.id.time);
            vh.color=(Button) convertView.findViewById(R.id.color);
            vh.delete=(Button)convertView.findViewById(R.id.delete);
            vh.up=(Button)convertView.findViewById(R.id.up);
            convertView.setTag(vh);
        }
        vh=(ViewHolder) convertView.getTag();
        vh.text.setText(array.get(position).getText());
        vh.time.setText(array.get(position).getTime());
        vh.color.setBackground(ContextCompat.getDrawable(getContext(),Color.getColorPicId(array.get(position).getColor())));
        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.toDelete(array.get(position).getId());
            }
        });
        vh.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sort sort=new Sort();
                sort.toUp(array.get(position).getId());
            }
        });
        return convertView;
    }
    class ViewHolder{     //内部类，对控件进行缓存
        TextView text,time;
        Button color,delete,up;
    }
}
