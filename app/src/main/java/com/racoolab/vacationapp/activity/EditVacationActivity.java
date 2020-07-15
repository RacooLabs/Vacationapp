package com.racoolab.vacationapp.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.racoolab.vacationapp.R;
import com.racoolab.vacationapp.adapter.EditVacationAdapter;
import com.racoolab.vacationapp.datatype.MonthData;
import com.racoolab.vacationapp.datatype.StateData;
import com.racoolab.vacationapp.datatype.VacationData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditVacationActivity extends AppCompatActivity implements OnItemClick {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<VacationData> arrayVacationData;

    private boolean editState = false;

    NumberPicker nPicker_year;
    NumberPicker nPicker_month;
    EditText EditText_title;

    public AdView mAdView;



    private TextView TextView_addButton;
    private TextView TextView_TextView_subTotalvalue;
    private TextView TextView_removeButton;

    private TextView TextView_yearText;
    private TextView TextView_monthText;
    private TextView TextView_editButton;
    private TextView TextView_saveButton;

    private MonthData monthData;
    private StateData statedata = new StateData();
    private int position;






    @Override
    public void onClick (int subtotal){
        TextView_TextView_subTotalvalue.setText("합계 " + subtotal);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vacation_data_edit);

        MobileAds.initialize(this, "ca-app-pub-7972968096388401~4035110400");
        mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        arrayVacationData = new ArrayList<>(); // 넘길 배열
        monthData = new MonthData();

        setBind();
        getDataDetails();
        setNpicker_settitle_setText();
        setclickButton();

        setRecyclerView();



    }



    public void setBind(){

        nPicker_year = (NumberPicker) findViewById(R.id.npicker_year);
        nPicker_month = (NumberPicker) findViewById(R.id.npicker_month);
        TextView_addButton = (TextView) findViewById(R.id.TextView_addButton);
        TextView_TextView_subTotalvalue = (TextView) findViewById(R.id.TextView_subTotalvalue);
        EditText_title = (EditText) findViewById((R.id.EditText_title));
        TextView_removeButton = (TextView) findViewById(R.id.TextView_removeButton);


        TextView_yearText = (TextView) findViewById(R.id.TextView_yearText);
        TextView_monthText = (TextView) findViewById(R.id.TextView_monthText);
        TextView_editButton = (TextView) findViewById(R.id.TextView_editButton);
        TextView_saveButton = (TextView) findViewById(R.id.TextView_saveButton);


    }

    public void setNpicker_settitle_setText(){


        nPicker_year.setMinValue(monthData.getYear()-1);
        nPicker_year.setMaxValue(monthData.getYear()+1);
        nPicker_month.setMinValue(1);
        nPicker_month.setMaxValue(12);

        TextView_yearText.setText(Integer.toString(monthData.getYear()));
        TextView_monthText.setText(Integer.toString(monthData.getMonth()));

        nPicker_year.setValue(Integer.parseInt(TextView_yearText.getText().toString()));
        nPicker_month.setValue(Integer.parseInt(TextView_monthText.getText().toString()));

        EditText_title.setText(monthData.getTitle());


    }

    public void setRecyclerView(){

        recyclerView = (RecyclerView) findViewById(R.id.addDayrecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)



        mAdapter = new EditVacationAdapter(arrayVacationData, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((EditVacationAdapter)mAdapter).addVacationData();
                recyclerView.smoothScrollToPosition(arrayVacationData.size()-1);

            }
        },this,statedata);

        recyclerView.setAdapter(mAdapter);

    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("나가시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        setResult(1);
                        finish();

                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.show();
    }

    void showremove()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.putExtra("position", position);
                        setResult(3,intent);
                        finish();

                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.show();
    }

    @Override
    public void onBackPressed(){

        if(editState){
            show();
        } else {
            sendData();
            return;
        }
    }

    public void getDataDetails(){

        Intent intent = getIntent();

        if(intent != null){

            Bundle bld = intent.getExtras();
            Object obj = bld.get("arrayMonthData");
            position = (int) bld.get("position");

            if(obj != null && obj instanceof MonthData){
                this.monthData = (MonthData) obj;
                arrayVacationData.addAll(monthData.getArrayvacationdata());
            }

        }

        ActionBar ab = getSupportActionBar() ;
        if(monthData.isState()){
            ab.setTitle("수입 편집");
            EditText_title.setBackgroundResource(R.drawable.editbox_round_positive);
        }else{
            ab.setTitle("지출 편집");
            EditText_title.setBackgroundResource(R.drawable.editbox_round_negative);
        }


    }

    void setclickButton(){

        TextView_addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isblank(arrayVacationData)){

                    editState = false;
                    setEditmode(editState);
                    statedata.State = false;

                    arrayVacationData.remove(0);
                    removeblank (arrayVacationData);

                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);

                    setData();


                } else {

                }



            }

        });

        TextView_removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showremove();

            }
        });

        TextView_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editState){
                    sendData();
                }


            }
        });

        TextView_editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editState = true;
                setEditmode(editState);
                statedata.State = true;

                arrayVacationData.add(0,new VacationData()); // + 버튼
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);

            }

        });

    }

    void setEditmode(boolean editState){

        if (editState) {


            nPicker_year.setValue(Integer.parseInt(TextView_yearText.getText().toString()));
            nPicker_month.setValue(Integer.parseInt(TextView_monthText.getText().toString()));
            nPicker_year.setVisibility(View.VISIBLE);
            nPicker_month.setVisibility(View.VISIBLE);
            TextView_addButton.setVisibility(View.VISIBLE);
            TextView_removeButton.setVisibility((View.VISIBLE));

            TextView_saveButton.setVisibility(View.INVISIBLE);
            TextView_editButton.setVisibility(View.INVISIBLE);
            TextView_yearText.setVisibility(View.INVISIBLE);
            TextView_monthText.setVisibility(View.INVISIBLE);

            EditText_title.setEnabled(true);



        } else {


            TextView_yearText.setText(Integer.toString(nPicker_year.getValue()));
            TextView_monthText.setText(Integer.toString(nPicker_month.getValue()));
            nPicker_year.setVisibility(View.INVISIBLE);
            nPicker_month.setVisibility(View.INVISIBLE);
            TextView_addButton.setVisibility(View.INVISIBLE);
            TextView_removeButton.setVisibility((View.INVISIBLE));

            TextView_saveButton.setVisibility(View.VISIBLE);
            TextView_editButton.setVisibility(View.VISIBLE);
            TextView_yearText.setVisibility(View.VISIBLE);
            TextView_monthText.setVisibility(View.VISIBLE);

            EditText_title.setEnabled(false);


        }

    }

    void setData(){

        //아무것도 입력 안했으면 확인 버튼이 활성화 되지가 않아) (EditVacationAdapter)mAdapter).getVacationDate()

        List<VacationData> vacationDataArray = new ArrayList<>();
        vacationDataArray.addAll(((EditVacationAdapter)mAdapter).getVacationData());

        if(vacationDataArray != null){
            removeblank(vacationDataArray);
        }


        if(vacationDataArray != null && vacationDataArray.size()>0){

            editState = false;
            setEditmode(editState);

            monthData.setYear(Integer.parseInt(TextView_yearText.getText().toString()));
            monthData.setMonth(Integer.parseInt(TextView_monthText.getText().toString()));


            String string = EditText_title.getText().toString();
            if(string.equals("")){
                monthData.setTitle("-");
            } else {
                monthData.setTitle(string);
            }

            monthData.setArrayvacationdata(vacationDataArray);

            editState = false;
            setEditmode(editState);


        }

    }

    void sendData(){

        Intent intent = new Intent();

        intent.putExtra("monthData", monthData);
        intent.putExtra("position", position);

        setResult(2,intent);

        finish();

    }

//공간이 모두 빈공간인가. 그럼 버튼 비활성화.
    boolean isblank(List<VacationData> vacationData){

        int length = vacationData.size();
        int blankcount = 0;

        for(int i = length-2; i>=0;i--){
            if(vacationData.get(i+1).getDaysort().equals("") || vacationData.get(i+1).getDaysort() == null ){
                blankcount += 1;
            }
        }

        if(blankcount == vacationData.size()-1){
            return false;
        } else {
            return true;
        }

    }

    public void removeblank (List<VacationData> vacationData){

        int length = vacationData.size();

        for(int i = length-1; i>=0;i--){
            if(vacationData.get(i).getDaysort().equals("") || vacationData.get(i).getDaysort() == null ){
                vacationData.remove(i);
            }
        }

    }

}
