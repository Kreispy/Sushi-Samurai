package com.project.sushi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import com.project.sushi.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.view.View;
import android.view.View.OnClickListener;
import com.project.sushi.R;

public class LeaderBoard extends Activity{
	
	Button button;
	static String PREFERENCE_KEY = "user_score_string";
	static String arrayName = "high_score_array";
	TextView myText;
	SharedPreferences settings;
	static List<Integer> scoresList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard);
		addListenerOnButton();
		settings = getSharedPreferences(PREFERENCE_KEY, 0);
		
		
		LinearLayout lView = new LinearLayout(this);

		// TEST
		/*
		List<Integer> testList = new ArrayList<Integer>();
		testList.add(1000);
		testList.add(0);
		testList.add(0);
		testList.add(0);
		testList.add(0);
		Collections.sort(testList);
		saveList(testList, arrayName, this.getApplicationContext());
		// saveArray(testArray, arrayName, this.getApplicationContext());
		 
		*/
		// TEST
		
		List<Integer> scoreList = loadList(arrayName, this.getApplicationContext());
		String displayString = "";
		
		myText = new TextView(this);
		
		
		for (int i=0; i < scoreList.size(); i++){
			displayString += "DefaultUser" + (i+1) + " : " + scoreList.get(i) + "\n";
		}
		this.scoresList = scoreList;
		
		myText.setText(displayString);

		lView.addView(myText);

		setContentView(lView);
	}
	
	public SharedPreferences getSharedPreferences(){
		return settings;
	}
	
	public static List<Integer> getScoresList(){
		return scoresList;
	}
	
	public static void setScoresList(List<Integer> scores){
		scoresList = scores;
	}
	
	public static boolean saveList(List<Integer> list, String arrayName, Context mContext) {   
	    SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0);  
	    SharedPreferences.Editor editor = prefs.edit();  
	    editor.putInt(arrayName +"_size", list.size());  
	    for(int i = 0;i < list.size(); i++)  
	        editor.putInt(arrayName + "_" + i, list.get(i));  
	    return editor.commit();  
	}
	
	
	public List<Integer> loadList(String arrayName, Context mContext) {  
	    SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0);  
	    int size = prefs.getInt(arrayName + "_size", 5);  
	    List<Integer> list = new ArrayList<Integer>();  
	    for(int i = 0; i < size; i++)  
	        list.add(prefs.getInt(arrayName + "_" + i, 0)); // returns -1 if cannot be found
	    Collections.sort(list);
	    Collections.reverse(list); // reverse for greatest to least
	    return list;  
	}
	
	public void addListenerOnButton() {
		 
		final Context context = this;
 
		button = (Button) findViewById(R.id.back);
 
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, MainMenu.class);
			    finish();
			    			startActivity(intent);   
 
			}
 
		});
 
	}
}
