package com.hackamaroo.hw2;

public class Vector {

	private double x; 
	private double y; 
	private Point start; 
	private Point end; 
	private double x_unit; 
	private double y_unit; 
	

	public Vector (Point a, Point b){
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
		start = a; 
		end = b; 
		x_unit = b.getX() - a.getX();
		y_unit = b.getY() - a.getY();
	}
	
	
	public void makeUnit(){
		x_unit = x/magnitude(); 
		y_unit = y/magnitude(); 
	}
	
	public double magnitude(){
		return Math.sqrt(x*x + y*y);
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
	
	public Point add(Point a){
		return new Point(x_unit+a.getX(), y_unit+a.getY());
	}
}