package com.racoolab.vacationapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.racoolab.vacationapp.adapter.MainDayAdapter;
import com.racoolab.vacationapp.R;
import com.racoolab.vacationapp.adapter.SlidingAdapter;
import com.racoolab.vacationapp.datatype.MonthData;
import com.racoolab.vacationapp.datatype.MonthDataCompare;
import com.racoolab.vacationapp.datatype.SlidingVacationData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MonthData> arrayMonthData;

    private RecyclerView recyclerView_sliding;
    private RecyclerView.Adapter mAdapter_sliding;
    private RecyclerView.LayoutManager layoutManager_sliding;

    private TextView TextView_addButton;
    private TextView TextView_cancelButton;
    private TextView TextView_Totalvalue;
    private ImageView ImageView_notification;


    private int position = 0;
    private int slidingPosition = 0;
    private int setPosition = 0;

    public AdView mAdView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search :
                // TODO : process the click event for action_search item.

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("초기화하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                arrayMonthData.clear();
                                mAdapter.notifyDataSetChanged();
                                save(arrayMonthData);


                                int total = 0;
                                if(arrayMonthData.size() == 0){
                                    TextView_Totalvalue.setText("잔여 " + Integer.toString(total));
                                }


                                if(arrayMonthData.size() == 0){
                                    ImageView_notification.setVisibility(View.VISIBLE);
                                }

                                setSlidingData();

                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                builder.show();
                return true ;

            default :
                return false;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slidingup);

        MobileAds.initialize(this, "ca-app-pub-7972968096388401~4035110400");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("휴가 목록");

        TextView_addButton = findViewById(R.id.TextView_addButton);
        TextView_cancelButton = findViewById(R.id.TextView_minusButton);
        TextView_Totalvalue = findViewById(R.id.TextView_Totalvalue);
        ImageView_notification = findViewById(R.id.ImageView_notification);



        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        arrayMonthData = load();
        Collections.sort(arrayMonthData, new MonthDataCompare());
        setSlidingData();

        if(arrayMonthData.size() != 0){
            ImageView_notification.setVisibility(View.INVISIBLE);
        } else {
            ImageView_notification.setVisibility(View.VISIBLE);
        }

        int total = 0;
        if(arrayMonthData.size()>0){
            total = setTotal(arrayMonthData, total);
            TextView_Totalvalue.setText("잔여 " + Integer.toString(total));
        } else {
            TextView_Totalvalue.setText("잔여 " + Integer.toString(total));
        }



