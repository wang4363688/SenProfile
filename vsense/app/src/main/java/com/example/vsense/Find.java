package com.example.vsense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Find extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_find);
        Button b1,b2,b3;
        b1=(Button)findViewById(R.id.fin);

        
        b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Find.this,Index.class);
				Bundle bundle=new Bundle();
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
    }
}
