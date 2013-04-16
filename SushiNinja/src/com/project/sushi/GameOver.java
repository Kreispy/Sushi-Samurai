package com.project.sushi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOver extends Activity {

	Button restart, exit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end);

	}

	public void onRestartClick(View v) {
		
		Intent intent = new Intent(this, MainMenu.class);
		finish();
		startActivity(intent);

	}

	public void onEndClick(View v) {
		//finish();
		System.exit(0);

	}
	
}
