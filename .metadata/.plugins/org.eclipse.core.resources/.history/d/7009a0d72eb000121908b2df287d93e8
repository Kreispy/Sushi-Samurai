package com.project.sushi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOver extends Activity {

	Button restart, exit;
	MusicPlayer mp = new MusicPlayer(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end);
		mp.play(R.raw.blackbutterflyending);

	}

	public void onRestartClick(View v) {
		mp.stopMusic();
		finish();
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);

	}

	public void onEndClick(View v) {
		mp.stopMusic();
		finish();

	}
	
}
