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
	public Drawable[] sushi_images;
	public TextView scoreboard;
	public ImageView feedback;
	public TextView remaining;
	public MediaPlayer playSound;
	public MediaPlayer mpFail = MediaPlayer.create(getContext(), R.raw.miss);
	
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
		this.currentCuttable =  new Cuttable("ingredient1");
		toBeSpawn = new ArrayList<Cuttable>();
		recipes =  new ArrayList<Cuttable>(); 
		this.setOnTouchListener(this);
		Resources res = getResources();
		sushi_images = new Drawable[]
				{res.getDrawable(R.drawable.sushi),res.getDrawable(R.drawable.sushi1), res.getDrawable(R.drawable.sushi2)};

		circle = res.getDrawable(currentCuttable.getImage());
		if(currentCuttable.getSound() >= 0){
			playSound = MediaPlayer.create(getContext(), currentCuttable.getSound());
		}
	
		startY = random.nextInt(500)+(getWidth()/2); //getHeight()/2; 
		startX = getHeight();
		
		recipesMadeGoal = 2;
		sushiDroppedTotal = 0;
		//add in all possible ingredients with 0
		ingredients.put("ingredient1", 0);
		ingredients.put("ingredient2", 0);
		ingredients.put("nori", 0);
		ingredients.put("sashimi", 0);
		ingredients.put("rawseaweed", 0);
		ingredients.put("livefish", 0);
		
		//initialize recipes and add in what they need
		HashMap<String, Integer> recipe1 = new HashMap<String, Integer>(); 
		recipe1.put("ingredient1",1);
		recipe1.put("ingredient2",1);
		
		HashMap<String, Integer> recipe2 = new HashMap<String, Integer>(); 
		recipe2.put("rawseaweed",1);
		recipe2.put("livefish",1);

		//add each recipe into recipes
		recipes.add(new Cuttable("sushi", R.drawable.sushi, recipe1));
		recipes.add(new Cuttable("logo", R.drawable.logo, recipe2));
		
		// TODO: Update number of levels
	}
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas){
		if(first){
			generateStartingPosition();
			first = false; 
		}
		
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
			Log.v("ingredient1", Integer.toString(ingredients.get("ingredient1")));
			Log.v("ingredient2", Integer.toString(ingredients.get("ingredient2")));
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

					//recipesMade--; 
					
					regenerateSushi(); 
					handleCheckNextLevel(); 
				}
				checkCollide = false;
			}
		
			else if(startY+incY < 0 || startX+incX > getWidth() || startY+incY > getHeight() || startX+incX < 0 ){
				//off screen
				handleCheckNextLevel();
				regenerateSushi(); 
			}
		}
		

		for(int i = 0; i < pdrawn.size(); i++){
			Point pt = pdrawn.get(i);
			p.setColor(pt.getColor());
			//canvas.drawCircle(pt.getX(),pt.getY(),pt.getSize(),p);
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
		if(isWin()){

			MainActivity.setIsPaused(true);
			
			final Dialog dialog = new Dialog(getContext());
			dialog.setContentView(R.layout.dialog);
			dialog.setTitle("Level " + currentLevel + "completed!");
			
			TextView text = (TextView) dialog.findViewById(R.id.text);
			
			// Not sure about this???
			int TotalCut = LeaderBoard.loadTotalInt("TOTAL_SUSHI_CUT", this.getContext());
			int SushiGenerated = LeaderBoard.loadTotalInt("TOTAL_SUSHI_GENERATED", this.getContext());
			int totalTime = LeaderBoard.loadRecentInt("TOTAL_PLAYTIME", this.getContext());
			
			text.setText(
					"Current Score:" + totalScore + "\n" +
					"Recipes Made:" + recipesMade + "\n	" +
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
		MainActivity.ti = startTi; // reset time to zero
		incX = 0; // reset everything
		incY = 0; // reset everything
		
		if(currentCuttable.hasRecipe() && checkCollide){
			
			recipesMade--; 
			checkCollide = false; 
			Log.v(Integer.toString(recipesMade), "hit a recipe");
			handleCheckNextLevel();
		}
		
		if(currentLevel > 1 && isLoss()){
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
		
		if(currentCuttable.needsProcessing()){
			sushiDropped++;
			sushiDroppedTotal++; 
			if(sushiDropped % 3 ==0 && recipesMade >0){
				sushiDropped = 0; 
				recipesMade--; 
			}
			Log.v(Integer.toString(sushiDropped), "sushi dropped recipesMade sushiDropped");
			Log.v(Integer.toString(recipesMade), "sushi dropped recipesMade");
			feedback.setImageResource(R.drawable.tryagain);
			mpFail.start();
			handleCheckNextLevel();
		}
		checkCollide = false; 
		int stopped = 0; 
		//check if there's a recipe that has been fulfilled
		boolean finished = false; //boolean for if there's a recipe finished
		for (int i = 0; i < recipes.size(); i++){
			if(recipes.get(i).checkRecipeMade(ingredients)){
				circle = res.getDrawable(recipes.get(i).getImage()); //make the next the recipe final product
				currentCuttable = recipes.get(i);
				Log.v(Boolean.toString(currentCuttable.hasRecipe()), "has recipe");
				stopped = i; 
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
		}
		/*Log.v("ingredient1", Integer.toString(ingredients.get("ingredient1")));
		Log.v("ingredient2", Integer.toString(ingredients.get("ingredient2")));
		Log.v("fish", Integer.toString(ingredients.get("livefish")));
		Log.v("seaweed", Integer.toString(ingredients.get("rawseaweed")));
		*/
		if(finished == false){
			//need to spawn random ingredient to get any recipe made
			for(int i = 0; i < recipes.size(); i++){
				toBeSpawn.addAll(recipes.get(i).getMissingIng(ingredients));
			}
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
					//startX = random.nextInt(getWidth()/4);
					//startX = random.nextInt(getWidth());
					startX = random.nextInt(getWidth()) / 2;
				}
				else{
					left = false;
					//startX = getWidth() - random.nextInt(getWidth()/4);
					//startX = getWidth() - random.nextInt(getWidth());
					startX = random.nextInt(getWidth()) / 2 + getWidth() / 2;
				}
				
				break; 
			case 1:
				left = true;
				startY = random.nextInt(1*getHeight()/3) + (int)(1*getHeight()/3);
				//startY = random.nextInt(3*getHeight()/5) + (int)(1*getHeight()/5);
				startX = 0; 
				generateVFromWalls();
				//MainActivity.Vx *= random.nextInt(3*getHeight()/4) / getHeight(); 
				break;
			case 2:
				left = false; 
				startY = random.nextInt(1*getHeight()/3) + (int)(1*getHeight()/3);
				//startY = random.nextInt(3*getHeight()/5) + (int)(1.25*getHeight()/5);
				startX = getWidth(); 
				generateVFromWalls();
				//MainActivity.Vy = -1 * random.nextInt((int)Math.sqrt(2* (getHeight() - startY) - 1)); 
				//MainActivity.Vx *= random.nextInt(3*getHeight()/4) / getHeight(); 
				break;
		}
		/*Log.v("Start x: ", Double.toString(startX));
		Log.v("Start y: ", Double.toString(startY));
		Log.v("V x: ", Double.toString(Vx));
		Log.v("V y: ", Double.toString(Vy));
		Log.v(Double.toString(getWidth()), "total width");
		Log.v(Double.toString(getHeight()), "total height");
		Log.v(Double.toString(startY), "starty");*/
		if(left){
			Vx *= -1;
		}
		//MainActivity.setVx(Vx);
		//MainActivity.setVy(Vy);
		//incY = Vy; 
		//startY = 500;
	}
	
	private void generateVFromWalls(){
		Log.v("Generate from wall:", "true");
		//MainActivity.setVy(-1/12 * random.nextInt((int)Math.sqrt(2* (getHeight() - startY) - 1 )) - getHeight()/40);
		
		//MainActivity.setVx(-(getWidth()/200 + random.nextInt((getWidth()/220)+1)));
        Vy = (-1/12 * random.nextInt((int)Math.sqrt(2* (getHeight() - startY) - 1 )) - getHeight()/40);
		
		Vx = (-(getWidth()/200 + random.nextInt((getWidth()/220)+1)));
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
			remaining.setText(Integer.toString(sushiSliced)+  " Recipes made:" + Integer.toString(recipesMade));
		}
		else{
		remaining.setText(Integer.toString(sushiSliced));
		}
		
	}
	public void updateScore(){
		if(scoreboard != null){
			scoreboard.setText(Double.toString(totalScore)); //+ "\t Remaining Sushi: " + sushiSliced);
			remaining.setText(Integer.toString(sushiSliced));
		}
	}
	
	public boolean isWin(){
		//return sushiSliced == 0;	
		if(currentLevel > 1){
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
				offset -= 10;
			}
			Vy -= 10;
			positiveRecipe = false; 
			recipesMadeGoal = currentLevel+1; 
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
