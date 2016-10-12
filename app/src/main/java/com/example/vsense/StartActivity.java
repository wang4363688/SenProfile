package com.example.vsense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class StartActivity extends Activity implements View.OnClickListener {
	private EditText et1, et2, et3;
	private Button btn_start;
	private ImageButton Ibt_wh1, Ibt_wh2, Ibt_wh3,Ibt_back1;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
//	private GoogleApiClient client;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_start);

		et1 = (EditText) findViewById(R.id.brake_min);
		et2 = (EditText) findViewById(R.id.brake_max);
		et3 = (EditText) findViewById(R.id.angularspeed_min);

		btn_start = (Button) findViewById(R.id.button_start);
		Ibt_wh1 = (ImageButton) findViewById(R.id.wenhao1);
		Ibt_wh2 = (ImageButton) findViewById(R.id.wenhao2);
		Ibt_wh3 = (ImageButton) findViewById(R.id.wenhao3);
		Ibt_back1 = (ImageButton) findViewById(R.id.back1);

		btn_start.setOnClickListener(this);
		Ibt_wh1.setOnClickListener(this);
		Ibt_wh2.setOnClickListener(this);
		Ibt_wh3.setOnClickListener(this);
		Ibt_back1.setOnClickListener(this);
	}

	    @Override
		public void onClick(View view) {
			switch (view.getId()){
				case R.id.button_start:
					String filename = "vsense";
					DataSet.P_00 = 1;
					DataSet.Q = 100;
					DataSet.R = 1;
					MainActivity.filename = filename;

					String minStr = et1.getText().toString();
					String maxStr = et2.getText().toString();
					String zStr = et3.getText().toString();

					if(!minStr.equals("") && !maxStr.equals("") && !zStr.equals("")){
						float min= Float.parseFloat(minStr);
						float max= Float.parseFloat(maxStr);
						float z=Float.parseFloat(zStr);
						Intent intent = new Intent(StartActivity.this, FgActivity.class);
						Bundle bd=new Bundle();
						bd.putFloat("min",min);
						bd.putFloat("max",max);
						bd.putFloat("z",z);
						bd.putString("mode","自定义模式");
						intent.putExtras(bd);
						startActivity(intent);

					}else {
						Toast.makeText(StartActivity.this, "参数不能为空！", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.wenhao1:
					dialog_brmin();
					break;
				case R.id.wenhao2:
					dialog_brmax();
					break;
				case R.id.wenhao3:
					dialog_angularmin();
					break;
				case R.id.back1:
					Intent intent1 = new Intent(StartActivity.this, SelectActivity.class);
					startActivity(intent1);
			}

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	private void dialog_brmin(){
		//先new出一个监听器，设置好监听
		DialogInterface.OnClickListener dialogOnclickListener=new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
					case Dialog.BUTTON_POSITIVE:
						break;
				}
			}
		};
		//dialog参数设置
		AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
		builder.setTitle("刹车加速度最小值域"); //设置标题
		builder.setMessage("刹车事件的加速度下限"); //设置内容
		builder.setPositiveButton("我了解了",dialogOnclickListener);
		builder.create().show();
	}


	private void dialog_brmax(){
		//先new出一个监听器，设置好监听
		DialogInterface.OnClickListener dialogOnclickListener=new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
					case Dialog.BUTTON_POSITIVE:
						break;
				}
			}
		};
		//dialog参数设置
		AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
		builder.setTitle("刹车加速度最大值域"); //设置标题
		builder.setMessage("刹车事件的加速度上限"); //设置内容
		builder.setPositiveButton("我了解了",dialogOnclickListener);
		builder.create().show();
	}


	private void dialog_angularmin(){
		//先new出一个监听器，设置好监听
		DialogInterface.OnClickListener dialogOnclickListener=new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
					case Dialog.BUTTON_POSITIVE:
						break;
				}
			}
		};
		//dialog参数设置
		AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
		builder.setTitle("转弯角速度最小值域"); //设置标题
		builder.setMessage("转弯事件的角速度下限"); //设置内容
		builder.setPositiveButton("我了解了",dialogOnclickListener);
		builder.create().show();
	}
}
