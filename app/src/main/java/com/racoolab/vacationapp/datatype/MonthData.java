package com.racoolab.vacationapp.datatype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class MonthData implements Serializable {


    private boolean state;


    private int year;


    private int month;



    private String title = "-";


    private List<VacationData> arrayvacationdata;


    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<VacationData> getArrayvacationdata() {
        return arrayvacationdata;
    }

    public void setArrayvacationdata(List<VacationData> arrayvacationdata) {
        this.arrayvacationdata = arrayvacationdata;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
