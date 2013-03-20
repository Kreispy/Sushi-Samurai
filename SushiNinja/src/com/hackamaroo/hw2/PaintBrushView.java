package com.hackamaroo.hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PaintBrushView extends View implements OnTouchListener{

	protected int drawColor = Color.WHITE; 
	protected int size = 7; 
	public List<Point> pdrawn = new ArrayList<Point>();
	public Stack<Point> asdf = new Stack<Point>(); 
	public Path path;
	public Drawable circle;
	public Drawable[] sushi_images;

	private int startY = 0;
	private int startX = 0;
	private int incX = 0; 
	private int incY = 0; 
	private int offset = 50;
	
	private int startTi = 5;
	private Random random = new Random();
	private Random sushiRand = new Random();
	Collision col = new Collision(); 
	boolean checkCollide = false; 
	boolean addPoint = false; 
	boolean left = true;

	public PaintBrushView(Context context) {
		super(context);
		init();
	}
	
	public PaintBrushView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PaintBrushView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	protected void init(){
		this.setOnTouchListener(this);
		Resources res = getResources();
		sushi_images = new Drawable[]
				{res.getDrawable(R.drawable.sushi),res.getDrawable(R.drawable.sushi1), res.getDrawable(R.drawable.sushi2)};
		int image = sushiRand.nextInt(3);
		
		circle = sushi_images[image];
		//circle.setBounds(230, 220, 230+80, 220+80);
		startY = random.nextInt(500)+(getWidth()/2); //getHeight()/2; 
		startX = getHeight();
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas){
		Log.v("pdrawnsize", Integer.toString(pdrawn.size()));
		//Check for collisions
		if(pdrawn.size() >=2 && addPoint){
			//if(!pdrawn.get(pdrawn.size()-1).getFirst() && !pdrawn.get(pdrawn.size()-2).getFirst()){
				checkCollide = col.checkCollisionAY(pdrawn.get(pdrawn.size()-2), pdrawn.get(pdrawn.size()-1), startX+incX+offset, startY+incY+offset, offset);
				Log.v("checkCollide", Boolean.toString(checkCollide));
				addPoint = false; 
			//}
		}
		Paint p = new Paint();
		circle.draw(canvas);
		//inc +=10; 
		
		// Increase the radius a bit
		//circle.setBounds(startY+incY, startX+incX, startY+50+incY, startX+50+incX);
		circle.setBounds(startX+incX, startY+incY, startX+offset+incX, startY+offset+incY);
		
		
		// Reset the coordinates of the new object if it goes off the screen
		// Note: This is the same instance
		if (startY+incY < 0 || startX+incX > getWidth() || startY+incY > getHeight() || startX+incX < 0 || checkCollide) {
			startY = getHeight();
				//random.nextInt(150)+(getWidth()/2)-100; //800; //getHeight();
			
			if(random.nextBoolean()){
				left = true;
				startX = random.nextInt(getWidth()/4);
			}
			else{
				left = false;
				startX = getWidth() - random.nextInt(getWidth()/4);
			}
			
			Log.v("startx", Integer.toString(getWidth()));
			Log.v("starty", Integer.toString(getHeight()));
			MainActivity.Vy = -(getHeight()/25 + random.nextInt(getHeight()/50)); // reset to default value
			MainActivity.Vx = -(getWidth()/100 + random.nextInt(getWidth()/100));
			MainActivity.ti = startTi; // reset time to zero
			incX = 0; // reset everything
			incY = 0; // reset everything
			int image = sushiRand.nextInt(3);
		    circle = sushi_images[image];
			checkCollide = false; 
			
			//pdrawn.clear();
			invalidate(); 
		}

		for(int i = 0; i < pdrawn.size(); i++){
			Point pt = pdrawn.get(i);
			p.setColor(pt.getColor());
			//canvas.drawCircle(pt.getX(),pt.getY(),pt.getSize(),p);
			if(!pt.getFirst() && i != 0){
				canvas.drawLine( pdrawn.get(i-1).getX(), pdrawn.get(i-1).getY(), pt.getX(), pt.getY(),p);
			}
			else{
				p.setStrokeWidth(pt.getSize());
			}
			
		}
		
	}	
	
	public boolean onTouch(View view, MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN ) {
			pdrawn.clear();
			Point newP = new Point(event.getX(),event.getY(),drawColor,size, true); 
			pdrawn.add(newP);
			Log.v(Float.toString(event.getX()), Float.toString(event.getY()));
			addPoint = true; 
			invalidate();
		}
		else if (event.getAction() != MotionEvent.ACTION_UP){
			Point newP = new Point(event.getX(),event.getY(),drawColor,size, false); 
			pdrawn.add(newP);
			//Log.v("tag", circle.getBounds().toString()); 
			addPoint = true; 
			invalidate();
		}
		return true; 
		
	}
	
	public void clearPoints(){
		pdrawn = new ArrayList<Point>(); 
	}
	
	public void setColor(int a){
		drawColor = a; 
	}
	
	public void setSize(int a){
		size = a; 
	}
	
	public void increaseX(int value){
		incX += value;
	}
	
	public void decreaseX(int value){
		incX -= value;
	}
	
	public void increaseY(int value){
		incY += value;
	}
	
	public boolean isLeft(){
		return left;
	}
	
	public void biggerSushi(){
		offset += 10;
		invalidate();
	}
	public void smallerSushi(){
		if(offset > 0){
			offset -= 10;
		invalidate();
		}
	}
	
	
}
