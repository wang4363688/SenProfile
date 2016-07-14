package com.example.vsense;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_welcome);
        final Intent it = new Intent(Welcome.this, StartActivity.class); //��Ҫת���Activity

        Timer timer = new Timer(); 

        TimerTask task = new TimerTask() {  

            @Override  

            public void run() {   

            	startActivity(it); //ִ��  
            	finish();
            } 

         };

        timer.schedule(task, 1000 * 1); 
    }

}

