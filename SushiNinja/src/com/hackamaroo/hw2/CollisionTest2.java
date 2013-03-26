package com.hackamaroo.hw2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CollisionTest2 {

	Collision c;
	Point p1;
	Point p2;
	List<Point> pdrawn; 
	
	@Before
	public void setUp() {
		// line from (0,0) to (12,6)
		// circle centered at (8,0) with radius 5
		// Should intersect, min distance should be 4 ish
		c = new Collision();
		p1 = new Point(0, 0, 1, 1, false);
		p2 = new Point(12, 6, 1, 1, true);
		pdrawn = new ArrayList<Point>(); 
		pdrawn.add(p1);
		pdrawn.add(p1);
		pdrawn.add(p2);
	}
	
	@Test
	public void test() {
		boolean result = c.checkCollisionsVectors(p1, p2, 8, 0, 8);
		//System.out.println(c.getCloseness());
		assertTrue(result);
		assertTrue(c.getCloseness() == Math.sqrt(12.8));
	}
	
	@Test
	public void testFalse() {
		boolean result = c.checkCollisionsVectors(p1, p2, 8, 0, 2);
		//System.out.println(c.getCloseness());
		assertTrue(c.getCloseness() == Math.sqrt(12.8));
		assertFalse(result);
		
	}
	
	@Test
	public void testList() {
		boolean result = c.checkCollisionsVectors(pdrawn.get(pdrawn.size()-2), pdrawn.get(pdrawn.size()-1), 8, 0, 8);
		//System.out.println(c.getCloseness());
		assertTrue(c.getCloseness() == Math.sqrt(12.8));
		assertTrue(result);
		
	}
	
	@Test
	public void testTooFar() {
		boolean result = c.checkCollisionsVectors(p1, p2, 80, 0, 8);
		//System.out.println(c.getCloseness());
		assertTrue(c.getCloseness() == 68.26419266350405);
		assertFalse(result);
		
	}
	
	@Test
	public void testOnLine() {
		boolean result = c.checkCollisionsVectors(p1, p2, 2, 1, 1);
		//System.out.println(c.getCloseness());
		assertTrue(c.getCloseness() == 0);
		assertTrue(result);
	}
	
	@Test
	public void testOnExactlyA() {
		boolean result = c.checkCollisionsVectors(p1, p2, 0, 0, 1);
		//System.out.println(c.getCloseness());
		assertTrue(result);
		assertTrue(c.getCloseness() == 0);
	}
	
	@Test
	public void testOnExactlyB() {
		boolean result = c.checkCollisionsVectors(p1, p2, 12, 6, 1);
		//System.out.println(c.getCloseness());
		assertTrue(result);
		assertTrue(c.getCloseness() == 0);
	}
}