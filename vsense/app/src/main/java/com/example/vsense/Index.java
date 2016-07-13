package com.example.vsense;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Index extends Activity {

	private MyDatabaseHelper dbHelper;
	EditText edt1;
	EditText edt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_index);
		dbHelper = new MyDatabaseHelper(this, "Vsense.db", null, 1);
        Button b1;
        TextView b2,b3,b4;
        b1=(Button)findViewById(R.id.login);
        b2=(TextView)findViewById(R.id.register);
        b3=(TextView)findViewById(R.id.jump);
        b4=(TextView)findViewById(R.id.find);
		edt1 = (EditText)findViewById(R.id.et1);
		edt2 = (EditText)findViewById(R.id.et2);

		edt1.setFocusable(true);
		edt1.setFocusableInTouchMode(true);
		edt1.requestFocus();

		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = edt1.getText().toString();
				String password = edt2.getText().toString();
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				//选出用户名和密码两列
				Cursor cursor = db.query("Vsense",new String[]{"username","password"},"username == ?"
						,new String[]{username},null,null,null);
				if(cursor.moveToFirst()){
					//不能用等号，要用String的方法
					if(cursor.getString(cursor.getColumnIndex("password")).equals(password)) {
						//密码输入正确
						Toast.makeText(Index.this, "登陆成功！", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(Index.this, StartActivity.class);
						Bundle bundle = new Bundle();
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}else {
						Log.d("password", cursor.getString(cursor.getColumnIndex("password"))+password);
						Toast.makeText(Index.this, "密码输入错误，请重新输入！", Toast.LENGTH_SHORT).show();
						edt1.setText("");
						edt2.setText("");
					}
				}else{
					Toast.makeText(Index.this, "该用户名不存在，请注册！",Toast.LENGTH_SHORT).show();
					edt1.setText("");
					edt2.setText("");
				}

			}
		});
        
        b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Index.this,Register.class);
				startActivity(intent);
				finish();
			}
		});
        
        b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Index.this,StartActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        b4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Index.this,Find.class);
				startActivity(intent);
				finish();
			}
		});
    }
}
