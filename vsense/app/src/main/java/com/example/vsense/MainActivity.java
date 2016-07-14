package com.example.vsense;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener {
	LocationManager lm;
	SensorManager sm;
	TextView tv;
	Button bu;
	ImageView iv;

	int[] images = new int[]{
			R.drawable.car1, R.drawable.car2,
			R.drawable.car3, R.drawable.car4,
			R.drawable.car5, R.drawable.car6,
	};

	final static int No_Bump = 0;
	final static int One_Bump = 1;
	final static int Waiting_for_Bump = 2;
	final static float s = (float) 0.03;
	final static float h = (float) 0.05;
	float z = (float) 1.0;
	final static float t = (float) 1.0;
	final static float T_NEXT_DELAY = (float) 2.0;

	float ACC_OF_BRAKE = (float) -2.0;
	float EMERGENCY = (float) -5.0;


	public float ALL_OF_BUMP = 0;
	public float BAD_TURN_1 = 0;
	public float BAD_TURN_2 = 0;
	public float BAD_TURN = 0;
	public int BAD_ACC = 0;
	public float accelerationFromGps;
	public float speedLatter = 0;
	public float speedFormer;
	public float speed;        //get the velocity from GPS
	public float velocity = 0;       //compute the velocity from acceleration
	public float velocity1 = 0;
	public float velo = 0;
	public float velo1 = 0;
	public float avervelocity;
	public float avervelocity1;
	public float v;            //get the angular acceleration from gyroscope 
	public float v1;
	public double accelerator;  //acceleration from accelerator
	public double accelerator1;
	public double averalinear;//acceleration in the geo-frame
	public double averalinear1;
	public double averacc;
	public double averacc1;
	public double hpaveracc;
	public double hpaveracc1;
	public double accnoise = 0;
	public double accnoise1;

	public int flag = 0;
	public int FLAG_OF_BRAKE = 0;
	public float MAX_OF_BRAKE = 0;
	//设置采样频率
	public float intervalT;
	public float times = 0;
	public float turnLeftTimes = 0;
	public float turnRightTimes = 0;
	public float turnBackTimes = 0;
	public float ltorTimes = 0;
	public float rtolTimes=0;
	public float brakeTimes=0;
	public float curvyRoadTimes = 0;
	public float shift = 0;
	public static int start = 0;
	public static int begin, begin2 = 0;
	public static int end, end2 = 0;
	public float max;
	public float max1;
	public float max2;
	public float T_BUMP, T_BUMP2, T_dwell = 0;
	public int state = No_Bump;
	public int start_of_2nd_bump = 0;
	public float distance;
	public float ang;
	public float tdistance;
	public float tang;
	public float KalmanFilterSpeed;
	public double ori;
	public float ang_degree;
	public float[] vs = new float[200];
	public float[] speeds = new float[200];
	//private float accelerators[]=new float[100];
	public float[] velocitys = new float[200];
	public float[] velos = new float[200];
	public float[] accelerometerValues = new float[3];//get three-dimensional values from accelerometer从加速计获取3D值
	public float[] magneticFieldValues = new float[3];//get three-dimensional values from gyroscope从磁场计获取3D值
	public float[] orientation = new float[3];//get the azimuth from values above 从加速计和磁场计计算得来的方位角

	public static String filename = "vsense";

	DataSet dataset = new DataSet();
	MovingAverage moveAverage;
	MovingAverage moveAveragedir;
	MovingAverage moveAverageacc;

	//	public int middle=0;
	//返回值是角度
	public float angle_calculate(float[] vs, int b, int e) {
		float aaa = 0;
		float aaa1 = 0;
		if (e >= b) {
			for (int n = b; n <= e; n++) {
				aaa = (float) (aaa + vs[n] * 0.05 * 57.29578);
			}
		} else if (e < b) {
			for (int n = b; n < 200; n++) {
				aaa = (float) (aaa + vs[n] * 0.05 * 57.29578);
			}
			for (int n = 0; n <= e; n++) {
				aaa = (float) (aaa + vs[n] * 0.05 * 57.29578);
			}
		}

		DecimalFormat df2 = new DecimalFormat("#.00");
		aaa1 = Float.valueOf(df2.format(aaa));

		return aaa1;
	}


	//need to be finished
	private void writeSDcard(String str) {
		try {
			// if the SDcard exists 判断是否存在SD卡
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// get the directory of the SDcard 获取SD卡的目录
				File sdDire = Environment.getExternalStorageDirectory();
				FileOutputStream outFileStream = new FileOutputStream(
						sdDire.getCanonicalPath() + "/" + filename + ".txt", true);
				outFileStream.write(str.getBytes());
				outFileStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public float getdistance(float ve[], float vv[], int b, int e) {
		float d = 0;
		float d1 = 0;
		float a = 0;

		if (e >= b) {
			for (int i = b; i < e; i++) {
				a = a + vv[i] * (float) 0.05;     //angle=angle before * time (100ms/0.1s)角度等于原始角度加陀螺仪角速度乘以0.1秒时间
				d = d + ve[i] * (float) 0.05 * (float) Math.sin(a);  //horizontal displacement 水平距离等于速度乘以0.1秒时间乘以sin值
			}
		} else if (e < b) {
			for (int i = b; i < 200; i++) {
				a = a + vv[i] * (float) 0.05;     //angle=angle before * time (100ms/0.1s)角度等于原始角度加陀螺仪角速度乘以0.1秒时间
				d = d + ve[i] * (float) 0.05 * (float) Math.sin(a);  //horizontal displacement 水平距离等于速度乘以0.1秒时间乘以sin值
			}
			for (int i = 0; i <= e; i++) {
				a = a + vv[i] * (float) 0.05;     //angle=angle before * time (100ms/0.1s)角度等于原始角度加陀螺仪角速度乘以0.1秒时间
				d = d + ve[i] * (float) 0.05 * (float) Math.sin(a);  //horizontal displacement 水平距离等于速度乘以0.1秒时间乘以sin值
			}
		}

		DecimalFormat df = new DecimalFormat("#.00");
		d1 = Float.valueOf(df.format(d));

		return d1;
	}


	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			//计算gps加速度的定时器
			if (msg.what == 0x110) {
				accelerationFromGps = speedLatter - speedFormer;
			}
			if (msg.what == 0x111) {       //find the message according to 'what' 根据what字段判断是哪个消息


				times++;                //全局变量，统计log记得次数
				moveAverage.pushValue(v);
				v = moveAverage.getValue();
				vs[start] = v;
				//if(velocity==0){velocity=accelerator;}
				//if(velocitys[99]!=0){velocitys[99]=(float) 0.0;}
//        		if((start-1)<=0){
//        			middle=(start-1)+100;
//        			}else{
//        				middle=start-1;
//        				}

				avervelocity += averalinear * 0.05;
//        		velocity+=averacc*0.05;
//        		velo+=accelerator*0.05;
				DecimalFormat df = new DecimalFormat("#.00");
				avervelocity1 = Float.valueOf(df.format(avervelocity));

//        		velocity1 = Float.valueOf(df.format(velocity));
//        		velo1 = Float.valueOf(df.format(velo));
				velocitys[start] = avervelocity;
//        		velos[start]=velo1;
				KalmanFilterSpeed = algorithmKalmanFilter();
				speeds[start] = speed;
				//speeds[start]=KalmanFilterSpeed;
				start = (start + 1) % 200;   //start++;if start exceed 100, than begin from 0 但是防止start超过100，过了100后从0开始循环


				tdistance = Math.abs(getdistance(speeds, vs, (start + 199) % 200, start));//改成start
				tang = angle_calculate(vs, (start + 199) % 200, start);//改成start
				ori = calculateOrientation();


				distance = Math.abs(getdistance(speeds, vs, begin, start));
				ang = angle_calculate(vs, begin, start);


				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
				String str = sdf.format(new Date());
				String message = str;

				writeSDcard(message + "\t" + accelerator1 + "\t" + averacc1 + "\t" + averalinear1 + "\t" + accelerationFromGps + "\t" + avervelocity1 + "\t"
						+ speed + "\t" + KalmanFilterSpeed + "\t"
						+ v1 + "\t" + moveAverage.getValue() + "\t" + ang + "\t"
						+ ori + "\t" + distance + "\t" + tang + "\t" + tdistance + "\t" + "\n");


				//判断刹车
				if (averalinear1 <= ACC_OF_BRAKE) {
					FLAG_OF_BRAKE++;
					if (averalinear1 < MAX_OF_BRAKE) {
						MAX_OF_BRAKE = (float) averalinear1;
					}
				} else if (averalinear1 > ACC_OF_BRAKE) {
					if (FLAG_OF_BRAKE > 2) {
						tv.setText("brake");
						if (MAX_OF_BRAKE < EMERGENCY) {
							tv.setText("Emergency brake");
						}
					}
					MAX_OF_BRAKE = 0;
					FLAG_OF_BRAKE = 0;
				}


				if (state == No_Bump && Math.abs(v) > s) {     //陀螺仪读数大于s，进入一个凸点状态
					state = One_Bump;

					begin = start;
					max = v;

				} else if (state == One_Bump && Math.abs(v) > s) {      //此时为一个凸点状态
					if (Math.abs(v) > z) {
						BAD_TURN_1 = 1;
					}

					T_BUMP = T_BUMP + (float) 0.05;                 //第一个凸点的停留时间
					if (Math.abs(v) > Math.abs(max)) {
						max = v;
					}                               //计算测量过程中的最大偏航率
				} else if (state == One_Bump && Math.abs(v) <= s) {
					end = start;


					if (Math.abs(max) > h && T_BUMP > t) {

						ALL_OF_BUMP++;
						BAD_TURN += BAD_TURN_1;
/*
						if (angle_calculate(vs, begin, end) < -60 && angle_calculate(vs, begin, end) > -135) {
							//Toast.makeText(MainActivity.this, "Turn Right", Toast.LENGTH_SHORT).show();
							tv.setText("Turn Right");
							iv.setImageResource(images[2]);
							//turnRightTimes++;
							writeSDcard(message + "\t" + "Turn Right" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\n");
						} else if (angle_calculate(vs, begin, end) < 135 && angle_calculate(vs, begin, end) > 60) {
							//Toast.makeText(MainActivity.this, "Turn Left", Toast.LENGTH_SHORT).show();
							tv.setText("Turn Left");
							iv.setImageResource(images[1]);
							//turnLeftTimes++;
							writeSDcard(message + "\t" + "Turn Left" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\n");
						}//此时判定检测到的凸点为有效凸点，进入等待凸点状态
*/
						max1 = max;
						state = Waiting_for_Bump;
						max = 0;
					} else {                                     //如不符合三个条件，则判定刚才检测到的凸点无效，算法返回无凸点状态
						T_BUMP = 0;
						state = No_Bump;
						max = 0;
						BAD_TURN_1 = 0;
						iv.setImageResource(images[0]);
					}

				} else if (state == Waiting_for_Bump) {              //如果此时进入了等待凸点状态
					if (Math.abs(v) <= s&&start_of_2nd_bump == 0) {
						T_dwell = T_dwell + (float) 0.05;                //计算上一个凸点第二个s值与下一个凸点第一个s值之间的时间
					}

					//如果两个s之间时间间隔超过2秒 则不会打出finished
					if (T_dwell < T_NEXT_DELAY && Math.abs(v) > s) {   //如果两个凸点之间的时间间隔小于陀螺仪读数的最大停留时间，且陀螺仪读数大于s，进入第二个凸点状态

						T_BUMP2 = T_BUMP2 + (float) 0.05;            //第二个凸点的停留时间
						start_of_2nd_bump = 1;                   //开启第二个凸点状态
						if (Math.abs(v) > z) {
							BAD_TURN_2 = 1;
						}
						if (Math.abs(v) > Math.abs(max)) {          //计算第二个凸点状态中的最大偏航率
							max = v;
						}

					} else if (Math.abs(v) <= s && start_of_2nd_bump == 1) {    //算法已经进入第二个凸点状态，且已经结束 未必是凸点
						end2 = start;

						//有效的凸点
						if (Math.abs(max) > h && T_BUMP2 > t) {
							//5月4日 删除了距离的下限 只要第二个凸点结束就会提示变道

							ALL_OF_BUMP++;
							BAD_TURN += BAD_TURN_2;

							if (/*Math.abs(getdistance(speeds, vs, begin, end2)) <= 4 && */(max * max1 < 0)/* && Math.abs(getdistance(speeds, vs, begin, end2)) > 1.5*/) {    //&&(max*max1<0)
								//Toast.makeText(MainActivity.this, "Lane Change", Toast.LENGTH_SHORT).show();
								if (max > 0) {
									tv.setText("Change to a Right Lane");
									iv.setImageResource(images[5]);
									ltorTimes++;
									writeSDcard(message + "\t" + "Right Lane Change" + "\t" + angle_calculate(vs, begin, end2) + "\t" +
											getdistance(speeds, vs, begin, end2) + "\n");
								} else if (max < 0 ) {
									tv.setText("Change to a Left Lane");
									iv.setImageResource(images[4]);
									getdistance(speeds, vs, begin, end2);
									rtolTimes++;
									writeSDcard(message + "\t" + "Change to a Left Lane" + "\t" + angle_calculate(vs, begin, end2) + "\t" +
											getdistance(speeds, vs, begin, end2) + "\n");
								}
							}
							/*
							else if ((max * max1 < 0) && Math.abs(getdistance(speeds, vs, begin, end2)) > 4) {
								//Toast.makeText(MainActivity. this, "Curvy Road",Toast.LENGTH_SHORT).show();
								tv.setText("Curvy Road");
								curvyRoadTimes++;
								writeSDcard(message + "\t" + "Curvy Road" + "\t" + angle_calculate(vs, begin, end2) + "\t" +
										getdistance(speeds, vs, begin, end2) + "\n");
							}
							*/
							//两个反向凸点区分变道、在弯曲的道路上
							else if (max * max1 > 0) {
								if (angle_calculate(vs, begin, end2) <= -60 && angle_calculate(vs, begin, end2) >= -135) {
									//Toast.makeText(MainActivity.this, "Turn Right finished", Toast.LENGTH_SHORT).show();
									tv.setText("Turn Right finished");
									turnRightTimes++;
									iv.setImageResource(images[2]);
									writeSDcard(message + "\t" + "Turn Right finished" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\n");
								} else if (angle_calculate(vs, begin, end2) <= 135 && angle_calculate(vs, begin, end2) >= 60) {
									//Toast.makeText(MainActivity.this, "Turn Left finished", Toast.LENGTH_SHORT).show();
									tv.setText("Turn Left finished");
									turnLeftTimes++;
									iv.setImageResource(images[1]);
									writeSDcard(message + "\t" + "Turn Left finished" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\n");
								} else if (Math.abs(angle_calculate(vs, begin, end)) > 135) {
									//Toast.makeText(MainActivity.this, "Turn Back", Toast.LENGTH_SHORT).show();
									tv.setText("Turn Back");
									iv.setImageResource(images[3]);
									turnBackTimes++;
									writeSDcard(message + "\t" + "Turn Back" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\t" + getdistance(speeds, vs, begin, end) + "\n");
								}
							}
//        						if(Math.abs(getdistance(speeds,vs,begin,end2))>4){
//        							//Toast.makeText(MainActivity. this, "Curvy Road",Toast.LENGTH_SHORT).show();
//        							tv.setText("Curvy Road"+"\t"+angle_calculate(vs, begin, end2)+"\t"+getdistance(speeds,vs,begin,end2));
//        							curvyRoadTimes++;
//        							writeSDcard(message+"\t"+"Curvy Road"+"\t"+angle_calculate(vs, begin, end2)+"\t"+
//        									getdistance(speeds,vs,begin,end2)+"\n");
//        						}
						}
						//无效的凸点
						else {
							if (angle_calculate(vs, begin, end) <= -60 && angle_calculate(vs, begin, end2) >= -135) {
								//Toast.makeText(MainActivity.this, "Turn Right finished", Toast.LENGTH_SHORT).show();
								tv.setText("Turn Right finished");
								turnRightTimes++;
								iv.setImageResource(images[2]);
								writeSDcard(message + "\t" + "Turn Right finished" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\n");
							} else if (angle_calculate(vs, begin, end) <= 135 && angle_calculate(vs, begin, end2) >= 60) {
								//Toast.makeText(MainActivity.this, "Turn Left finished", Toast.LENGTH_SHORT).show();
								tv.setText("Turn Left finished");
								turnLeftTimes++;
								iv.setImageResource(images[1]);
								writeSDcard(message + "\t" + "Turn Left finished" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\n");
							} else if (Math.abs(angle_calculate(vs, begin, end)) > 135) {
								//Toast.makeText(MainActivity.this, "Turn Back", Toast.LENGTH_SHORT).show();
								tv.setText("Turn Back");
								iv.setImageResource(images[3]);
								turnBackTimes++;
								writeSDcard(message + "\t" + "Turn Back" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\t" + getdistance(speeds, vs, begin, end) + "\n");
							} else {
								iv.setImageResource(images[0]);
							}
						}

						T_BUMP = 0;
						T_BUMP2 = 0;
						state = No_Bump;
						max = 0;
						max1 = 0;
						T_dwell = 0;
						start_of_2nd_bump = 0;
						//恢复到原始状态
						BAD_TURN_1 = 0;
						BAD_TURN_2 = 0;

					} else if (T_dwell >= T_NEXT_DELAY) {
						end2 = start;

						//此时判定为只有一个凸点
						if (angle_calculate(vs, begin, end) < -60 && angle_calculate(vs, begin, end) >= -135) {
							//Toast.makeText(MainActivity.this, "Turn Right finished", Toast.LENGTH_SHORT).show();
							tv.setText("Turn Right finished");
							turnRightTimes++;
							iv.setImageResource(images[2]);
							writeSDcard(message + "\t" + "Turn Right finished" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\t" + "One_bump" + "\n");
        					/*
    						if(Math.abs(getdistance(algorithmKalmanFilter(),vs,begin,end2))>3.65&&Math.abs(getdistance(algorithmKalmanFilter(),vs,begin,end2))<=13.1){
    							Toast.makeText(MainActivity.this, "Turn Right finished", Toast.LENGTH_SHORT).show();
    							//tv.setText("Turn Right");
        					    //此动作应该伴随着转向灯信号，此时汽车应该行驶在外侧车道上
    						}
    						else if(Math.abs(getdistance(algorithmKalmanFilter(),vs,begin,end2))>13.1&&Math.abs(getdistance(algorithmKalmanFilter(),vs,begin,end2))<=19.2){
    							Toast.makeText(MainActivity.this, "Turn Right finished", Toast.LENGTH_SHORT).show();
    							//tv.setText("Turn Right");
    							//此动作应该伴随着转向灯信号，此时汽车行驶在内侧车道上
    						}
    						*/
						} else if (angle_calculate(vs, begin, end) <= 135 && angle_calculate(vs, begin, end) > 60) {
							//Toast.makeText(MainActivity.this, "Turn Left finished", Toast.LENGTH_SHORT).show();
							tv.setText("Turn Left finished");
							turnLeftTimes++;
							iv.setImageResource(images[1]);
							writeSDcard(message + "\t" + "Turn Left finished" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\t" + "One_bump" + "\n");
    						/*
    						if(Math.abs(getdistance(algorithmKalmanFilter(),vs,begin,end2))>19.2&&Math.abs(getdistance(algorithmKalmanFilter(),vs,begin,end2))<=21.64){
    							Toast.makeText(MainActivity.this, "Turn Left finished", Toast.LENGTH_SHORT).show();
    							//tv.setText("Turn Left");
        					    //此动作应该伴随着转向灯信号，此时汽车应该行驶在内侧车道
    						}
    						else if(Math.abs(getdistance(algorithmKalmanFilter(),vs,begin,end2))>21.64){
    							Toast.makeText(MainActivity.this, "Turn Left finished", Toast.LENGTH_SHORT).show();
    							//tv.setText("Turn Left");
    							//此动作应该伴随着转向灯信号，此时汽车行驶在外侧车道
    						}
    						*/
						} else if (Math.abs(angle_calculate(vs, begin, end)) > 135) {
							//Toast.makeText(MainActivity.this, "Turn Back", Toast.LENGTH_SHORT).show();
							tv.setText("Turn Back");
							iv.setImageResource(images[3]);
							writeSDcard(message + "\t" + "Turn Back" + "\t" + v1 + "\t" + moveAverage.getValue() + "\t" + angle_calculate(vs, begin, end) + "\t" + getdistance(speeds, vs, begin, end) + "\t" + "One_bump" + "\n");
							//此时汽车调头，应伴随转向灯信号
						} else {
							iv.setImageResource(images[0]);
						}

//    					else{
//    						if(Math.abs(getdistance(KalmanFilterSpeed,vs,begin,end2))<=3.65&&Math.abs(getdistance(KalmanFilterSpeed,vs,begin,end2))>2){                              
//        						//Toast.makeText(MainActivity.this, "Lane Change", Toast.LENGTH_SHORT).show();
//        						tv.setText("Lane Change"+getdistance(KalmanFilterSpeed,vs,begin,end2));
//        						writeSDcard(message+"\t"+"Lane Change"+"\n");
//        						}
//    						else if(Math.abs(getdistance(KalmanFilterSpeed,vs,begin,end2))>3.65){
//    							//Toast.makeText(MainActivity. this, "Curvy Road",Toast.LENGTH_SHORT).show();
//    							tv.setText("Curvy Road"+getdistance(KalmanFilterSpeed,vs,begin,end2));
//    							writeSDcard(message+"\t"+"Curvy Road"+"\n");
//    						}
////    						//Toast.makeText(MainActivity.this, "Lane Change or Curvy Road", Toast.LENGTH_SHORT);
////    						tv.setText("Lane Change or Curvy Road");
////    						writeSDcard(message+"\t"+"Lane Change or Curvy Road"+"\n");
						//   					}
						T_BUMP = 0;
						T_BUMP2 = 0;
						state = No_Bump;
						T_dwell = 0;
						max = 0;
						max1 = 0;
						start_of_2nd_bump = 0;
						BAD_TURN_1 = 0;
						BAD_TURN_2 = 0;

					}
				}

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv);

		bu = (Button) findViewById(R.id.make_end);

		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		Intent intent=getIntent();
		Bundle bd=intent.getExtras();
		ACC_OF_BRAKE=bd.getFloat("min");
		EMERGENCY=bd.getFloat("max");
		z=bd.getFloat("z");

		// 10 可自定义，代表平均“窗口”大小。。小刘博要改成偶数？
		moveAverage = new MovingAverage(15);
		moveAveragedir = new MovingAverage(5);
		moveAverageacc = new MovingAverage(3);

		iv = (ImageView) findViewById(R.id.image);

		iv.setImageResource(images[0]);

		bu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, Ending.class);
				Float percent = (float) (BAD_TURN / ALL_OF_BUMP);
				Bundle bundle = new Bundle();
				bundle.putFloat("percentage", percent);
				bundle.putFloat("left",turnLeftTimes);
				bundle.putFloat("right",turnRightTimes);
				bundle.putFloat("back",turnBackTimes);
				bundle.putFloat("ltor",ltorTimes);
				bundle.putFloat("rtol",rtolTimes);

				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});


		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0x111);

			}

		}, 0, 50);
		//此两个timer线程是否并行？http://www.cnblogs.com/chenssy/p/3788407.html
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				handler.sendEmptyMessage(0x110);
			}

		}, 0, 1000);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
		}
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.2f, new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
				}
				update(lm.getLastKnownLocation(provider));
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				update(null);
			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				update(location);
				speedFormer = speedLatter;
				speedLatter = speed;
			}
		});

		if(loc == null){
        	loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        Toast.makeText(this, "loc: "+ loc, Toast.LENGTH_SHORT).show();
        update(loc);

//        Thread thread = new Thread (new Runnable(){
//    		public void run(){
//    			while(true){
//    				try{
//    					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
//    		            String str=sdf.format(new Date());
//    		            DecimalFormat df = new DecimalFormat("#,##0.000");
//    		            String message = str;
////    					writeSDcard("时间"+message+"\n"+"当前线性加速度："+alinear1+"m/s2"+"当前传感器速度："+velocity1+"m/s"
////    				+"GPS速度："+speed+"m/s"+"KalmanFilter速度："+KalmanFilterSpeed+"m/s"
////    	        	+"当前角速度（弧度）："+v1+"rad/s"+"当前角度："+ang+"度"
////    				+"当前方位角："+ori+"度"+"当前水平距离："+distance+"m"+"\n"+"\n");
//    		            writeSDcard(message+"\t"+alinear1+"\t"+accelerator1+"\t"+velocity1+"\t"+velo1+"\t"
//    		    				+speed+"\t"+KalmanFilterSpeed+"\t"
//    		    	        	+v1+"\t"+ang+"\t"
//    		    				+ori+"\t"+distance+"\t"+"\n");
//    					Thread.sleep(100);
//    				}catch(InterruptedException e){
//    					e.printStackTrace();
//    				}
//    				System.out.println(Thread.currentThread());
//    			}
//    		}
//    	});
//    	thread.start();


    }
    public void update(Location loc){
    	if(loc!=null){
    		speed=(float)loc.getSpeed();//单位：m/s

    	}

    }



    protected void onResume(){
    	super.onResume();

    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_FASTEST);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),SensorManager.SENSOR_DELAY_UI);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_UI);
    }



    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	sm.unregisterListener(this);
    }

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//String message = new String();
		//String message2 = new String();
		DecimalFormat df = new DecimalFormat("#,##0.000");
		String message = new String();
		float[] values=event.values;
		double save = 0;
		//float angle1;

		int type=event.sensor.getType();
		switch(type){
		case Sensor.TYPE_ACCELEROMETER:
			accelerometerValues = values;
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			magneticFieldValues = values;
			values[0] = (float)Math.toDegrees(values[0]);
			break;
		case Sensor.TYPE_LINEAR_ACCELERATION:
            //z轴线性加速度
			accelerator = values[2];    //z-axis value of accelerometer 加速度计z轴读数

			//滑动滤波
			moveAverageacc.pushValue((float)accelerator);
			averacc=moveAverageacc.getValue();

			//高通滤波
//			final double alpha = 1;
//			if(flag == 0){
//				accnoise = 0.3;
//			}
//			else{
//				accnoise = alpha*accnoise + (1-alpha)*averacc;
//			}
//			
//			flag = flag+1;
//			hpaveracc = averacc-accnoise;//经过高通滤波的加速度
////			if(flag % 10 == 0){
//				tv4.setText("hpaveracc now is " + hpaveracc);
//			}

//			if(flag % 30 == 0){
//				Log.d("test", "averacc: " + averacc + " accnoise: " + accnoise + " hpaveracc: " + hpaveracc +"");
//				tv4.setText("hpaveracc now is " + hpaveracc);
//			}


			float csc=(float) Math.sin(calculateOrientation());
			if(csc==0){
				csc=(float) 0.1;
			}
			averalinear = averacc/csc;//acceleration in horizontal 水平方向的加速度

//			averalinear = hpaveracc/csc;//acceleration in horizontal 水平方向的加速度 
			if(Math.abs(averalinear)>=10){
				averalinear=save;
				}else{
					save=averalinear;
				}


			//DecimalFormat df1 = new DecimalFormat("#.00");
			DecimalFormat df1 = new DecimalFormat("#.00");

//			accnoise1 = Float.valueOf(df1.format(accnoise));
			accelerator1 = Double.valueOf(df1.format(accelerator));
			averacc1 = Double.valueOf(df1.format(averacc));
			hpaveracc1 = Double.valueOf(df1.format(hpaveracc));
			averalinear1 = Double.valueOf(df1.format(averalinear));
			//averacc1 = Double.valueOf(df1.format(averacc));


			break;
		case Sensor.TYPE_GYROSCOPE:
			v=(float) Math.sqrt(values[1]*values[1]+values[2]*values[2]);
			if((values[1]+values[2])<0){
				v=0-v;
			}

			DecimalFormat df2 = new DecimalFormat("#.00");
			v1 = Float.valueOf(df2.format(v));

			break;
		case Sensor.TYPE_ROTATION_VECTOR:
			float X = values[0];
			float Y = values[1];
			float Z = values[2];

			message += df.format(X) + "  ";
			message += df.format(Y) + "  ";
			message += df.format(Z) +  "\n";


			break;
		}
		//calculateOrientation();	


	}

	public double calculateOrientation(){
		float R[] = new float[9];
		double averori;
		double ori1;
		SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
		SensorManager.getOrientation(R, orientation);
		moveAveragedir.pushValue(orientation[1]);
		averori=moveAveragedir.getValue();
		DecimalFormat df = new DecimalFormat("#.00");
		ori1 = Double.valueOf(df.format(averori));
		return ori1;
//		tv4.setText("the orientation now is: "+orientation[0]);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public float algorithmKalmanFilter(){
		float x_10;
		float p_10;
		float K;
		float x_11 = 0;
		float p_11;
		float estimatedVelocity=0;
			x_10 = (float)(dataset.getX_00() + 0.05*averalinear1);
			p_10 = dataset.getP_00() + dataset.getQ();
			K = p_10/(p_10 + dataset.getR());
			x_11 = x_10 + K*(speed-x_10);
			dataset.setX_00(x_11);
			p_11 = p_10 - K*p_10;
			dataset.setP_00(p_11);
			//dataset.estimatedVelocity = x_11;
			DecimalFormat df = new DecimalFormat("#.00");
			estimatedVelocity = Float.valueOf(df.format(x_11));
			dataset.kAll = K;
			//Log.v("x11",""+x_11);
		return estimatedVelocity;
	}


}
