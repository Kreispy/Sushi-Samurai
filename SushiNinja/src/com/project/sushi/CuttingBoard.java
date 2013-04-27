package com.project.sushi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.sushi.R;

public class CuttingBoard extends View implements OnTouchListener{

	protected int drawColor = Color.WHITE; 
	protected int size = 7; 
	
	public List<Point> pdrawn = new ArrayList<Point>();
	public Stack<Point> asdf = new Stack<Point>(); 
	public Path path;
	public Drawable circle;
	public TextView scoreboard;
	public ImageView feedback;
	public ImageView recipesBack;
	public TextView totalCut;
	public TextView recipesCreated;
	public Button viewRecipes;
	public MediaPlayer playSound;
	public MediaPlayer mpFail = MediaPlayer.create(getContext(), R.raw.miss);
	private Drawable[] rankingImages; 
	private String[] rankingTitles;
	
	//parameters to manipulate between levels
	private int currentLevel = 1;
	private int sushiSliced = 0;//for a game over / end screen.
	private int sushiDropped = 0;
	private int offset = 150;
	
	private int startY = 0;
	private int startX = 0;
	private int incX = 0; 
	private int incY = 0; 
	
	private int startTi = 5;
	private Random random = new Random();
	Collision col = new Collision(); 
	boolean checkCollide = false; 
	boolean addPoint = false; 
	boolean left = true;
	boolean gameOver = false;
	
	private int sushiGeneratedStat = 0;
	private int sushiSlicedStat = 0; 
	
	public double totalScore; 
	private HashMap<String, Integer> ingredients = new HashMap<String, Integer>(); //contains what you have cut and haven't changed into recipes
	private ArrayList<Cuttable> recipes ;  //recipes for sushi
	private Random ingRand = new Random(); //random generator to be used with toBeSpawn
	private Cuttable currentCuttable; //current object flying

	
	private int sushiDroppedTotal; 
	int Vx = 0;
	int Vy = 0; 
	
	boolean first = true; 
	boolean positiveRecipe = false; 
	
	
	private int recipesMadeGoal; 
	private int recipesMade = 0; 
	
	private boolean nextLevelFlag; 
	
	//this probably don't need to be up here?
	private ArrayList<Cuttable> toBeSpawn; //array containing missing ingredients for recipes

	public CuttingBoard(Context context) {
		super(context);
		init();
	}
	
