package com.example.vsense;

public class DataSet {
	
	public float kAll;
//	public float estimatedVelocity;
	private float x_00 = 0;
	public static float Q = (float) 1;
	public static float P_00 = 1;
	public static float R = (float) 100.0;
	
	public float getkAll() {
		return kAll;
	}
	
	public void setkAll(float kAll){
		this.kAll = kAll;
	}
	
//	public float getEstimatedVelocity(){
//		return estimatedVelocity;
//	}
//	
//	public void setEstimatedVelocity(float estimatedVelocity) {
//		this.estimatedVelocity = estimatedVelocity;
//	}
//	
	public float getX_00(){
		return x_00;
	}
	
	public void setX_00(float x_00) {
		this.x_00 = x_00;
	}
	
	public float getQ(){
		return Q;
	}
	
	public void setQ(float q) {
		Q = q;
	}
	
	public float getP_00(){
		return P_00;
	}
	
	public void setP_00(float p_00){
		P_00 = p_00;
	}
	
	public float getR(){
		return R;
	}
	
	public void setR(float r){
		R = r;
	}

}
