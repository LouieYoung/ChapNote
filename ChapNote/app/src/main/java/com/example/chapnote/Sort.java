package com.example.chapnote;


import java.util.ArrayList;

public class Sort extends Func {
    MyDatabase myDatabase = new MyDatabase(this);
    public void toUp(int id){
        ArrayList<Data> arr=new ArrayList<Data>();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).getId()==id){
                if(arr.get(i).getThirdid()!=0){
                    toUp(arr.get(i).getId(),arr.get(i).getFirstid(),arr.get(i).getSecondid(),arr.get(i).getThirdid());
                }else if(arr.get(i).getSecondid()!=0){
                    toUp(arr.get(i).getId(),arr.get(i).getFirstid(),arr.get(i).getSecondid());
                }else if(arr.get(i).getFirstid()!=0){
                    toUp(arr.get(i).getId(),arr.get(i).getFirstid());
                }else{
                    return;
                }
            }
        }
        return;
    }
    public void toUp(int id,int firstid){
        int maxsmallerfirstid=firstid-1;
        if(maxsmallerfirstid!=0){
            ArrayList<Data> arr=new ArrayList<Data>();
            arr=myDatabase.getarray();
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

    public void toUp(int id,int firstid, int secondid){
        int maxsmallersecondid=secondid-1;
        if(maxsmallersecondid!=0){
            ArrayList<Data> arr=new ArrayList<Data>();
            arr=myDatabase.getarray();
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getThirdid()==maxsmallersecondid){
                    arr.get(i).setFirstid(secondid);
                    myDatabase.toUpdate(arr.get(i));
                }else if(arr.get(i).getFirstid()==secondid){
                    arr.get(i).setFirstid(maxsmallersecondid);
                    myDatabase.toUpdate(arr.get(i));
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
                if(arr.get(i).getThirdid()==maxsmallerthirdid){
                    arr.get(i).setFirstid(thirdid);
                    myDatabase.toUpdate(arr.get(i));
                }else if(arr.get(i).getFirstid()==thirdid){
                    arr.get(i).setFirstid(maxsmallerthirdid);
                    myDatabase.toUpdate(arr.get(i));
                }
            }
        }else {
            return;
        }

    }

}
