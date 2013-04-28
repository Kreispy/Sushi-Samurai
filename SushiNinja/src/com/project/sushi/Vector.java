/* COPYRIGHT (C) 2013 Angela M Yu, Ana Mei, Kevin Zhao, and Chris Chow. All Rights Reserved. */
package com.project.sushi;

public class Vector {

	private double x; 
	private double y; 
	private double x_unit; 
	private double y_unit; 
	

	public Vector (Point a, Point b){
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
		x_unit = b.getX() - a.getX();
		y_unit = b.getY() - a.getY();
	}
	
	
	public void makeUnit(){
		double length = magnitude();
		x_unit = x/length; 
		y_unit = y/length; 
	}
	
	public double magnitude(){
		if (x_unit != x || y_unit != y){
			return Math.sqrt(x_unit*x_unit + y_unit*y_unit);
		}
		else{
			return Math.sqrt(x*x + y*y);
		}
	}
	
	
	public double dot(Vector a){
		return a.x_unit*x_unit + a.y_unit*y_unit; 
	}
	
	// Takes a vector, times it by a magnitude, and then adds to a point
	public Point timesAdd(double m, Point a ){
		double x_temp = x_unit*m +a.getX(); 
		double y_temp = y_unit*m + a.getY(); 

		return new Point(x_temp, y_temp);
	}
	
}
