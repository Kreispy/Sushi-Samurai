package com.project.sushi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;


public class Cuttable {
	
	private String name; 
	private String prevName; 
	private int image;
	private int sound;
	private HashMap<String, Integer> recipe; 
	private HashMap<String, Integer> stringDrawMap = new HashMap<String, Integer>(); 
	private boolean processed; 
	private HashMap<String, String> ingProcessMap = new HashMap<String, String>();
	private HashMap<String, Integer> soundMap = new HashMap<String, Integer>();
	
	public int countMade; 
	
	public Cuttable(String n, int d, HashMap<String, Integer> r){
		name = n;    
		prevName = n; 
		image = d; 
		recipe = r; 
		processed = true; 
		init();
	}
	
	public Cuttable(String n, int d){
		name = n; 
		prevName = n; 
		image = d; 
		recipe = new HashMap<String, Integer>(); 
		processed = false; 
		init();
	}
	
	public Cuttable(String n){
		name = n; 
		prevName = n; 
		recipe = new HashMap<String, Integer>(); 
		stringDrawMap.put("avocado", R.drawable.avocado);
		stringDrawMap.put("avocadocut", R.drawable.avocadocut);
		stringDrawMap.put("shrimp", R.drawable.shrimp);
		stringDrawMap.put("tempura", R.drawable.tempura);
		stringDrawMap.put("nori", R.drawable.nori);
		stringDrawMap.put("rawseaweed", R.drawable.seaweed);
		stringDrawMap.put("sashimi", R.drawable.sashimi);
		stringDrawMap.put("livefish", R.drawable.livefish);
		stringDrawMap.put("crab", R.drawable.crab);
		stringDrawMap.put("crabmeat", R.drawable.crabmeat);
		stringDrawMap.put("ricebag", R.drawable.ricebag);
		stringDrawMap.put("cookedrice", R.drawable.cookedrice);
		
		soundMap.put("rawseaweed", R.raw.seaweed);
		soundMap.put("avocado", R.raw.blade4);
		soundMap.put("livefish", R.raw.fish);
		soundMap.put("shrimp", R.raw.shrimp);
		soundMap.put("crab", R.raw.crab);
		soundMap.put("ricebag", R.raw.rice);
		soundMap.put("sushi", R.raw.bladecut);
		soundMap.put("sushi1", R.raw.bladecut);
		soundMap.put("sashimisushi", R.raw.bladecut);
		soundMap.put("tempurasushi", R.raw.bladecut);
		
		image = stringDrawMap.get(n); 
		
		if(soundMap.containsKey(n)){
			sound = soundMap.get(n);
		}
		else{
			sound = -1;
		}
		
		
		processed = false; 
		init();
	}
	
	private void init(){
		ingProcessMap.put("shrimp", "tempura");
		ingProcessMap.put("avocado", "avocadocut");
		ingProcessMap.put("rawseaweed", "nori");
		ingProcessMap.put("livefish", "sashimi");
		ingProcessMap.put("crab", "crabmeat");
		ingProcessMap.put("ricebag", "cookedrice");
		countMade = 0; 
	}
	
	public boolean hasRecipe(){
		if(recipe.size() == 0){
			return false; 
		}
		return true; 
	}
	
	public String getName(){
		return name; 
	}

	public HashMap<String, Integer> getRecipe(){
		return recipe; 
	}
	
	public int getImage(){
		return image; 
	}
	
	public int getSound(){
		return sound;
	}
	
	public boolean getProcessed(){
		return processed; 
	}
	
	public boolean processIngredient(){
		if(ingProcessMap.containsKey(name) && !processed && !hasRecipe()){
			processed = true;
			prevName = name; 
			image = stringDrawMap.get(ingProcessMap.get(name));
			name = ingProcessMap.get(name);
			return true; 
		}
		processed = false; 
		return false; 
	}
	
	public boolean needsProcessing(){
		return !processed; 
	}
	
	public String getPrevName(){
		return prevName; 
	}
	

	public boolean hasSound(String n){
		return (soundMap.containsKey(n));
	}
	
	public boolean checkRecipeMade(HashMap<String, Integer> ingredients){
		Iterator<Entry<String, Integer>> it = (recipe).entrySet().iterator(); 
		while(it.hasNext()){
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next(); 
			if(ingredients.get(pairs.getKey()) < pairs.getValue()){
				return false; 
			}
		}
		countMade++; 
		return true; 
	}
	
	public ArrayList<Cuttable> getMissingIng(HashMap<String, Integer> ingredients){
		ArrayList<Cuttable> toBeSpawn = new ArrayList<Cuttable>(); 
		Iterator<Entry<String, Integer>> it = (recipe).entrySet().iterator(); 
		while(it.hasNext()){
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next(); 
			Log.v(pairs.getKey(), "pairs key");
			Log.v(Integer.toString(pairs.getValue()), "pairs value");
			
			Log.v(Integer.toString(ingredients.get(pairs.getKey())), "ing value");
			if(ingredients.get(pairs.getKey()) < pairs.getValue()){
				toBeSpawn.add(new Cuttable(pairs.getKey())); //adds missing recipe peices in 
			}
		}
		return toBeSpawn; 
	}
	
	public void decrementCountMade(){
		countMade--;
	}
	
}
