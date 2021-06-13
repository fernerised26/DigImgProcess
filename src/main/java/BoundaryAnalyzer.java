package main.java;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class BoundaryAnalyzer {
	
	public static void isPixelInsidePolygon(BufferedImage image, int initX, int initY, int[][][] rgbTracker) {
		int width = image.getWidth();
		
		if(initX >= width/2) {
			for(int i = initX; i < width; i++) {
				
			}
		} else {
			for(int i = initX; i >= 0; i--) {
				
			}
		}
	}

	public static boolean isBoundary(int[] rgbArr) {
		if(rgbArr[0] == rgbArr[1] && rgbArr[0] == rgbArr[2]) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void main(String[] args) throws IOException{
		BoundaryAnalyzer boundAnly = new BoundaryAnalyzer();
		File input = new File("E:\\Pictures\\DigImgWork\\NaiveCircle.png");
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight(); //yIndex is bounded on width, assumes that the microscope outputs square images
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);
		
		List<List<Integer>> twoDList = new ArrayList<List<Integer>>();

		for (int yIndex = 0; yIndex < height; yIndex++) {
			twoDList.add(createEmptyList(width));
		}
	}
	
	private static List<Integer> createEmptyList(int length) {
		List<Integer> newList = new ArrayList<Integer>(length);
		for (int i = 0; i < length; i++) {
			newList.add(0);
		}
		return newList;
	}
}
