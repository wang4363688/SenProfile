package com.example.vsense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SelectActivity extends Activity implements View.OnClickListener{

    private Button btn_city, btn_high_way, btn_userdefined;
    public static ModeMsg modeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_select);
        modeMsg=new ModeMsg();

        btn_city = (Button) findViewById(R.id.button_city);
        btn_high_way = (Button) findViewById(R.id.button_highway);
        btn_userdefined = (Button) findViewById(R.id.button_userdefined);

        btn_city.setOnClickListener(this);
        btn_high_way.setOnClickListener(this);
        btn_userdefined.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_city:
                dialog_city();
                break;
            case R.id.button_highway:
                dialog_highway();
                break;
            case R.id.button_userdefined:
                dialog_userdefined();
                break;
        }
    }

    private void dialog_city(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:

                        Intent intent1 = new Intent(SelectActivity.this, FgActivity.class);
                        Bundle bd = new Bundle();
                        bd.putFloat("min",-2.0f);
                        bd.putFloat("max",-5.0f);
                        bd.putFloat("z",1.0f);
                        bd.putString("mode","市区模式");
                        intent1.putExtras(bd);
                        startActivity(intent1);
                        modeMsg.setMode("市区模式");
                        modeMsg.setModeNum(1);
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("市区模式"); //设置标题
        builder.setMessage("监测项目包括：行驶时间、左转、右转、调头、向左变道、向右变道、刹车、急刹车、危险驾驶比例"); //设置内容
        builder.setPositiveButton("开始旅途",dialogOnclicListener);
        builder.setNegativeButton("重新选择", dialogOnclicListener);
        builder.create().show();
    }

    private void dialog_highway(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        Intent intent2 = new Intent(SelectActivity.this, FgActivity.class);
                        Bundle bd1 = new Bundle();
                        bd1.putFloat("min",-2.0f);
                        bd1.putFloat("max",-5.0f);
                        bd1.putFloat("z",1.0f);
                        bd1.putString("mode","高速模式");
                        intent2.putExtras(bd1);
                        startActivity(intent2);

                        modeMsg.setMode("高速模式");
                        modeMsg.setModeNum(2);

                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("高速模式"); //设置标题
        builder.setMessage("监测项目包括：行驶时间、向左变道、向右变道、刹车、急刹车、危险驾驶比例"); //设置内容
        builder.setPositiveButton("开始旅途",dialogOnclicListener);
        builder.setNegativeButton("重新选择", dialogOnclicListener);
        builder.create().show();
    }

    private void dialog_userdefined(){
        final String items[]={"行驶时间","左转","右转","调头","向左变道","向右变道","刹车","急刹车","危险驾驶比例"};
        final boolean selected[]={true,true,true,true,true,true,true,true,true,true,true};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("自定义模式"); //设置标题
        builder.setMultiChoiceItems(items,selected,new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(SelectActivity.this, StartActivity.class);
                startActivity(intent);

                //android会自动根据你选择的改变selected数组的值。
                for (int i=0;i<selected.length;i++){
                    Log.e("hongliang",""+selected[i]);
                }

                modeMsg.setMode("自定义模式");
                modeMsg.setModeNum(3);
                modeMsg.setSelect(selected);
//                message.obj=modeMsg;

            }
        });
        builder.create().show();
    }

}
