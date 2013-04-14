package com.project.sushi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import com.project.sushi.R;

public class Progression extends Activity {

	Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progression);
	}

	/*
	 * public void onSpeedRadioButtonClicked(View view) { // Is the button now
	 * checked? boolean checked = ((RadioButton) view).isChecked();
	 * 
	 * // Check which radio button was clicked switch(view.getId()) { case
	 * R.id.slow: if (checked) MainActivity.slow = true; break; case R.id.fast:
	 * if (checked) MainActivity.fast = true; break; } }
	 * 
	 * public void onSizeRadioButtonClicked(View view) { // Is the button now
	 * checked? boolean checked = ((RadioButton) view).isChecked();
	 * 
	 * // Check which radio button was clicked switch(view.getId()) { case
	 * R.id.small: if (checked) CuttingBoard.sushiSize -= 40; break; case
	 * R.id.large: if (checked) CuttingBoard.sushiSize += 25; break; } }
	 */

	// This is to start Level 1, our "tutorial" level, where you cannot fail.
	/*
	 * public void addListenerOnButton() {
	 * 
	 * final Context context = this;
	 * 
	 * button = (Button) findViewById(R.id.next);
	 * 
	 * button.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View arg0) {
	 * 
	 * Intent intent = new Intent(this, MainMenu.class); finish();
	 * startActivity(intent); }
	 * 
	 * });
	 * 
	 * 
	 * }
	 */
	public void onNextLevelClick(View v) {
		CuttingBoard.level++;
		MainActivity.reset = true;
		Intent intent = new Intent(this, MainActivity.class);
		finish();
		startActivity(intent);
		
	}
}