//        arrayMonthData = new ArrayList<>();

        mAdapter = new MainDayAdapter(arrayMonthData, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getTag() != null){
                    position = (int) v.getTag();
                    Intent intent = new Intent(MainActivity.this, EditVacationActivity.class);
                    intent.putExtra("arrayMonthData",arrayMonthData.get(position));
                    intent.putExtra("position",position);
                    startActivityForResult(intent,0);

                }

            }
        });

        recyclerView.setAdapter(mAdapter);


        TextView_addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddVacationDataActivity.class);

                startActivityForResult(intent, 0);


            }
        });

        TextView_cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MinusVacationDataActivity.class);

                startActivityForResult(intent, 0);

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        int total = 0;

        switch (resultCode) {

            case 0:


                if(data != null){

                    Bundle bld = data.getExtras();
                    MonthData monthData = (MonthData) bld.get("monthData");
                    arrayMonthData.add(monthData);

                    Collections.sort(arrayMonthData, new MonthDataCompare());
//                    mAdapter.notifyItemInserted(arrayMonthData.size());

                    mAdapter.notifyDataSetChanged();


                    if(arrayMonthData.size()>0){
                        total = setTotal(arrayMonthData, total);
                        TextView_Totalvalue.setText("잔여 " + Integer.toString(total));
                    }

                    setSlidingData();

                    if(arrayMonthData.size() != 0){
                        ImageView_notification.setVisibility(View.INVISIBLE);
                    }


                    save(arrayMonthData);
                    break;

                }


                break;



            case 2:



                if(data != null){

                    Bundle edit = data.getExtras();

                    MonthData monthData = (MonthData) edit.get("monthData");
                    setPosition = (int) edit.get("position");

                    arrayMonthData.remove(setPosition);
                    arrayMonthData.add(setPosition,monthData);

                    Collections.sort(arrayMonthData, new MonthDataCompare());

//                    mAdapter.notifyItemChanged(position);
                    mAdapter.notifyDataSetChanged();

                    total = 0;
                    if(arrayMonthData.size()>0){
                        total = setTotal(arrayMonthData, total);
                        TextView_Totalvalue.setText("잔여 " + Integer.toString(total));
                    }

                    setSlidingData();

                    save(arrayMonthData);


                    break;

                }


                break;

            case 3:

                if(data != null) {

                    Bundle edit = data.getExtras();
                    setPosition = (int) edit.get("position");

                    arrayMonthData.remove(setPosition);
//                mAdapter.notifyItemRemoved(position);

                    Collections.sort(arrayMonthData, new MonthDataCompare());
//                mAdapter.notifyItemRangeChanged(position,arrayMonthData.size());

                    mAdapter.notifyDataSetChanged();

                    total = 0;
                    if(arrayMonthData.size()>0) {
                        total = setTotal(arrayMonthData, total);
                        TextView_Totalvalue.setText("잔여 " + Integer.toString(total));
                    } else {
                        TextView_Totalvalue.setText("잔여 " + Integer.toString(total));
                    }

                    setSlidingData();

                    if(arrayMonthData.size() == 0){
                        ImageView_notification.setVisibility(View.VISIBLE);
                    }

                    save(arrayMonthData);
                    break;

                }


            default:

                break;

        }

    }





    void save (List<MonthData> monthData){

        List<MonthData> inputdata = new ArrayList<>();
        inputdata.addAll(monthData);

//        Gson gson = new Gson();
//        Type listType = new TypeToken<ArrayList<MonthData>>() {}.getType();
//        String json = gson.toJson(monthData, listType);
//
//        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("contacts", json); // JSON으로 변환한 객체를 저장한다.
//        editor.commit(); //완료한다.

        // 어? 왜 이게 되지
        // 이전에는 typetoken 을 세이브 로드 할때 사용했는데
        // 세이브할때는 타입토큰과 리스트 타입을 이용하지 않으니 되네
        //대체 타입 토큰과 리스트 타입은 대체 뭐지?
        //그리고 그 작성자는 왜 세이브할때 앞서 했었던 토큰과 리스트 타입은 사용하지 않은걸까.

        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(monthData);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Set",json );
        editor.commit();


    }

    List<MonthData> load() {

//        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
//        String strContact = sp.getString("contacts", "");
//
//        if(strContact == null || strContact.length()<1){
//            return new ArrayList<>();
//        } else {
//
//            Type listType = new TypeToken<ArrayList<MonthData>>() {}.getType();
//            return gson.fromJson(strContact, listType);
//        }

        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("Set", "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<MonthData>>() {
            }.getType();
            return gson.fromJson(json, type);

        }

    }


    int setTotal(List<MonthData> arrayMonthData,int total){

        for(int i = 0 ;i<arrayMonthData.size();i++){

            if(arrayMonthData.get(i).getArrayvacationdata().size()>0){

                int subtotal = 0;

                for(int j = 0 ; j<arrayMonthData.get(i).getArrayvacationdata().size();j++){

                    subtotal += arrayMonthData.get(i).getArrayvacationdata().get(j).getDays();

                }

                if(arrayMonthData.get(i).isState()){
                    total += subtotal;
                } else {
                    total -= subtotal;
                }

            } else {
                total = 0;
            }

        }

        return total;

    }

    void setSlidingData(){

        recyclerView_sliding = (RecyclerView) findViewById(R.id.recyclerview_sliding);
        recyclerView_sliding.setHasFixedSize(true);
        layoutManager_sliding = new LinearLayoutManager(this);
        recyclerView_sliding.setLayoutManager(layoutManager_sliding);


        List<SlidingVacationData> positiveArray = new ArrayList<>();
        List<SlidingVacationData> negativeArray = new ArrayList<>();

        SlidingVacationData slidingvacationdata = new SlidingVacationData();
        slidingvacationdata.setDaysort("수입 요약");
        slidingvacationdata.setDays(0);
        positiveArray.add(slidingvacationdata);

        slidingvacationdata = new SlidingVacationData();
        slidingvacationdata.setDaysort("지출 요약");
        slidingvacationdata.setDays(0);
        negativeArray.add(slidingvacationdata);


        for(int i = arrayMonthData.size()-1;i>=0;i--) {

            if (arrayMonthData.get(i).isState()) {
                for (int j = 0; j < arrayMonthData.get(i).getArrayvacationdata().size(); j++) {
                    slidingvacationdata = new SlidingVacationData();
                    slidingvacationdata.setDaysort(arrayMonthData.get(i).getArrayvacationdata().get(j).getDaysort());
                    slidingvacationdata.setDays(arrayMonthData.get(i).getArrayvacationdata().get(j).getDays());

                    slidingvacationdata.setNumber(i);
                    positiveArray.add(slidingvacationdata);

                }


            } else {

                for (int j = 0; j < arrayMonthData.get(i).getArrayvacationdata().size(); j++) {
                    slidingvacationdata = new SlidingVacationData();
                    slidingvacationdata.setDaysort(arrayMonthData.get(i).getArrayvacationdata().get(j).getDaysort());
                    slidingvacationdata.setDays(arrayMonthData.get(i).getArrayvacationdata().get(j).getDays());

                    slidingvacationdata.setNumber(i);
                    negativeArray.add(slidingvacationdata);
                }

            }
        }

        positiveArray.addAll(negativeArray);


        mAdapter_sliding = new SlidingAdapter(positiveArray, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getTag() != null) {

                    slidingPosition = (int) v.getTag();
                    Intent intent = new Intent(MainActivity.this, EditVacationActivity.class);
                    intent.putExtra("arrayMonthData", arrayMonthData.get(slidingPosition));
                    intent.putExtra("position",slidingPosition);
                    startActivityForResult(intent, 0);

                }

            }

        });

        recyclerView_sliding.setAdapter(mAdapter_sliding);


    }


}
