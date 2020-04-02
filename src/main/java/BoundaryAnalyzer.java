package main.java;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class BoundaryAnalyzer {

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
