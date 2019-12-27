package com.racoolab.vacationapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.racoolab.vacationapp.R;
import com.racoolab.vacationapp.datatype.MonthData;

import java.util.List;

public class MainDayAdapter extends RecyclerView.Adapter<MainDayAdapter.MyViewHolder> {

    private List<MonthData> mDataset;
    private static View.OnClickListener onClickListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder {



        public TextView TextView_month1;
        public TextView TextView_year1;
        public TextView TextView_title1;
        public TextView TextView_days1;
        public View rootView;


        public MyViewHolder(View v) {
            super(v);

            TextView_month1 = v.findViewById(R.id.TextView_month1);
            TextView_year1 = v.findViewById(R.id.TextView_year1);
            TextView_title1 = v.findViewById(R.id.TextView_title1);
            TextView_days1 = v.findViewById(R.id.TextView_days1);

            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);


        }
    }


    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).isState() == true ? 0 : 1 ;
    }


    public MainDayAdapter(List<MonthData> arrayMonthData, View.OnClickListener onClick){
        mDataset = arrayMonthData;
        onClickListener = onClick;
    }


    @Override
    public MainDayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view

        LinearLayout v;

        if(viewType == 0){
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_days_positive, parent, false);
        } else {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_days_negative, parent, false);
        }


        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.TextView_year1.setText(Integer.toString(mDataset.get(position).getYear()));
        holder.TextView_month1.setText(Integer.toString(mDataset.get(position).getMonth()));

        int subtotal = 0;

        for(int i=0;i<mDataset.get(position).getArrayvacationdata().size();i++){
            subtotal += mDataset.get(position).getArrayvacationdata().get(i).getDays();
        }


        if(mDataset.get(position).isState()){
            holder.TextView_days1.setText("+"+Integer.toString(subtotal)+"일");

        } else {
            holder.TextView_days1.setText("-"+Integer.toString(subtotal)+"일");
        }

        holder.TextView_title1.setText(mDataset.get(position).getTitle());

        holder.rootView.setTag(position);


    }

    @Override
    public int getItemCount() {
        return mDataset != null  ?  mDataset.size() : 0;
    }

}