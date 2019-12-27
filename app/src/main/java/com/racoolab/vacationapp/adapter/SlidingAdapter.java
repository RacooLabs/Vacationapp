package com.racoolab.vacationapp.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.racoolab.vacationapp.R;
import com.racoolab.vacationapp.activity.OnItemClick;
import com.racoolab.vacationapp.datatype.SlidingVacationData;
import com.racoolab.vacationapp.datatype.StateData;
import com.racoolab.vacationapp.datatype.VacationData;

import java.util.List;

public class SlidingAdapter extends RecyclerView.Adapter<SlidingAdapter.MyViewHolder> {

    private List<SlidingVacationData> mDataset;
    static private View.OnClickListener onClickListener;



    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView TextView_subtitle;
        public TextView TextView_subtitle2;
        public TextView TextView_days;
        public View rootView;




        public MyViewHolder(View v,int viewType) {

            super(v);



            if(viewType == 0){

                TextView_subtitle = (TextView) v.findViewById(R.id.TextView_subtitle);
                TextView_days = (TextView) v.findViewById(R.id.TextView_days);
                v.setClickable(true);
                v.setEnabled(true);
                v.setOnClickListener(onClickListener);

                rootView = v;

            }else{
                TextView_subtitle2 = (TextView) v.findViewById(R.id.TextView_subtitle);
            }


        }

    }


    public SlidingAdapter(List<SlidingVacationData> mDataset, View.OnClickListener onClickListener) {

        this.mDataset = mDataset;
        this.onClickListener = onClickListener;


    }


    @Override
    public SlidingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view


        LinearLayout v;


        if(viewType == 0){
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sliding_adapter, parent, false);


        } else {

            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sliding_adapter_title, parent, false);

        }

        MyViewHolder vh = new MyViewHolder(v,viewType);
        return vh;

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if (!(mDataset.get(position).getDaysort().equals("수입 요약") || mDataset.get(position).getDaysort().equals("지출 요약"))){
            holder.TextView_subtitle.setText(mDataset.get(position).getDaysort());
            holder.rootView.setTag(mDataset.get(position).getNumber());
            holder.TextView_days.setText(mDataset.get(position).getDays()+"");
        } else if (mDataset.get(position).getDaysort().equals("수입 요약")){
            holder.TextView_subtitle2.setText("수입 요약");
            holder.TextView_subtitle2.setTextColor(Color.parseColor("#00613E"));
        } else if (mDataset.get(position).getDaysort().equals("지출 요약")){
            holder.TextView_subtitle2.setText("지출 요약");
            holder.TextView_subtitle2.setTextColor(Color.parseColor("#C33149"));
        }




    }

    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }




    public int getItemViewType(int position) {

        if (mDataset.get(position).getDaysort().equals("수입 요약") || mDataset.get(position).getDaysort().equals("지출 요약")) {
            return 1;
        } else {
            return 0;
        }


    }

}

