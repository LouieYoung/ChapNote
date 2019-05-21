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

public class MyAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<Data> array;
    MyDatabase myDatabase;

    public MyAdapter(Context context,LayoutInflater inf,ArrayList<Data> arry){
        this.mContext=context;
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
            vh.up=(Button)convertView.findViewById(R.id.up);
            convertView.setTag(vh);
        }
        vh=(ViewHolder) convertView.getTag();
        vh.text.setText(array.get(position).getText());
        vh.time.setText(array.get(position).getTime());
        vh.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUp(array.get(position).getId());
                array.clear();
                array=myDatabase.getFirst();
                notifyDataSetChanged();
            }
        });
        vh.color.setBackgroundResource(getColorPicId(array.get(position).getColor()));

        return convertView;
    }
    class ViewHolder{     //内部类，对控件进行缓存
        TextView text,time;
        Button color,up;
    }
    public void toUp(int id){
        ArrayList<Data> arr=new ArrayList<Data>();
        myDatabase=new MyDatabase(mContext);
        arr=myDatabase.getarray();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getId()==id){
                if(arr.get(i).getFirstid()>0){
                    toUp(arr.get(i).getId(),arr.get(i).getFirstid());
                }else{
                    return;
                }
            }
        }
        return;
    }
    public void toUp(int id,int firstid){
        ArrayList<Data> arr=new ArrayList<Data>();
        arr=myDatabase.getarray();
        int maxsmallerfirstid=0;
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getFirstid()<firstid){
                maxsmallerfirstid=arr.get(i).getFirstid();
            }else if(arr.get(i).getFirstid()==firstid){
                break;
            }
        }
        if(maxsmallerfirstid>0){
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getFirstid()==maxsmallerfirstid){
                    arr.get(i).setFirstid(firstid);
                    myDatabase.toUpdate(arr.get(i));
                }else if(arr.get(i).getFirstid()==firstid){
                    arr.get(i).setFirstid(maxsmallerfirstid);
                    myDatabase.toUpdate(arr.get(i));
                }
            }
        }else {
            return;
        }
    }


}
