package com.project.sushi;

public class Collision {
	private double closeness; //closest point on the line segment drawn by the user
	private double score; //score based off of closeness
	
	public Collision(){
		closeness = -1; 
		score = 0; 
	}
	
	public double getScore(){
		return score; 
	}
	
	public double getCloseness(){
		return closeness; 
	}
	
	public void extrapolateLine(Point first, Point last, double xc, double yc, double rc){
		double slope = ((last.getY() - first.getY()) / (last.getX() - first.getX())); 
		double b = last.getY() - slope * last.getX(); 
		closeness = (Math.abs(slope*xc - yc + b) / Math.sqrt(slope*slope + 1));
		score = calcScore(closeness);
	}
	
    private Point closest_point_on_seg(Point a, Point b, double xc, double yc){
    	Point cPoint = new Point (xc, yc);
        Vector seg_v = new Vector(a, b);
        Vector pt_v = new Vector(a, cPoint);
        
        Vector seg_v_unit = new Vector(a, b);
        seg_v_unit.makeUnit();
        
        double proj = pt_v.dot(seg_v_unit);
        if (proj <= 0){
            return a;
        }
        if (proj >= seg_v.magnitude()){
            return b;
        }
        Point closest = seg_v_unit.timesAdd(proj, a);
        return closest;
    }

    public boolean checkCollisionsVectors(Point a, Point b, double xc, double yc, double circ_rad){
        if (a == null || b == null || b.getCollisionFlag()){
        	return false; //invalid points
        }
    	Point closest = closest_point_on_seg(a, b, xc, yc);
        Point circ_pos = new Point(xc, yc);
        closeness = circ_pos.distance(closest);
        score = calcScore(closeness); 
        //System.out.println(score);
        if (closeness > 1.0*circ_rad){ 
        	//can change the constant*circ_rad to be more stringent or lenient for diff difficulty
            return false;
        }
        else{
            //Collision!!
        	//extrapolateLine(a, b, xc, yc, circ_rad);
        	return true; 
        }
        
    }
    
    private int calcScore(double c){
    	if (c < 1000){
    		//score is based off of a circular target with different regions associated with different points
    		//max score possible is 1000
    		//we can change this to have bigger differences in score
    		return (int) (1000.0 - c); 
    	}
    	return 0; 
    }
    
    


}

