package com.racoolab.vacationapp.datatype;

import java.io.Serializable;

public class SlidingVacationData implements Serializable {




    private int number;

    private String daysort = "";

    private int days = 0;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDaysort() {
        return daysort;
    }

    public void setDaysort(String daysort) {  this.daysort = daysort;   }

    public int getDays() {  return days;  }

    public void setDays(int days) {

        if(days<0||days>500){

            this.days = 0;
        } else {
            this.days = days;
        }

    }




}
