package com.example.vsense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {
	private Button buttonstart;
	EditText et1,et2,et3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_start);
		et1=(EditText)findViewById(R.id.br);
		et2=(EditText)findViewById(R.id.em);
		et3=(EditText)findViewById(R.id.zz);

		buttonstart=(Button) findViewById(R.id.startbutton);
		
		buttonstart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String filename = "vsense(" +" P: 1  Q 100 R: 1" +")";
				DataSet.P_00 = 1;
				DataSet.Q = 100;
				DataSet.R = 1;
				MainActivity.filename = filename;
				float min= Float.parseFloat(et1.getText().toString());
				float max= Float.parseFloat(et2.getText().toString());
				float z=Float.parseFloat(et3.getText().toString());
				Intent intent = new Intent(StartActivity.this, MainActivity.class);
				Bundle bd=new Bundle();
				bd.putFloat("min",min);
				bd.putFloat("max",max);
				bd.putFloat("z",z);
				intent.putExtras(bd);
				startActivity(intent);
				finish();
			}
		});
	}
}
