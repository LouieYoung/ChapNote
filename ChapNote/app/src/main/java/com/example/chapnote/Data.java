package com.example.chapnote;

public class Data{
    private int id;
    private int firstid;
    private int secondid;
    private int thirdid;
    private String text;
    private String color;
    private String time;
    private String open;

    public Data(int id,int firstid,int secondid,int thirdid,String text,String color,String time,String open){
        this.id=id;
        this.firstid=firstid;
        this.secondid=secondid;
        this.thirdid=thirdid;
        this.text=text;
        this.color=color;
        this.time=time;
        this.open=open;
    }
    public void setFirstid(int firstid){

        this.firstid=firstid;

    }
    public void setSecondid(int secondid){

        this.secondid=secondid;

    }
    public void setThirdid(int thirdid){

        this.thirdid=thirdid;

    }
    public void setOpen(String open){

        this.open=open;

    }
    public int getId(){

        return id;

    }
    public int getFirstid(){

        return firstid;

    }
    public int getSecondid(){

        return secondid;

    }
    public int getThirdid(){

        return thirdid;

    }
    public String getText(){

        return text;

    }
    public String getColor(){

        return color;

    }
    public String getTime(){

        return time;

    }
    public String getOpen(){

        return open;

    }



}
