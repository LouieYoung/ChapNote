package com.example.chapnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.chapnote.Color.getColorPicId;

public class MyAdapterFirst extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<Data> array;
    MyDatabase myDatabase;
    int first;

    public MyAdapterFirst(Context context, LayoutInflater inf, ArrayList<Data> arry,int fir){
        this.first=fir;
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
                toUp(array.get(position).getId(),array.get(position).getFirstid(),array.get(position).getSecondid());
                array.clear();
                array=myDatabase.getMore(first);
                notifyDataSetChanged();
            }
        });
       // vh.color.setBackgroundResource(getColorPicId(array.get(first).getColor()));

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
                if(arr.get(i).getThirdid()!=0){
                    toUp(arr.get(i).getId(),arr.get(i).getFirstid(),arr.get(i).getSecondid(),arr.get(i).getThirdid());
                }else if(arr.get(i).getSecondid()!=0){
                    toUp(arr.get(i).getId(),arr.get(i).getFirstid(),arr.get(i).getSecondid());
                }else{
                    return;
                }
            }
        }
        return;
    }

    public void toUp(int id,int firstid, int secondid){
        int maxsmallersecondid=secondid-1;
        if(maxsmallersecondid!=0){
            ArrayList<Data> arr=new ArrayList<Data>();
            myDatabase=new MyDatabase(mContext);
            arr=myDatabase.getarray();
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getFirstid()==firstid){
                    if(arr.get(i).getSecondid()==maxsmallersecondid){
                        Toast.makeText(mContext,"替换"+maxsmallersecondid,Toast.LENGTH_SHORT).show();
                        arr.get(i).setSecondid(secondid);
                        myDatabase.toUpdate(arr.get(i));
                    }else if(arr.get(i).getSecondid()==secondid){
                        arr.get(i).setSecondid(maxsmallersecondid);
                        myDatabase.toUpdate(arr.get(i));
                    }
                }
            }
        }else {
            return;
        }

    }
    public void toUp(int id,int firstid, int secondid,int thirdid){
        int maxsmallerthirdid=thirdid-1;
        if(maxsmallerthirdid!=0){
            ArrayList<Data> arr=new ArrayList<Data>();
            arr=myDatabase.getarray();
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getFirstid()==firstid&&arr.get(i).getSecondid()==secondid){
                    if(arr.get(i).getThirdid()==maxsmallerthirdid){
                        arr.get(i).setThirdid(thirdid);
                        myDatabase.toUpdate(arr.get(i));
                    }else if(arr.get(i).getThirdid()==thirdid){
                        arr.get(i).setThirdid(maxsmallerthirdid);
                        myDatabase.toUpdate(arr.get(i));
                    }
                }

            }
        }else {
            return;
        }
    }
}
