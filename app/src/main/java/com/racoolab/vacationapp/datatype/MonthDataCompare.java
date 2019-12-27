package com.racoolab.vacationapp.datatype;

import java.util.Comparator;

public class MonthDataCompare implements Comparator<MonthData> {

    int ret = 0;

    @Override
    public int compare(MonthData s1, MonthData s2) {


        if (s1.getYear() < s2.getYear()) {
            ret = 1;
        }
        if (s1.getYear() == s2.getYear()) {
            if (s1.getMonth() == s2.getMonth()) {
                if (s1.isState() == false && s2.isState() == true) {
                    ret = 1;
                } else if (s1.isState() == s2.isState()) {
                    ret = 0;
                } else if (s1.isState() == true && s2.isState() == false) {
                    ret = -1;
                }
            } else if (s1.getMonth() < s2.getMonth()) {
                ret = 1;
            } else if (s1.getMonth() > s2.getMonth()) {
                ret = -1;
            }
        }
        if (s1.getYear() > s2.getYear()) {
            ret = -1;
        }
        return ret;

    }

}
