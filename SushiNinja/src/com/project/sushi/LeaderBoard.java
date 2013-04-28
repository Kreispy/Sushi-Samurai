/* COPYRIGHT (C) 2013 Angela M Yu, Ana Mei, Kevin Zhao, and Chris Chow. All Rights Reserved. */
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
	Button statsButton;
	static String PREFERENCE_KEY = "user_score_string";
	static String arrayName = "high_score_array";
	TextView myText;
	SharedPreferences settings;
	static List<Integer> scoresList;
	MusicPlayer mp = new MusicPlayer(this);
	static String username = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard);
		addListenerOnButton();
		settings = getSharedPreferences(PREFERENCE_KEY, 0);
		
		updateDisplayString(username);
	}
	
	public void updateDisplayString(String username) {
		List<Integer> scoreList = loadList(arrayName, this.getApplicationContext());
		String displayString = "";
		
		myText = (TextView)findViewById(R.id.scoreboard);
		
		displayString += "\n";
		displayString += "\n";
		for (int i=0; i < scoreList.size(); i++){
			displayString += "Top Samurai " + (i+1) + " : " + scoreList.get(i) + "\n";
		}
		this.scoresList = scoreList;
		
		myText.setText(displayString);
	}
	
	public void setUsernameUpdateLeaderBoard(String username) {
		LeaderBoard.username = username;
		
		updateDisplayString(username);
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
	        list.add(prefs.getInt(arrayName + "_" + i, 0)); // returns 0 if cannot be found
	    Collections.sort(list);
	    Collections.reverse(list); // reverse for greatest to least
	    return list;  
	}
	
	public static boolean saveRecentInt(String metricType, Context mContext, int metric){
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0);  
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt(username + "_" + metricType + "_recent", metric);  
	    return editor.commit();
	}
	
	public static int loadRecentInt(String metricType, Context mContext){
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0); 
		return prefs.getInt(username + "_" + metricType + "_recent", 0);
	}
	
	public static boolean saveTotalInt(String metricType, Context mContext, int metric){
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0);  
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt(username + "_" + metricType + "_total", metric);  
	    return editor.commit();
	}
	
	public static int loadTotalInt(String metricType, Context mContext){
		SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0); 
		return prefs.getInt(username + "_" + metricType + "_total", 0);
	}
	
	public void addListenerOnButton() {
		 
		final Context context = this;
 
		button = (Button) findViewById(R.id.back);
 
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				mp.stopMusic();
			    Intent intent = new Intent(context, MainMenu.class);
			    finish();
			    			startActivity(intent);   
 
			}
 
		});
		
		statsButton = (Button) findViewById(R.id.stats);
		
		statsButton.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
			    Intent intent = new Intent(context, StatBoard.class);
			    finish();
			    			startActivity(intent);   
 
			}
 
		});
 
	}
}
