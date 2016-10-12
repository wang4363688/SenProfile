package com.example.vsense;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;

public class Ending extends Activity {
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    TableRow row1,row2,row3,row4,row5,row6,row7,row8,row9;
    private ModeMsg modeMsg1;
    public Bundle bd;
    public long driveTime;
    public static String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ending);
        row1= (TableRow) findViewById(R.id.row1);
        row2= (TableRow) findViewById(R.id.row2);
        row3= (TableRow) findViewById(R.id.row3);
        row4= (TableRow) findViewById(R.id.row4);
        row5= (TableRow) findViewById(R.id.row5);
        row6= (TableRow) findViewById(R.id.row6);
        row7= (TableRow) findViewById(R.id.row7);
        row8= (TableRow) findViewById(R.id.row8);
        row9= (TableRow) findViewById(R.id.row9);

        t1= (TextView) findViewById(R.id.tv_time);
        t2=(TextView) findViewById(R.id.tv_tl);
        t3=(TextView) findViewById(R.id.tv_tr);
        t4=(TextView) findViewById(R.id.tv_ctl);
        t5=(TextView) findViewById(R.id.tv_ctr);
        t6=(TextView) findViewById(R.id.tv_U);
        t7=(TextView) findViewById(R.id.tv_brake);
        t8=(TextView) findViewById(R.id.tv_brake_high);
        t9=(TextView) findViewById(R.id.per);
        t10=(TextView) findViewById(R.id.tv_mode);

        modeMsg1=SelectActivity.modeMsg;

        Button anotherTime = (Button)findViewById(R.id.anotherTime);
        bd=getIntent().getExtras();

        float per=bd.getFloat("percentage");
        float left=bd.getFloat("left");
        float right=bd.getFloat("right");
        float back=bd.getFloat("back");
        float ltor=bd.getFloat("ltor");
        float rtol=bd.getFloat("rtol");
        float brake = bd.getFloat("brake");
        float eBrake = bd.getFloat("eBrake");

        if(modeMsg1.getMode()!=null){
            String string_mode = modeMsg1.getMode();
            t10.setText(string_mode);
        }

            if (modeMsg1.getmodeNum() == 1) {

            } else if (modeMsg1.getmodeNum() == 2) {
                row2.setVisibility(View.GONE);
                row3.setVisibility(View.GONE);
                row4.setVisibility(View.GONE);
            }


        if(modeMsg1.getSelect()!=null){
            boolean[] selected=modeMsg1.getSelect();
//            for(int i=0;i<selected.length;i++){
                if(selected[0]==false){
                    row1.setVisibility(View.GONE);
                }
            if(selected[1]==false){
                row2.setVisibility(View.GONE);
            }
            if(selected[2]==false){
                row3.setVisibility(View.GONE);
            }
            if(selected[3]==false){
                row4.setVisibility(View.GONE);
            }
            if(selected[4]==false){
                row5.setVisibility(View.GONE);
            }
            if(selected[5]==false){
                row6.setVisibility(View.GONE);
            }
            if(selected[6]==false){
                row7.setVisibility(View.GONE);
            }if(selected[7]==false){
                row8.setVisibility(View.GONE);
            }
            if(selected[8]==false){
                row9.setVisibility(View.GONE);
            }

        }

        driveTime = bd.getLong("driveTime");
        time=formatLongToTimeStr(driveTime);

        t1.setText(time);
        t2.setText("  "+left);
        t3.setText("  "+right);
        t4.setText("  "+rtol);
        t5.setText("  "+ltor);
        t6.setText("  "+back);
        t7.setText("  "+brake);
        t8.setText("  "+eBrake);
        t9.setText("  "+per);



//        bn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                am.restartPackage(getPackageName());
//                finish();
//            }
//        });
//        deleteCache.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteSDcard("vsense");
//            }
//        });
        anotherTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ending.this, SelectActivity.class);
                deleteSDcard("vsense");
                startActivity(intent);
                finish();
            }
        });
    }

    public void deleteSDcard(String fileName){
        try {
            // if the SDcard exists 判断是否存在SD卡
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // get the directory of the SDcard 获取SD卡的目录
                File sdDire = Environment.getExternalStorageDirectory();
                File file = new File(sdDire.getCanonicalPath()+"/"+fileName+".txt");
                if(file.isFile()){
                    file.delete();
                    Log.d("SDcard","delete");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatLongToTimeStr(Long driveTime) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = (int) (driveTime / 1000);

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if(data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

}
