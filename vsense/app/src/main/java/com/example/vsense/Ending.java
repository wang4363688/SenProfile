package com.example.vsense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Ending extends Activity {
    TextView t2,t3,t4,t5,t6,t7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ending);
        t2=(TextView) findViewById(R.id.t2);
        t3=(TextView) findViewById(R.id.t3);
        t4=(TextView) findViewById(R.id.t4);
        t5=(TextView) findViewById(R.id.t5);
        t6=(TextView) findViewById(R.id.t6);
        t7=(TextView) findViewById(R.id.t7);

        Button bn=(Button)findViewById(R.id.exit);
        Bundle bd=getIntent().getExtras();
        Float per=bd.getFloat("percentage");
        Float left=bd.getFloat("left");
        Float right=bd.getFloat("right");
        Float back=bd.getFloat("back");
        Float ltor=bd.getFloat("ltor");
        Float rtol=bd.getFloat("rtol");

        t2.setText("  "+left);
        t3.setText("  "+right);
        t4.setText("  "+rtol);
        t5.setText("  "+ltor);
        t6.setText("  "+back);
        t7.setText("  "+per);
        bn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    }
}
