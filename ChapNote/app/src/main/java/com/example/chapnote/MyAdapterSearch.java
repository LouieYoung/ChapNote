package com.example.chapnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.chapnote.Color.getColorPicId;

public class MyAdapterSearch extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<Data> array;

    public MyAdapterSearch(Context context, LayoutInflater inf, ArrayList<Data> array){
        this.mContext=context;
        this.inflater=inf;
        this.array=array;
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
            convertView=inflater.inflate(R.layout.listcontent_search,null);//加载listview子项
            vh.text=(TextView) convertView.findViewById(R.id.text);
            vh.blank=(TextView) convertView.findViewById(R.id.blank);
            vh.color=(Button) convertView.findViewById(R.id.color);
            convertView.setTag(vh);
        }
        vh=(ViewHolder) convertView.getTag();
        vh.text.setText(array.get(position).getText());
        if(array.get(position).getThirdid()!=0){
            vh.blank.setBackgroundResource(getColorPicId("blank"));
            vh.color.setBackgroundResource(getColorPicId("third_"+array.get(position).getColor()));
        }else if(array.get(position).getSecondid()!=0){
            vh.blank.setBackgroundResource(getColorPicId("blank_second"));
            vh.color.setBackgroundResource(getColorPicId("second_"+array.get(position).getColor()));
        }else{
            vh.color.setBackgroundResource(getColorPicId(array.get(position).getColor()));
        }
        return convertView;
    }
    class ViewHolder{     //内部类，对控件进行缓存
        TextView text,blank;
        Button color;
    }

}
