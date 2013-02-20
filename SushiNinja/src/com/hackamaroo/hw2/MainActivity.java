package com.hackamaroo.hw2;

//import java.util.Timer;
//import java.util.TimerTask;

import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity{

	private long start;
	private int ti;
	private int Vy;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		start = System.nanoTime(); 
		
		//Declare the timer
		Timer t = new Timer();
		//Set the schedule function and rate
		int inc = 0; 
		ti = 0;
		Vy = -25;
		
		t.scheduleAtFixedRate(new TimerTask() {

		    @Override
		    public void run() {
		        //Called each time when 1000 milliseconds (1 second) (the period parameter)
		    	//We must use this function in order to change the text view text
		    	runOnUiThread(new Runnable() {
		    		final int Vx = 5; 
		    
		    	    @Override
		    	    public void run() {
		    	        PaintBrushView pbv = (PaintBrushView) findViewById(R.id.pbView);
		    	      
		    	        PaintBrushView.incY -= Vx;
		    	        PaintBrushView.incX += Vy; //+ 1*ti;
		    	        //PaintBrushView.incY = -1*(Vy*ti + 1*ti*ti); 
		    	        Vy += 1;
		    	        ti += 1;
		    	        Log.v("Vy = ", Integer.toString(Vy));
		    	        Log.v("t = ", Integer.toString(ti));
		    	        Log.v(Integer.toString(PaintBrushView.incX), Integer.toString(PaintBrushView.incY));		    	        
		    	        pbv.invalidate();
		    	        
		    	       
		    	    }
		    	     
		    	});
		    }
		         
		},
		//Set how long before to start calling the TimerTask (in milliseconds)
		0,
		//Set the amount of time between each execution (in milliseconds)
		1000);
	}
	
	
	public void onClearButtonClick(View view){
		PaintBrushView pbv = (PaintBrushView) findViewById(R.id.pbView);
		pbv.clearPoints();
		pbv.invalidate();
	}

	public void onPaletteClick(View view){
		PaintBrushView pbv = (PaintBrushView) findViewById(R.id.pbView);
		PaletteView pv = (PaletteView) findViewById(R.id.pView);
		pbv.setColor(pv.getColor()); 
		pbv.setSize(pv.getSize()); 
		
	}


	
}
