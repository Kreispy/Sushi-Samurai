package com.project.sushi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.view.View;
import android.view.View.OnClickListener;
import com.project.sushi.R;
 
public class Options extends Activity {
 
	Button button;
	Button backbutton;
	MusicPlayer mp = new MusicPlayer(this);
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		addListenerOnButton();
		
		final Dialog userName = new Dialog(this);
		userName.setContentView(R.layout.username);
		userName.setTitle("Please enter your name:");
		//make the user input dialog box pop up
		Button input = (Button) userName.findViewById(R.id.GObutton);
		
		input.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userName.dismiss();		
			}
		});
		userName.show();
		
	}

	
	public void addListenerOnButton() {
 
		final Context context = this;
 
		button = (Button) findViewById(R.id.slice);
 
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				mp.stopMusic();
			    Intent intent = new Intent(context, MainActivity.class);
			    finish();
			    			startActivity(intent);   
 
			}
 
		});
		
		backbutton = (Button) findViewById(R.id.back);
		 
		backbutton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				mp.stopMusic();
			    Intent intent = new Intent(context, MainMenu.class);
			    finish();
			    			startActivity(intent);   
 
			}
 
		});
 
	}
 
}