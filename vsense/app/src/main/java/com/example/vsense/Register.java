package com.example.vsense;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reg);
        Button b1,b2,b3;
        b1=(Button)findViewById(R.id.login);
        dbHelper = new MyDatabaseHelper(this,"Vsense.db", null, 1);

        
        b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText edt1 = (EditText)findViewById(R.id.username);
                EditText edt2 = (EditText)findViewById(R.id.password);
                EditText edt3 = (EditText)findViewById(R.id.phonenum);
                EditText edt4 = (EditText)findViewById(R.id.email);
                String username = edt1.getText().toString();
                String password = edt2.getText().toString();
                String phoneNum = edt3.getText().toString();
                String email = edt4.getText().toString();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if("".equals(username.trim())||"".equals(password.trim())||
                        "".equals(phoneNum.trim())||"".equals(email.trim())){
                    Toast.makeText(Register.this, "Please input username and password",
                            Toast.LENGTH_SHORT).show();
                }else{
                    db.execSQL("insert into Vsense (username, password, phoneNum, email) values(?,?,?,?)",
                            new String[]{username, password, phoneNum, email});
                    edt1.setText("");
                    edt2.setText("");
                    edt3.setText("");
                    edt4.setText("");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
                    dialog.setTitle("register succeed");
                    dialog.setMessage("Register Succeed!");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Register.this, Index.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }
				
				// TODO Auto-generated method stub
				Intent intent=new Intent(Register.this,Index.class);
				Bundle bundle=new Bundle();
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
        
 
    }
}
