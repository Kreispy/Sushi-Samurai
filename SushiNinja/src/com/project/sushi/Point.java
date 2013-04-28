/* COPYRIGHT (C) 2013 Angela M Yu, Ana Mei, Kevin Zhao, and Chris Chow. All Rights Reserved. */
package com.project.sushi;

public class Point {
	private double x; 
	private double y; 
	private int color;
	private int size;
	private boolean first; 
	private boolean forWithinCollision; 
	
	public Point(float x1, float y1, int c, int s, boolean b){
		x = x1; 
		y = y1; 
		color = c;
		size = s; 
		first = b; 
		forWithinCollision = false; 
	}
	
	public Point(double d, double e){
		x = d; 
		y = e; 
		color = -1;
		size = -1; 
		first = false; 
		forWithinCollision = true; 
	}
	
	public double distance(Point b){
		return ( Math.sqrt((x-b.getX())*(x-b.getX()) + (y - b.getY())*(y-b.getY())));
	}


	public double getX(){
		return x; 
	}
	
	public double getY(){
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
	
	public boolean getCollisionFlag(){
		return forWithinCollision; 
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
