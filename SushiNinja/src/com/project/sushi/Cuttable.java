package com.project.sushi;

import java.util.HashMap;


public class Cuttable {
	
	private String name; 
	private String prevName; 
	private int image; 
	private HashMap<String, Integer> recipe; 
	private HashMap<String, Integer> stringDrawMap = new HashMap<String, Integer>(); 
	private boolean processed; 
	private HashMap<String, String> ingProcessMap = new HashMap<String, String>();
	
	
	public Cuttable(String n, int d, HashMap<String, Integer> r){
		name = n; 
		prevName = n; 
		image = d; 
		recipe = r; 
		processed = false; 
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
		stringDrawMap.put("ingredient1", R.drawable.sushi1);
		stringDrawMap.put("ingredient2", R.drawable.sushi2);
		stringDrawMap.put("sushi", R.drawable.sushi);
		stringDrawMap.put("gj", R.drawable.goodjob);
		stringDrawMap.put("nori", R.drawable.nori);
		stringDrawMap.put("rawseaweed", R.drawable.rawseaweed);
		stringDrawMap.put("sashimi", R.drawable.sashimi);
		stringDrawMap.put("livefish", R.drawable.livefish);
		
		image = stringDrawMap.get(n); 
		processed = false; 
		init();
	}
	
	private void init(){
		ingProcessMap.put("ingredient1", "ingredient2");
		ingProcessMap.put("ingredient2", "ingredient1");
		ingProcessMap.put("rawseaweed", "nori");
		ingProcessMap.put("livefish", "sashimi");
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
	
	public boolean getProcessed(){
		return processed; 
	}
	
	public boolean processIngredient(){
		if(ingProcessMap.containsKey(name) && !processed){
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
	

	
}
