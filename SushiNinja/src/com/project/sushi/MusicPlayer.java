/* COPYRIGHT (C) 2013 Angela M Yu, Ana Mei, Kevin Zhao, and Chris Chow. All Rights Reserved. */
package com.project.sushi;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {
	private static MediaPlayer mp;
    private static Context context;
    
	public MusicPlayer(Context context){
		this.context = context;
		
	}
	
	public void play(int song){
		mp = MediaPlayer.create(context,song);
		mp.start();
	}
	
	public MediaPlayer getMp(){
		return mp;
	}
	
	public void stopMusic(){
		if(mp!= null && mp.isPlaying()){
			mp.stop();
		}
		mp.release();
	}
	
	
}