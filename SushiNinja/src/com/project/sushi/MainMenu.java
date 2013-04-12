package com.project.sushi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.project.sushi.R;

public class MainMenu extends Activity {

	Button button;
	Button scorebutton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		addListenerOnButton();
		RunAnimations();
	}

	public void addListenerOnButton() {

		final Context context = this;

		button = (Button) findViewById(R.id.start);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(context, Options.class);
				finish();
				startActivity(intent);

			}

		});
		
		scorebutton = (Button) findViewById(R.id.score);

		scorebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(context, LeaderBoard.class);
				finish();
				startActivity(intent);

			}

		});
		
	}
	
	public void RunAnimations() {
	    Animation a = AnimationUtils.loadAnimation(this, R.anim.alpha);
	    a.reset();
	    ImageView logo = (ImageView) findViewById(R.id.logo);
	    logo.clearAnimation();
	    logo.startAnimation(a);
	 
	}

}