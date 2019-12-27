package com.racoolab.vacationapp.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.racoolab.vacationapp.R;
import com.racoolab.vacationapp.activity.OnItemClick;
import com.racoolab.vacationapp.datatype.VacationData;

import java.util.List;

public class MinusVacationDataAdapter extends RecyclerView.Adapter<MinusVacationDataAdapter.MyViewHolder> {

    private List<VacationData> mDataset;
    private View View_button;
    private View.OnClickListener onClickListener;
    private OnItemClick mCallback;




    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public View close_icon;
        public EditText EditText_subtitle;
        public EditText EditText_day;
        public MyCustomEditTextListener1 myCustomEditTextListener1;
        public MyCustomEditTextListener2 myCustomEditTextListener2;


        public MyViewHolder(View v, MyCustomEditTextListener1 myCustomEditTextListener1,
                            MyCustomEditTextListener2 myCustomEditTextListener2,int viewType) {

            super(v);

            if(viewType != 0){

                close_icon = (View) v.findViewById(R.id.close_icon);
                EditText_subtitle = (EditText) v.findViewById(R.id.EditText_subtitle);
                EditText_day = (EditText) v.findViewById(R.id.EditText_day);

                this.myCustomEditTextListener1 = myCustomEditTextListener1;
                EditText_subtitle.addTextChangedListener(myCustomEditTextListener1);

                this.myCustomEditTextListener2 = myCustomEditTextListener2;
                EditText_day.addTextChangedListener(myCustomEditTextListener2);

            }

        }

    }


    public MinusVacationDataAdapter(List<VacationData> arrayVacationData, View.OnClickListener onClickListener, OnItemClick listener) {
        mDataset = arrayVacationData;
        this.onClickListener = onClickListener;
        this.mCallback = listener;
    }


    @Override
    public MinusVacationDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view

        LinearLayout v;

        if(viewType == 0){
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_vacation_data_plus, parent, false);

            View_button = v.findViewById(R.id.View_button);
            View_button.setOnClickListener(onClickListener);

        } else {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_vacation_data, parent, false);
        }


        MyViewHolder vh = new MyViewHolder(v, new MyCustomEditTextListener1(),new MyCustomEditTextListener2(),viewType);
        return vh;

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if(position<mDataset.size()-1){


            holder.myCustomEditTextListener1.updatePosition(holder.getAdapterPosition()+1);

            holder.myCustomEditTextListener2.updatePosition(holder.getAdapterPosition()+1);

            holder.EditText_subtitle.setText(mDataset.get(position+1).getDaysort());


            if(mDataset.get(position+1).getDays() == 0){
                holder.EditText_day.setText("");
            } else {
                holder.EditText_day.setText(String.valueOf(mDataset.get(position+1).getDays()));
            }




            holder.close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(position<mDataset.size()-1){
                        mDataset.remove(position+1);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mDataset.size());
                        resetsubtotal();
                    }



                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    public void addVacationData(){
        mDataset.add(new VacationData());
        notifyItemInserted(mDataset.size()-1);


    }

    public int getItemViewType(int position){
        return (position == mDataset.size()-1) ? 0 : 1;
    }


    private class MyCustomEditTextListener1 implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            mDataset.get(position).setDaysort(charSequence.toString());

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class MyCustomEditTextListener2 implements TextWatcher {

        private int position;


        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            if(charSequence.toString().length() != 0){
                mDataset.get(position).setDays(Integer.parseInt(charSequence.toString()));

                resetsubtotal();

            } else {
                mDataset.get(position).setDays(0);

                resetsubtotal();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    public List<VacationData> getVacationData(){

        if(mDataset.size()>0){

            return mDataset;

        } else {

            return null;

        }
    }

    public void resetsubtotal(){

        int subtotal = 0;

        for(int j = 0;j<mDataset.size();j++){
            subtotal += mDataset.get(j).getDays();
            mCallback.onClick(subtotal);}

    }

}

