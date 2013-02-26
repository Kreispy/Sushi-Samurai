package com.hackamaroo.hw2;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.util.Log;

public class PaletteView extends View implements OnTouchListener{

	protected ShapeDrawable redS; 
	protected ShapeDrawable blueS; 
	protected ShapeDrawable greenS; 
	protected ShapeDrawable whiteS; 
	protected ShapeDrawable yellowS; 
	
	public Context cont; 
	
	private int color = Color.WHITE; 
	private int size; 
	
	static long time; 
	private long start; 
	
	private boolean incdt; 
	private boolean decdt; 
	
	private Timer tt;
	
	public PaletteView(Context context) {
		super(context);
		init();
		cont = context;
		time = 0; 
		start = System.nanoTime();
		incdt = false; 
		decdt = false; 
	}

	public PaletteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		cont = context; 
		time = 0; 
		start = System.nanoTime(); 

	}
	
	public PaletteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		cont = context;
		time = 0; 
		start = System.nanoTime(); 

	}
	
	protected void init(){
		this.setOnTouchListener(this);
		
		redS = new ShapeDrawable(new RectShape());
		blueS = new ShapeDrawable(new RectShape());
		greenS = new ShapeDrawable(new RectShape());
		whiteS = new ShapeDrawable(new RectShape());
		yellowS = new ShapeDrawable(new RectShape());
		
		redS.getPaint().setColor(Color.RED);
		blueS.getPaint().setColor(Color.BLUE);
		greenS.getPaint().setColor(Color.GREEN);
		whiteS.getPaint().setColor(Color.WHITE);
		yellowS.getPaint().setColor(Color.YELLOW);
		
		redS.setBounds(0, 10, 40, 50);
		yellowS.setBounds(40, 10, 80, 50);
		greenS.setBounds(80,10,120,50);
		blueS.setBounds(120,10,160,50);
		whiteS.setBounds(160,10,200,50);
		
		size = 6; 
		
		tt = new Timer();
	}
	
	@SuppressLint({ "DrawAllocation", "NewApi" })
	protected void onDraw(Canvas canvas){
		redS.draw(canvas);
		blueS.draw(canvas);
		whiteS.draw(canvas);
		yellowS.draw(canvas);
		greenS.draw(canvas);
		
		Paint p = new Paint(); 
		p.setColor(Color.WHITE);
		p.setTextAlign(Paint.Align.LEFT);
		p.setTypeface(Typeface.SANS_SERIF);
		p.setTextSize(20);
		
		canvas.drawText("thin", 210, 30, p);
		canvas.drawText("THICK", 250, 30, p);
		
		time += System.nanoTime() - start; 
		
		p.setTextSize(10);
		canvas.drawText(String.format("%02d:%02d", 
			    TimeUnit.NANOSECONDS.toMinutes(time),
			    TimeUnit.NANOSECONDS.toSeconds(time) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(time))), 320, 30, p);
		
		
		//invalidate();
	}

	public void drawTime(){
		
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if( event.getAction() == MotionEvent.ACTION_DOWN){
		float x = event.getX();
		float y = event.getY(); 
		
		Log.v("touch", x+"_"+y);
		
		if(redS.getBounds().contains((int)x, (int)y)){
			color = Color.RED; 
			incdt = true; 
		}
		else if(blueS.getBounds().contains((int)x, (int)y)){
			color = Color.BLUE; 
			decdt = true; 
		}
		else if(whiteS.getBounds().contains((int)x, (int)y)){
			color = Color.WHITE; 
		}
		else if(yellowS.getBounds().contains((int)x, (int)y)){
			color = Color.YELLOW; 
		}
		else if(greenS.getBounds().contains((int)x, (int)y)){
			color = Color.GREEN; 
		}
		else if(x>=211 && x<=244 && y>=15 && y<=30){
			size = 2; 
		}
		else if(x>251 && x <=307 && y>=15 && y<=30){
			size = 6; 
		}
		
		if(cont instanceof MainActivity){
			MainActivity main = (MainActivity)cont; 
			main.onPaletteClick(this);
			if(incdt){
				incdt = false; 
				main.dt+=100; 
				main.t.cancel(); 
				main.t = new Timer();
				main.scheduleTT();
				//Log.v("dt", Integer.toString(main.dt));
			}
			else if (decdt){
				decdt = false; 
				if(main.dt >=101){
					main.dt-=100;
				}
				main.t.cancel(); 
				main.t = new Timer();
				main.scheduleTT();
			}
		}
		}
		return true;
	}
	
	public int getColor(){
		return color;
	}
	
	public int getSize(){
		return size; 
	}
}
