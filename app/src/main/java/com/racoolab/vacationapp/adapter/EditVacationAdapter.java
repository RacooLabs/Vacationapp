package com.racoolab.vacationapp.adapter;

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
import com.racoolab.vacationapp.datatype.StateData;
import com.racoolab.vacationapp.datatype.VacationData;

import java.util.List;

public class EditVacationAdapter extends RecyclerView.Adapter<EditVacationAdapter.MyViewHolder> {

    private List<VacationData> mDataset;
    private View View_button;
    private View.OnClickListener onClickListener;
    private OnItemClick mCallback;
    private StateData statedata;





    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView close_icon;
        public ImageView close_iconinner;
        public EditText EditText_subtitle;
        public EditText EditText_day;
        public MyCustomEditTextListener1 myCustomEditTextListener1;
        public MyCustomEditTextListener2 myCustomEditTextListener2;
        public TextView TextView_subtitle;
        public TextView TextView_day;
        public TextView TextView_daykorean;


        public MyViewHolder(View v, MyCustomEditTextListener1 myCustomEditTextListener1,
                            MyCustomEditTextListener2 myCustomEditTextListener2,int viewType) {

            super(v);

            if(viewType != 0){

                close_icon = (TextView) v.findViewById(R.id.close_icon);
                close_iconinner = (ImageView) v.findViewById(R.id.close_iconinner);
                EditText_subtitle = (EditText) v.findViewById(R.id.EditText_subtitle);
                EditText_day = (EditText) v.findViewById(R.id.EditText_day);

                this.myCustomEditTextListener1 = myCustomEditTextListener1;
                EditText_subtitle.addTextChangedListener(myCustomEditTextListener1);

                this.myCustomEditTextListener2 = myCustomEditTextListener2;
                EditText_day.addTextChangedListener(myCustomEditTextListener2);

                TextView_subtitle = v.findViewById(R.id.TextView_subtitle);
                TextView_day = v.findViewById(R.id.TextView_day);
                TextView_daykorean = v.findViewById(R.id.TextView_daykorean);

            }

        }

    }


    public EditVacationAdapter(List<VacationData> arrayVacationData, View.OnClickListener onClickListener, OnItemClick listener,StateData statedata) {
        mDataset = arrayVacationData;
        this.onClickListener = onClickListener;
        this.mCallback = listener;
        this.statedata = statedata;
        //데이터 리셋할때 이건 작동을 안하
    }


    @Override
    public EditVacationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
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
                    .inflate(R.layout.adapter_vacation_data_edit, parent, false);
        }


        MyViewHolder vh = new MyViewHolder(v, new MyCustomEditTextListener1(),new MyCustomEditTextListener2(),viewType);
        return vh;

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if(statedata.State) {//어레이의 첫칸이 공백이란건 그건 편집모드라는 것임.


            if (position < mDataset.size() - 1) {

                holder.close_icon.setVisibility(View.VISIBLE);
                holder.close_iconinner.setVisibility(View.VISIBLE);

                holder.TextView_subtitle.setVisibility(View.INVISIBLE);
                holder.TextView_day.setVisibility((View.INVISIBLE));

                holder.EditText_subtitle.setVisibility((View.VISIBLE));
                holder.EditText_day.setVisibility((View.VISIBLE));

                holder.TextView_daykorean.setVisibility(View.VISIBLE);



                holder.myCustomEditTextListener1.updatePosition(holder.getAdapterPosition() + 1);
                holder.myCustomEditTextListener2.updatePosition(holder.getAdapterPosition() + 1);

                holder.EditText_subtitle.setText(mDataset.get(position + 1).getDaysort());


                if (mDataset.get(position + 1).getDays() == 0) {
                    holder.EditText_day.setText("");
                } else {
                    holder.EditText_day.setText(String.valueOf(mDataset.get(position + 1).getDays()));
                }



                holder.close_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(position<mDataset.size()-1){
                            mDataset.remove(position+1);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mDataset.size());
                            resetsubtotal();
                        }

                    }
                });


            }


        } else {

            holder.close_icon.setVisibility(View.GONE);
            holder.close_iconinner.setVisibility(View.GONE);
            holder.TextView_subtitle.setVisibility(View.VISIBLE);
            holder.TextView_day.setVisibility((View.VISIBLE));

            holder.EditText_subtitle.setVisibility((View.INVISIBLE));
            holder.EditText_day.setVisibility((View.INVISIBLE));

            holder.TextView_subtitle.setText(mDataset.get(position).getDaysort());
            holder.TextView_daykorean.setVisibility(View.INVISIBLE);
            holder.TextView_day.setText(mDataset.get(position).getDays()+" 일");
            resetsubtotal();

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

        if(!statedata.State){
            return 1;
        } else {
            return (position == mDataset.size() - 1) ? 0 : 1;
        }
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


//            charSequence
//            if(charSequence.length() == 0 && mDataset.get(position).getDaysort().length()>0){
//
//            } else if (charSequence.length() > 0){
//
//            } else if (charSequence.length() == 0){
//                mDataset.get(position).setDaysort("");
//            }



//            if(charSequence.length() == 0 && mDataset.get(position+1).getDaysort().length()>0){
//
//            } else if(charSequence.length() > 0){
//
//
//            } else if (charSequence.length() == 0 ) {
//                mDataset.get(position+1).setDaysort("");
//            }
//            if(charSequence.length()==0 && mDataset.get(position).)


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



            if(charSequence.length() > 0){
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

