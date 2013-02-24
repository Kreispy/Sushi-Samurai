package com.hackamaroo.hw2;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class CollisionTest {

	Collision c;
	Point p1;
	Point p2;
	
	@Before
	public void setUp() {
		// line from (0,0) to (12,6)
		// circle centered at (8,0) with radius 5
		// Should intersect, min distance should be 4 ish
		c = new Collision();
		p1 = new Point(0, 0, 1, 1, false);
		p2 = new Point(12, 6, 1, 1, true);
	}
	
	@Test
	public void test() {
		boolean result = c.checkCollision(p1, p2, 8, 0, 5);
		assertTrue(result);
		System.out.println(c.getMinDist());
	}

}
