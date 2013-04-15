package com.project.sushi;

//import java.util.Timer;
//import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;

import com.project.sushi.R;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity{

	protected long start;
	protected static int ti;
	protected static int Vy;
	protected static int Vx;
	protected static boolean slow = false;
	protected static boolean fast = false;
	static int dt; 
	Timer t; 
	TimerTask tt;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		start = System.nanoTime(); 
		
		//Declare the timer
		t = new Timer();
		//Set the schedule function and rate
		//int inc = 0; 
		ti = 0;
		if(slow){
			Vy = -100;
			dt = 50;
		}
		else if(fast){
			Vy = -150;
			dt = 30;
		}
		else{
			Vy = -125;
			dt =  40;
		}
		
		scheduleTT();  
	}
	
	public void scheduleTT(){
		 
		t.scheduleAtFixedRate(new TimerTask() {
		    @Override
		    public void run() {
		        //Called each time when 1000 milliseconds (1 second) (the period parameter)
		    	//We must use this function in order to change the text view text
		    	runOnUiThread(new Runnable() {
		    		
		    	    @Override
		    	    public void run() {
		    	    	
		    	        CuttingBoard cb = (CuttingBoard) findViewById(R.id.pbView);
		    	        cb.scoreboard = (TextView) findViewById(R.id.score);
		    	        cb.feedback = (ImageView) findViewById(R.id.feedback);
		    	        
		    	        
		    	        cb.increaseY(Vy);
		 
		    	        //ball will either go left or right
		    	        if(cb.isLeft()){
		    	        	cb.decreaseX(Vx);
		    	        } 
		    	        else{
		    	        	cb.increaseX(Vx);
		    	        }
		    	          	        
		    	        /*CuttingBoard.incY -= Vx;
		    	        CuttingBoard.incX += Vy; //+ 1*ti;
		    	        */
		    	        //CuttingBoard.incY = -1*(Vy*ti + 1*ti*ti); 
		    	        Vy += 1;
		    	        ti += 5;
		    	        //Log.v("Vy = ", Integer.toString(Vy));
		    	        //Log.v("t = ", Integer.toString(ti));
		    	        //Log.v(Integer.toString(CuttingBoard.incX), Integer.toString(CuttingBoard.incY));		    	        
		    	        //Log.v("dt", Integer.toString(dt));
		    	        cb.invalidate();
		    	        
		    	       
		    	    }
		    	     
		    	});
		    }
		         
		},
		//Set how long before to start calling the TimerTask (in milliseconds)
		0,
		//Set the amount of time between each execution (in milliseconds)
		dt);
	}
	
	/*
	public void onClearButtonClick(View view){
		CuttingBoard pbv = (CuttingBoard) findViewById(R.id.pbView);
		pbv.clearPoints();
		pbv.invalidate();
	}
	*/
/*
 * Used for self- 
 *
	public void onSlowVxButtonClick(View view){
		Vx = (Vx / 2);
	}
	public void onFastVxButtonClick(View view){
		Vx = (Vx * 2);
	}
	public void onSlowVyButtonClick(View view){
		Vy = (Vy / 2);
	}
	public void onFastVyButtonClick(View view){
		Vy = (Vy * 2);
	}
*/

	
}
