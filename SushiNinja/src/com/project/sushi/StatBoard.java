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
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import com.project.sushi.R;

public class StatBoard extends Activity{
	
	Button button;
	static String PREFERENCE_KEY = "user_statistics";
	TextView myText;
	SharedPreferences settings;
	static List<Integer> scoresList;
	MusicPlayer mp = new MusicPlayer(this);
	static String username = ""; // Default is empty string
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statboard);
		addListenerOnButton();
		settings = getSharedPreferences(PREFERENCE_KEY, 0);
		
		updateDisplayString(username);
	}
	
	public void setUsernameUpdateStatistics(String username_input) {
		username = username_input;
		updateDisplayString(username);
	}
	
	public void updateDisplayString(String username){
		String displayString = "";
		
		myText = (TextView)findViewById(R.id.stat_board);
		
		displayString += "\n";
		displayString += "\n";
		
		int totalSessions = 0;
		int totalScore, totalCut, totalGenerated, totalPlaytime, totalUncuttable;
		totalScore = totalCut = totalGenerated = totalPlaytime = totalUncuttable = 0;
		int averageScore, averageCut, averageGenerated, averagePlaytime;
		int recentScore, recentCut, recentGenerated, recentPlaytime, recentUncuttable;
		recentScore = recentCut = recentGenerated = recentPlaytime = recentUncuttable = 0;
		int averagePercent, recentPercent;
		recentPercent = 0;
		
		try {
			totalSessions = LeaderBoard.loadTotalInt("TOTAL_SESSIONS", this.getApplicationContext());
			
			totalScore = LeaderBoard.loadTotalInt("TOTAL_SCORE", this.getApplicationContext());
			totalCut = LeaderBoard.loadTotalInt("TOTAL_SUSHI_CUT", this.getApplicationContext());
			totalUncuttable = LeaderBoard.loadTotalInt("TOTAL_UNCUTTABLE_SUSHI_CUT", this.getApplicationContext());
			totalGenerated = LeaderBoard.loadTotalInt("TOTAL_SUSHI_GENERATED", this.getApplicationContext());
			totalPlaytime = LeaderBoard.loadTotalInt("TOTAL_PLAYTIME", this.getApplicationContext()); // For seconds
			
			recentScore = LeaderBoard.loadRecentInt("RECENT_SCORE", this.getApplicationContext());
			recentCut = LeaderBoard.loadRecentInt("RECENT_SUSHI_CUT", this.getApplicationContext());
			recentUncuttable = LeaderBoard.loadRecentInt("RECENT_UNCUTTABLE_SUSHI_GENERATED", this.getApplicationContext()); 
			recentGenerated = LeaderBoard.loadRecentInt("RECENT_SUSHI_GENERATED", this.getApplicationContext());
			recentPercent = LeaderBoard.loadRecentInt("RECENT_PLAYTIME", this.getApplicationContext()); // For seconds
			
			averageScore = totalScore / totalSessions;
			averageCut = totalCut / totalSessions;
			averageGenerated = totalGenerated / totalSessions;
			averagePlaytime = totalPlaytime / totalSessions;
			
			averagePercent = (totalCut * 100) / (totalGenerated  - totalUncuttable);
			recentPercent = (recentCut * 100) / (recentGenerated - recentUncuttable);
		}
		catch (Exception e) { // Avoid divide by zero error
			averageScore = 0; 
			averageCut = 0; 
			averageGenerated = 0; 
			averagePlaytime = 0;
			averagePercent = 0;
		}
		
		displayString += "Samurai " + StatBoard.username + "'s Progress\n\n";
		displayString += "Total Score: " + totalScore + "\n"
		+ "Average Score: " + averageScore + "\n" 
		+ "Recent Score: " + recentScore + "\n";
		displayString += "Total Cut: " + totalCut + "\n" + 
		"Average Cut: " + averageCut + "\n" +
		"Recent Cut: " + recentCut + "\n";
		displayString += "Total Generated: " + totalGenerated + "\n" +
		"Average Generated: " + averageGenerated + "\n" + "" +
		"Recent Generated: " + recentGenerated + "\n";
		// displayString += "TP: " + totalPlaytime + " , AP: " 
		// 		+ averagePlaytime + " , MRP: " + recentPlaytime + "\n";
		displayString += "Average Percent: " + averagePercent + "%" + "\n"
		+ "Recent Percent: " + recentPercent + "%" + "\n";
		displayString += "Sessions Played: " + totalSessions;
		
		myText.setText(displayString);
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
	    editor.putInt(username + "_" + arrayName +"_size", list.size());  
	    for(int i = 0;i < list.size(); i++)  
	        editor.putInt(username + "_"+ arrayName + "_" + i, list.get(i));  
	    return editor.commit();  
	}
	
	public List<Integer> loadList(String arrayName, Context mContext) {  
	    SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCE_KEY, 0);  
	    int size = prefs.getInt(username + "_" + arrayName + "_size", 5);  
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
		
		button = (Button) findViewById(R.id.back_stat);
 
		button.setOnClickListener(new OnClickListener() {
 
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