	public CuttingBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public CuttingBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	protected void init(){
		this.currentCuttable =  new Cuttable("ricebag");
		toBeSpawn = new ArrayList<Cuttable>();
		recipes =  new ArrayList<Cuttable>(); 
		this.setOnTouchListener(this);
		Resources res = getResources();

		circle = res.getDrawable(currentCuttable.getImage());
		if(currentCuttable.getSound() >= 0){
			playSound = MediaPlayer.create(getContext(), currentCuttable.getSound());
		}
	
		startY = random.nextInt(500)+(getWidth()/2); //getHeight()/2; 
		startX = getHeight();
		
		recipesMadeGoal = 2;
		sushiDroppedTotal = 0;
		//add in all possible ingredients with 0
		ingredients.put("shrimp", 0);
		ingredients.put("tempura", 0);
		ingredients.put("ricebag", 0);
		ingredients.put("cookedrice", 0);
		
		ingredients.put("nori", 0);
		ingredients.put("sashimi", 0);
		ingredients.put("rawseaweed", 0);
		ingredients.put("livefish", 0);
		ingredients.put("crab", 0);
		ingredients.put("crabmeat", 0);
		
		ingredients.put("avocado", 0);
		ingredients.put("avocadocut", 0);
		
		//initialize recipes and add in what they need
		HashMap<String, Integer> recipe1 = new HashMap<String, Integer>(); 
		recipe1.put("rawseaweed",1);
		recipe1.put("ricebag",1);
		recipe1.put("avocado",1);
		recipe1.put("crab",1);
		
		HashMap<String, Integer> recipe2 = new HashMap<String, Integer>(); 
		recipe2.put("ricebag",1);
		recipe2.put("livefish",1);
		
		HashMap<String, Integer> recipe3 = new HashMap<String, Integer>(); 
		recipe3.put("ricebag",1);
		recipe3.put("rawseaweed",1);
		recipe3.put("shrimp",1);
		
		HashMap<String, Integer> recipe4 = new HashMap<String, Integer>(); 
		recipe4.put("ricebag",1);
		recipe4.put("livefish",1);
		recipe4.put("crab",1);
		recipe4.put("avocado",1);
		

		//add each recipe into recipes
		recipes.add(new Cuttable("sushi", R.drawable.sushi, recipe1));
		recipes.add(new Cuttable("sashimisushi", R.drawable.sashimisushi, recipe2));
		recipes.add(new Cuttable("tempurasushi", R.drawable.tempurasushi, recipe3));
		recipes.add(new Cuttable("sushi1", R.drawable.sushi1, recipe4));
		
		rankingImages  = new Drawable[]
				{res.getDrawable(R.drawable.rank1),res.getDrawable(R.drawable.rank2), res.getDrawable(R.drawable.rank3),
						res.getDrawable(R.drawable.rank4), res.getDrawable(R.drawable.rank5)};
		
		rankingTitles = new String[]{"Novice Samurai", "Intermediate Samurai", "Advanced Samurai", "Daimyo", "Shogun" };
		nextLevelFlag = false; 
		
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas){
		 final Dialog recipeDialog = new Dialog(getContext());
			recipeDialog.setContentView(R.layout.ingredientlists);
			
			Button readlist = (Button) recipeDialog.findViewById(R.id.understood);
			// if button is clicked, close the custom dialog
			
			readlist.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					recipeDialog.dismiss();
					MainActivity.setIsPaused(false);		
				}
			});
			
			
			
	        viewRecipes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					recipeDialog.show();
					MainActivity.setIsPaused(true);				}
			});
			
		
		// if button is clicked, close the custom dialog
				
		if(first){
			generateStartingPosition();
			first = false; 
		}
		nextLevelFlag = false; 
		//Check for collisions
		if(pdrawn.size() >=2 && addPoint){
			//if(!pdrawn.get(pdrawn.size()-1).getFirst() && !pdrawn.get(pdrawn.size()-2).getFirst()){
				checkCollide = col.checkCollisionsVectors(pdrawn.get(pdrawn.size()-2), pdrawn.get(pdrawn.size()-1), startX+incX+offset, startY+incY+offset, offset);
				addPoint = false; 
			//}
		}
		
		Paint p = new Paint();
		Resources res = getResources();
		// Reset the coordinates of the new object if it goes off the screen
		// Note: This is the same instance
		if (startY+incY < 0 || startX+incX > getWidth() || startY+incY > getHeight() || startX+incX < 0 || checkCollide) {

			boolean checkProc = currentCuttable.needsProcessing(); 
			if(checkCollide){
				//checkCollide = false;
				
				if(checkProc){
				//switch images
					boolean checkSwitched = currentCuttable.processIngredient(); 
					if(checkSwitched){
						sushiSlicedStat++;
						circle = res.getDrawable(currentCuttable.getImage());
						//increment ingredients b/c it got hit and processed
						ingredients.put(currentCuttable.getPrevName(), ingredients.get(currentCuttable.getPrevName()) + 1); 
						
						double currentScore = col.getScore();
						totalScore += currentScore; // user's total score
						
						sushiSliced++;
						updateScore();
						
						handleCheckNextLevel();
							
						//visual feedback
						if(currentScore > 0 && currentScore < 900){
							feedback.setImageResource(R.drawable.nice);
						}
						else if(currentScore >= 900 && currentScore < 930){
							feedback.setImageResource(R.drawable.goodjob);
						}
						else if(currentScore >= 930 && currentScore < 960){
							feedback.setImageResource(R.drawable.excellent);
						}
						else if(currentScore >= 960 && currentScore < 990){
							feedback.setImageResource(R.drawable.amazing);
						}
						else if(currentScore >= 990 && currentScore <= 1000){
							feedback.setImageResource(R.drawable.perfect);
						}
						playSound.start();

						invalidate();
					}
				}
				if(currentCuttable.hasRecipe()){
				
					Log.v(currentCuttable.getName(), "entered has recipe collision");
					regenerateSushi(); 
					handleCheckNextLevel(); 
				}
				checkCollide = false;
			}
		
			else if(startY+incY < 0 || startX+incX > getWidth() || startY+incY > getHeight() || startX+incX < 0 ){
				//off screen
				if(currentCuttable.needsProcessing()){
					sushiDropped++;
					sushiDroppedTotal++; 
					if(sushiDropped % 3 ==0 && recipesMade >0){// && sushiDropped > 0){
						sushiDropped = 0; 
						recipesMade--; 
						Log.v(Integer.toString(recipesMade), "currentCuttable.needsProcessing()");
						Log.v(currentCuttable.getName(), "currentCuttable.needsProcessing() name");
						Log.v(Boolean.toString(currentCuttable.getProcessed()), "currentCuttable.needsProcessing() getProcessed()");
					}
					Log.v(Integer.toString(sushiDropped), "sushi dropped recipesMade sushiDropped");
					Log.v(Integer.toString(recipesMade), "sushi dropped recipesMade");
					feedback.setImageResource(R.drawable.tryagain);
					mpFail.start();
					
				}
				handleCheckNextLevel();
				regenerateSushi(); 
			}
			
		}
		

		for(int i = 0; i < pdrawn.size(); i++){
			Point pt = pdrawn.get(i);
			p.setColor(pt.getColor());
			if(!pt.getFirst() && i != 0){
				canvas.drawLine( (float)pdrawn.get(i-1).getX(), (float)pdrawn.get(i-1).getY(), (float)pt.getX(), (float)pt.getY(),p);
			}
			else{
				p.setStrokeWidth(pt.getSize());
			}
			
		}
		
		
		circle.setBounds(startX+incX, startY+incY, startX+offset+incX+50, startY+offset+incY+50);
		
		circle.draw(canvas);
		
	}	
	
	
	private void handleCheckNextLevel(){
		if(isWin() && nextLevelFlag == false){
			nextLevelFlag = true; 

			MainActivity.setIsPaused(true);
			
			final Dialog dialog = new Dialog(getContext());
			dialog.setContentView(R.layout.dialog);
			
			int imageNum = (int)Math.floor(Math.log(totalScore / 8000) / Math.log(2.2)) + 1;
			Log.v(Integer.toString(imageNum), "imageNum");
			
			Drawable rankI;
			String title = "";
			if(imageNum >= 5){
				rankI = rankingImages[4];
				title = rankingTitles[4];
			}
			else if(imageNum <0){
				rankI = rankingImages[0];
				title = rankingTitles[0];
			}
			else{
				rankI = rankingImages[imageNum];
				title = rankingTitles[imageNum];
			}
			dialog.setTitle("Level " + currentLevel + " completed!" + " You are now " + title + "!");
			
			
			TextView text = (TextView) dialog.findViewById(R.id.text);
			ImageView rank = (ImageView) dialog.findViewById(R.id.rank);
			rank.setImageDrawable(rankI);
			//check which ranking
			
			// Not sure about this???
			int TotalCut = LeaderBoard.loadTotalInt("TOTAL_SUSHI_CUT", this.getContext());
			int SushiGenerated = LeaderBoard.loadTotalInt("TOTAL_SUSHI_GENERATED", this.getContext());
			int totalTime = LeaderBoard.loadRecentInt("TOTAL_PLAYTIME", this.getContext());
			
			text.setText(
					"Current Score:" + totalScore + "\n" +
					"Recipes Made:" + recipesMade + "\n" +
					"Total Cut:" + TotalCut + "\n" +
					"Total Dropped:" + sushiDroppedTotal +"\n"+
					"Total Time:" + totalTime + "\n"
					);
		
			Button nextLevel = (Button) dialog.findViewById(R.id.OKbutton);
			// if button is clicked, close the custom dialog
			nextLevel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					MainActivity.setIsPaused(false);
					nextLevel();
					regenerateSushi();
				}
			});
 
			dialog.show();
		}
	}
	
	private void regenerateSushi(){
		//regenerate sushi b/c off screen
		generateStartingPosition(); 
		Resources res = getResources();
		sushiGeneratedStat++;
		//MainActivity.ti = startTi; // reset time to zero
		incX = 0; // reset everything
		incY = 0; // reset everything
		
		if(currentCuttable.hasRecipe() && checkCollide){
			playSound = MediaPlayer.create(getContext(), R.raw.bladecut);
			playSound.start(); 
			recipesMade--; 
			Log.v(Integer.toString(recipesMade), "currentCuttable.hasRecipe() && checkCollide");
			checkCollide = false; 
			for(int i = 0; i < recipes.size(); i++){
				if(recipes.get(i).getName() == currentCuttable.getName()){
					recipes.get(i).decrementCountMade(); 
					Log.v((recipes.get(i).getName()), "count made of recipe name decrement");
					Log.v(Integer.toString(recipes.get(i).countMade), "count made of recipe decrement");
				}
			}
			Log.v(Integer.toString(recipesMade), "hit a recipe");
			handleCheckNextLevel();
		}
		
		if(currentLevel > 1 && isLoss()){
			handleGameOver();
		}

		checkCollide = false; 
 
		//check if there's a recipe that has been fulfilled
		boolean finished = false; //boolean for if there's a recipe finished
		for (int i = 0; i < recipes.size(); i++){
			if(recipes.get(i).checkRecipeMade(ingredients)){
				circle = res.getDrawable(recipes.get(i).getImage()); //make the next the recipe final product
				currentCuttable = recipes.get(i);
				Log.v(Boolean.toString(currentCuttable.hasRecipe()), "has recipe");
				//decrement ingredients by recipe specifications
				Iterator<Entry<String, Integer>> it = (recipes.get(i)).getRecipe().entrySet().iterator(); 
				while(it.hasNext()){
					Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next(); 
					ingredients.put(pairs.getKey(), ingredients.get(pairs.getKey()) - pairs.getValue() );
				}
				finished = true; 
				Log.v(recipes.get(i).getName(), "recipeName");
				recipesMade++; 
				if(recipesMade > 0){
					positiveRecipe = true; 
				}
				break; 
			}
			Log.v((recipes.get(i).getName()), "count made of recipe name");
			Log.v(Integer.toString(recipes.get(i).countMade), "count made of recipe");
		}
		if(finished == false){
			//need to spawn random ingredient to get any recipe made
			for(int i = 0; i < recipes.size(); i++){
				Log.v(Integer.toString(toBeSpawn.size()), "toBeSpawnSize1");
				ArrayList<Cuttable> getTemp = recipes.get(i).getMissingIng(ingredients);
				toBeSpawn.addAll(getTemp);
				Log.v(Integer.toString(toBeSpawn.size()), "toBeSpawnSize2");
			}
			Log.v(Integer.toString(toBeSpawn.size()), "toBeSpawnSize3");
			int randomIngredient = ingRand.nextInt(toBeSpawn.size());
			currentCuttable = toBeSpawn.get((randomIngredient));
			circle = res.getDrawable(toBeSpawn.get((randomIngredient)).getImage());
			if(currentCuttable.getSound() >= 0){
				playSound = MediaPlayer.create(getContext(), currentCuttable.getSound());
			}
			toBeSpawn = new ArrayList<Cuttable>(); 
		}

		invalidate(); 
	}
	
	
	
	private void generateStartingPosition(){
		int yGeneration = random.nextInt(3);
		//int yGeneration = 0; 
		switch (yGeneration){
			case 0:
				Vy = -(getHeight()/28 + random.nextInt((getHeight()/60)+1)); // reset to default value
				Vx = -(getWidth()/100 + random.nextInt((getWidth()/100)+1));
				startY = getHeight();
				if(random.nextBoolean()){
					left = true;
					startX = random.nextInt(getWidth()) / 2;
				}
				else{
					left = false;
					startX = random.nextInt(getWidth()) / 2 + getWidth() / 2;
				}
				
				break; 
			case 1:
				left = true;
				startY = random.nextInt(1*getHeight()/3) + (int)(1*getHeight()/3);
				startX = 0; 
				generateVFromWalls();
				break;
			case 2:
				left = false; 
				startY = random.nextInt(1*getHeight()/3) + (int)(1*getHeight()/3);
				startX = getWidth(); 
				generateVFromWalls();
				break;
		}
		if(left){
			Vx *= -1;
		}
	}
	
	private void generateVFromWalls(){
        Vy = (-1/12 * random.nextInt((int)Math.sqrt(2* (getHeight() - startY) - 1 )) - getHeight()/40);
		Vx = (-(getWidth()/200 + random.nextInt((getWidth()/220)+1)));
		//math/physics not happening in time
		/*double vsquared = MainActivity.Vy*MainActivity.Vy + MainActivity.Vx*MainActivity.Vx;
		double range = vsquared;
		double angle = 90; 
		if(MainActivity.Vx < 0){
			angle = 180 + Math.atan(MainActivity.Vy / MainActivity.Vx);
		}
		else if (MainActivity.Vx < 0){
			angle = Math.atan(MainActivity.Vy / MainActivity.Vx);
		}
		range = vsquared*Math.sin(2*angle);
		/*Log.v("range: ", Double.toString(range));
		Log.v("Start x: ", Double.toString(startX));
		Log.v("Start y: ", Double.toString(startY));
		Log.v("V x: ", Double.toString(MainActivity.Vx));
		Log.v("V y: ", Double.toString(MainActivity.Vy));
		Log.v("V 2: ", Double.toString(vsquared));
		Log.v("angle: ", Double.toString(angle));
		*/
		/*(if (range < 100){
			
			//Log.v("max x: ", Double.toString(getWidth()));
			//Log.v("max y: ", Double.toString(getHeight()));
			generateVyFromWalls();
		}*/
	}
	
	public boolean onTouch(View view, MotionEvent event){
		if (event.getAction() == MotionEvent.ACTION_DOWN ) {
			pdrawn.clear();
			Point newP = new Point(event.getX(),event.getY(),drawColor,size, true); 
			pdrawn.add(newP);
			//Log.v(Float.toString(event.getX()), Float.toString(event.getY()));
			//Log.v("Action Down", "down");
			addPoint = true; 
			invalidate();
		}
		/*else if (event.getAction() == MotionEvent.ACTION_MOVE ) {
			//pdrawn.clear();
			Point newP = new Point(event.getX(),event.getY(),drawColor,size, true); 
			pdrawn.add(newP);
			Log.v(Float.toString(event.getX()), Float.toString(event.getY()));
			Log.v("action Move", "move");
			addPoint = true; 
			invalidate();
		}*/
		else if (event.getAction() != MotionEvent.ACTION_UP){
			Point newP = new Point(event.getX(),event.getY(),drawColor,size, false); 
			pdrawn.add(newP);
			//Log.v("tag", circle.getBounds().toString());
			//Log.v("action up", "up");
			addPoint = true; 
			invalidate();
		}
		
		
		return true; 
		
	}
	
	public void clearPoints(){
		pdrawn = new ArrayList<Point>(); 
	}
	
	public void setColor(int a){
		drawColor = a; 
	}
	
	public void setSize(int a){
		size = a; 
	}
	
	public void increaseX(int value){
		incX += value;
	}
	
	public void decreaseX(int value){
		incX -= value;
	}
	
	public void increaseY(int value){
		incY += value;
	}
	
	
	
	public boolean isLeft(){
		return left;
	}
	
	public void biggerSushi(){
		offset += 50;
	}
	public void smallerSushi(){
		if(offset > 50){
			offset -= 50;
		}
	}
	
	public void setText(){
		scoreboard.setText(Double.toString(totalScore));
		if(currentLevel > 1){
		//remaining.setText(Integer.toString(sushiSliced)+  " Dropped:" + Integer.toString(sushiDropped));
			recipesBack.setVisibility(ImageView.VISIBLE);
			viewRecipes.setVisibility(Button.VISIBLE);
			totalCut.setText(Integer.toString(sushiSliced));
			recipesCreated.setText(Integer.toString(recipesMade));
		}
		else{
			totalCut.setText(Integer.toString(sushiSliced));
		}
		
	}
	public void updateScore(){
		if(scoreboard != null){
			scoreboard.setText(Double.toString(totalScore)); //+ "\t Remaining Sushi: " + sushiSliced);
			totalCut.setText(Integer.toString(sushiSliced));
		}
	}
	
	public boolean isWin(){
		//return sushiSliced == 0;	
		if(currentLevel > 1){
			Log.v(Integer.toString(recipesMade), "recipesMade isWin");
			Log.v(Integer.toString(recipesMadeGoal), "recipesMadeGoal isWin");
			Log.v(Boolean.toString(recipesMade >= this.recipesMadeGoal), "isWin?");
			return (recipesMade >= this.recipesMadeGoal);
		}
		else{
			return (sushiSliced >5);
		}
	}
	
	public boolean isLoss(){
		return (this.positiveRecipe == true && recipesMade == 0 && currentLevel > 1);
	}
	
	public void nextLevel(){
		/*if(currentLevel == 1){
			sushiSliced = 0;
			sushiDropped = 0;
		} else*/

			//sushiSliced = 10 + 2*(currentLevel-2);
			//sushiDropped = 10 - (currentLevel-1);
			recipesMade = 0; 
			sushiDropped = 0; 
			if(offset > 18){
				offset -= 5;
			}
			Vy -= 10;
			positiveRecipe = false; 
			recipesMadeGoal = currentLevel*2; 
			Iterator<Entry<String, Integer>> it = ingredients.entrySet().iterator(); 
			while(it.hasNext()){
				Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next(); 
				ingredients.put(pairs.getKey(), 0 );
			}
			//MainActivity.setVy(Vy);

		currentLevel++;
	}
	
	public void move(){
		incY += Vy;
		Vy += 1; 
		incX += Vx; 
		//invalidate(); 
	}
	
	private void handleGameOver(){
		// TODO: Game Over
					LeaderBoard.saveRecentInt("RECENT_PLAYTIME", this.getContext(), MainActivity.totalMillisecondTime);
					
					// Update Total Playtime
			        int totalTime = LeaderBoard.loadRecentInt("TOTAL_PLAYTIME", this.getContext());
			        LeaderBoard.saveRecentInt("TOTAL_PLAYTIME", this.getContext(), totalTime + MainActivity.totalMillisecondTime);
			        
					Intent gameOver = new Intent (getContext(), GameOver.class);
					getContext().startActivity(gameOver);
					
					
					// Update Pieces of Sushi Cut
					LeaderBoard.saveRecentInt("RECENT_SUSHI_CUT", this.getContext(), sushiSlicedStat);
					
					int runningTotalCut = LeaderBoard.loadTotalInt("TOTAL_SUSHI_CUT", this.getContext());
					LeaderBoard.saveTotalInt("TOTAL_SUSHI_CUT", 
							this.getContext(), (runningTotalCut + sushiSlicedStat));
					
					
					// TODO: Update sushi generated cut
					LeaderBoard.saveRecentInt("RECENT_SUSHI_GENERATED", this.getContext(), sushiGeneratedStat);
					
					int runningSushiGenerated = LeaderBoard.loadTotalInt("TOTAL_SUSHI_GENERATED", this.getContext());
					LeaderBoard.saveTotalInt("TOTAL_SUSHI_GENERATED", 
							this.getContext(), (runningSushiGenerated + sushiGeneratedStat));
					
					// Update Recent Score
					LeaderBoard.saveRecentInt("RECENT_SCORE", this.getContext(), ((int) totalScore));
					
					// Update Cumulative Total Score
					int runningTotalScore = LeaderBoard.loadTotalInt("TOTAL_SCORE", this.getContext());
					LeaderBoard.saveTotalInt("TOTAL_SCORE", 
							this.getContext(), (runningTotalScore + ((int) totalScore)));
					
					// Check High Score
					try {
						List<Integer> scoreList = LeaderBoard.getScoresList();
						Integer thresholdScore = scoreList.get(4);
						if (((int) totalScore) > thresholdScore){
							scoreList.set(4, ((int) totalScore));
							LeaderBoard.setScoresList(scoreList);
							LeaderBoard.saveList(scoreList, LeaderBoard.arrayName, this.getContext());
						}
					}
					catch (Exception e) {
						Log.v(e.toString(), e.toString());
					}
	}
	
	//TO BE FURTHER TESTED
	/*public void initialize(){
		MainActivity.Vy = -(getHeight()/25 + random.nextInt((getHeight()/50)+1)); // reset to default value
		MainActivity.Vx = -(getWidth()/100 + random.nextInt((getWidth()/100)+1));
		startX = random.nextInt(500)+(getWidth()/2);
		startY = getHeight();
		incX = 0;
		incY = 0;
	}
*/
}
