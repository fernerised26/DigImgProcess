package main.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import main.java.BoundaryAnalyzer;

public class TestBoundaryAnalyzer {

	public static void main(String[] args) throws IOException {
		File input = new File("PolygonTest.png");	
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();
		Color[][] rgbTracker = new Color[height][width];
		
		Color result1 = BoundaryAnalyzer.isPixelInsidePolygon(image, 189, 68, rgbTracker);
		System.out.println("Result 1 isNull: " + (result1 == null) + " [Expected: true]");
		
		Color result2 = BoundaryAnalyzer.isPixelInsidePolygon(image, 121, 34, rgbTracker);
		System.out.println("Result 2 isNull: " + (result2 == null) + " [Expected: false]");
		System.out.println("Result 2 value: " + result2.getRGB() + " [Expected: -1237980]");
		
		Color result3 = BoundaryAnalyzer.isPixelInsidePolygon(image, 25, 300, rgbTracker);
		System.out.println("Result 3 isNull : " + (result3 == null) + " [Expected: true]");
		
		File input2 = new File("PolygonTest2.png");
		BufferedImage image2 = ImageIO.read(input2);
		int width2 = image2.getWidth();
		int height2 = image2.getHeight();
		Color[][] rgbTracker2 = new Color[height2][width2];
		
		Color result4 = BoundaryAnalyzer.isPixelInsidePolygon(image2, 297, 110, rgbTracker2);
		System.out.println("Result 4: " + (result4 == null) + " [Expected: true]");
		
		Color result5 = BoundaryAnalyzer.isPixelInsidePolygon(image2, 243, 46, rgbTracker2);
		System.out.println("Result 5 isNull: " + (result5 == null) + " [Expected: false]");
		System.out.println("Result 5 value: " + result5.getRGB() + " [Expected: -12629812]");
		
		File input3 = new File("PolygonTest3.png");
		BufferedImage image3 = ImageIO.read(input3);
		int width3 = image3.getWidth();
		int height3 = image3.getHeight();
		Color[][] rgbTracker3 = new Color[height3][width3];
		
		Color result6 = BoundaryAnalyzer.isPixelInsidePolygon(image3, 541, 131, rgbTracker3);
		System.out.println("Result 6: " + (result6 == null) + " [Expected: true]");
		
		Color result7 = BoundaryAnalyzer.isPixelInsidePolygon(image3, 311, 131, rgbTracker3);
		System.out.println("Result 7 isNull: " + (result7 == null) + " [Expected: false]");
		System.out.println("Result 7 value: " + result7.getRed() + ", " + result7.getGreen() + ", " + result7.getBlue() + " [Expected: green dominant]");
		
		Color result8 = BoundaryAnalyzer.isPixelInsidePolygon(image3, 331, 191, rgbTracker3);
		System.out.println("Result 8 isNull: " + (result8 == null) + " [Expected: false]");
		System.out.println("Result 8 value: " + result8.getRed() + ", " + result8.getGreen() + ", " + result8.getBlue() + " [Expected: red dominant]");
	}
}
