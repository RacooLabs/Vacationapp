package com.racoolab.vacationapp.activity;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.racoolab.vacationapp.R;
import com.racoolab.vacationapp.adapter.AddVacationDataAdapter;
import com.racoolab.vacationapp.datatype.MonthData;
import com.racoolab.vacationapp.datatype.VacationData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddVacationDataActivity extends AppCompatActivity implements OnItemClick {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<VacationData> arrayVacationData;
    private MonthData monthData;

    NumberPicker nPicker_year;
    NumberPicker nPicker_month;
    EditText EditText_title;

    Calendar calendar = Calendar.getInstance();
    int pickeryear = calendar.get(Calendar.YEAR);
    int pickermonth = calendar.get(Calendar.MONTH);


    private TextView TextView_addButton;
    private TextView TextView_TextView_subTotalvalue;
    private TextView TextView_cancelButton;


    @Override
    public void onClick (int subtotal){
        TextView_TextView_subTotalvalue.setText("합계 " + subtotal);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vacation_data);

        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("수입");



        arrayVacationData = new ArrayList<>(); // 넘길 배열
        arrayVacationData.add(new VacationData()); // + 버튼
        monthData = new MonthData();


        setBind();
        setNpicker();
        setRecyclerView();



        TextView_addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 아무것도 입력 안했으면 확인 버튼이 활성화 되지가 않아) (AddVacationDataAdapter)mAdapter).getVacationDate()
                // 이 데이터를 바탕으로 락 걸어버리자

                List<VacationData> vacationDataArray = new ArrayList<>();
                vacationDataArray.addAll(((AddVacationDataAdapter)mAdapter).getVacationData());

                if(vacationDataArray != null){
                    removeblank(vacationDataArray);
                }


                if(vacationDataArray != null && vacationDataArray.size()>0){


                    monthData.setState(true);
                    monthData.setYear(nPicker_year.getValue());
                    monthData.setMonth(nPicker_month.getValue());

                    String string = EditText_title.getText().toString();
                    if(string.equals("")){
                        monthData.setTitle("-");
                    } else {
                        monthData.setTitle(string);
                    }

                    monthData.setArrayvacationdata(vacationDataArray);

                    Intent intent = new Intent();

                    intent.putExtra("monthData", monthData);

                    setResult(0, intent);

                    finish();

                } else {


                    setResult(1);


                }


            }

        });

        TextView_cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show();

            }
        });





    }



    public void setBind(){

        nPicker_year = (NumberPicker) findViewById(R.id.npicker_year);
        nPicker_month = (NumberPicker) findViewById(R.id.npicker_month);
        TextView_addButton = (TextView) findViewById(R.id.TextView_addButton);
        TextView_TextView_subTotalvalue = (TextView) findViewById(R.id.TextView_subTotalvalue);
        EditText_title = (EditText) findViewById((R.id.EditText_title));
        TextView_cancelButton = (TextView) findViewById(R.id.TextView_cancelButton);
        EditText_title.setBackgroundResource(R.drawable.editbox_round_positive);

    }

    public void setNpicker(){


        nPicker_year.setMinValue(pickeryear-1);
        nPicker_year.setMaxValue(pickeryear+1);
        nPicker_year.setValue(pickeryear);

        nPicker_month.setMinValue(1);
        nPicker_month.setMaxValue(12);
        nPicker_month.setValue(pickermonth+1);

    }

    public void setRecyclerView(){

        recyclerView = (RecyclerView) findViewById(R.id.addDayrecyclerView);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)

        mAdapter = new AddVacationDataAdapter(arrayVacationData, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((AddVacationDataAdapter)mAdapter).addVacationData();
                recyclerView.smoothScrollToPosition(arrayVacationData.size()-1);

            }
        },this);
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

    @Override
    public void onBackPressed(){
        show();
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
