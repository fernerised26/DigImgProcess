package main.test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {
		Map<Integer, Integer> test = new HashMap<>();
		Integer testCounter = Integer.valueOf(0);
		
		test.put(Integer.valueOf(1000), testCounter);
		System.out.println(test);
		
		testCounter += 1;
		test.put(Integer.valueOf(2000), testCounter);
		testCounter += 1;
		test.put(Integer.valueOf(3000), testCounter);
		System.out.println(test);
	}
}
