package com.hackamaroo.hw2;

public class Point {
	private float x; 
	private float y; 
	private int color;
	private int size;
	private boolean first; 
	
	public Point(float x1, float y1, int c, int s, boolean b){
		x = x1; 
		y = y1; 
		color = c;
		size = s; 
		first = b; 
	}
	

	public float getX(){
		return x; 
	}
	
	public float getY(){
		return y; 
	}
	
	public int getColor(){
		return color; 
	}
	
	public int getSize(){
		return size; 
	}
	
	public boolean getFirst(){
		return first; 
	}
	
	public void setX(float a){
		x=a; 
	}
	
	public void setY(float a){
		y=a; 
	}
	
	public void setColor(int a){
		color=a; 
	}
	
	public void setSize(int a){
		size=a; 
	}
}
