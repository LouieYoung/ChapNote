package com.example.chapnote;

import java.util.Comparator;

public class DataComparator implements Comparator<Data> {
    public int compare(Data a,Data b){
        if(a.getFirstid()>b.getFirstid())
            return 1;
        else if(a.getFirstid()<b.getFirstid())
            return -1;
        else if(a.getSecondid()>b.getSecondid())
            return 1;
        else if(a.getSecondid()<b.getSecondid())
            return -1;
        else if(a.getThirdid()>b.getThirdid())
            return 1;
        else if(a.getThirdid()<b.getThirdid())
            return -1;
        return 0;

    }
}
