package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ColorTrackerHandler {
	
	public static int[] getRGB(BufferedImage image, int x, int y, boolean[][] tracker, int[][][] rgbTracker) {
		if(tracker[y][x] != true) {
			return rgbTracker[y][x];
		} else {
			return updateTrackers(image, x, y, tracker, rgbTracker);
		}
	}

	public static int[] updateTrackers(BufferedImage image, int x, int y, boolean[][] tracker, int[][][] rgbTracker) {
		int rgb = image.getRGB(x, y);
		tracker[y][x] = true;
		Color color = new Color(rgb);
		int[] rgbArr = new int[3];
		rgbArr[0] = color.getRed();
		rgbArr[1] = color.getGreen();
		rgbArr[2] = color.getBlue();
		
		rgbTracker[y][x] = rgbArr;
		return rgbArr;
	}
	
	public static void main(String[] args) {
		int[][][] test = new int[5][5][];
		test[0][0] = new int[] {0,1,2};
		System.out.println(Arrays.toString(test));
		
		int[][] arr = new int[5][];
		System.out.println(Arrays.toString(arr));
	}
}
