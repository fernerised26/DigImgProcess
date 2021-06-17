package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ColorTrackerHandler {
	
	public static Color getRGB(BufferedImage image, int x, int y, Color[][] rgbTracker) {
		if(rgbTracker[y][x] != null) {
			return rgbTracker[y][x];
		} else {
			return updateTrackers(image, x, y, rgbTracker);
		}
	}

	public static Color updateTrackers(BufferedImage image, int x, int y, Color[][] rgbTracker) {
		int rgb = image.getRGB(x, y);
		Color color = new Color(rgb);
		
		rgbTracker[y][x] = color;
		return color;
	}
	
	public static void main(String[] args) {
		int[][][] test = new int[5][5][];
		test[0][0] = new int[] {0,1,2};
		System.out.println(Arrays.toString(test));
		
		int[][] arr = new int[5][];
		System.out.println(Arrays.toString(arr));
	}
}
