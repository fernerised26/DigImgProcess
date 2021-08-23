package main.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.java.BoundaryAnalyzer;
import main.java.ColorTrackerHandler;
import main.java.LogicException;
import main.java.PixelMeta;

public class TestBoundaryAnalyzer {

	public static void main(String[] args) throws IOException, LogicException {
		File input = new File("PolygonTest.png");	
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();
		PixelMeta[][] pxlTracker = new PixelMeta[height][width];
		initPxlTracker(image, pxlTracker);
		
		Color result1 = BoundaryAnalyzer.isPixelInsidePolygon(189, 68, pxlTracker, width, height);
		System.out.println("Result 1 isNull: " + (result1 == null) + " [Expected: true]");
		
		Color result2 = BoundaryAnalyzer.isPixelInsidePolygon(121, 34, pxlTracker, width, height);
		System.out.println("Result 2 isNull: " + (result2 == null) + " [Expected: false]");
		System.out.println("Result 2 value: " + result2.getRGB() + " [Expected: -1237980]");
		
		Color result3 = BoundaryAnalyzer.isPixelInsidePolygon(25, 300, pxlTracker, width, height);
		System.out.println("Result 3 isNull : " + (result3 == null) + " [Expected: true]");
		
		File input2 = new File("PolygonTest2.png");
		BufferedImage image2 = ImageIO.read(input2);
		int width2 = image2.getWidth();
		int height2 = image2.getHeight();

		PixelMeta[][] pxlTracker2 = new PixelMeta[height2][width2];
		initPxlTracker(image2, pxlTracker2);
		
		Color result4 = BoundaryAnalyzer.isPixelInsidePolygon(297, 110, pxlTracker2, width2, height2);
		System.out.println("Result 4: " + (result4 == null) + " [Expected: true]");
		
		Color result5 = BoundaryAnalyzer.isPixelInsidePolygon(243, 46, pxlTracker2, width2, height2);
		System.out.println("Result 5 isNull: " + (result5 == null) + " [Expected: false]");
		System.out.println("Result 5 value: " + result5.getRGB() + " [Expected: -12629812]");
		
		File input3 = new File("PolygonTest3.png");
		BufferedImage image3 = ImageIO.read(input3);
		int width3 = image3.getWidth();
		int height3 = image3.getHeight();
		
		PixelMeta[][] pxlTracker3 = new PixelMeta[height3][width3];
		initPxlTracker(image3, pxlTracker3);
		
		Color result6 = BoundaryAnalyzer.isPixelInsidePolygon(541, 131, pxlTracker3, width3, height3);
		System.out.println("Result 6: " + (result6 == null) + " [Expected: true]");
		
		Color result7 = BoundaryAnalyzer.isPixelInsidePolygon(311, 131, pxlTracker3, width3, height3);
		System.out.println("Result 7 isNull: " + (result7 == null) + " [Expected: false]");
		System.out.println("Result 7 value: " + result7.getRed() + ", " + result7.getGreen() + ", " + result7.getBlue() + " [Expected: green dominant]");
		
		Color result8 = BoundaryAnalyzer.isPixelInsidePolygon(331, 191, pxlTracker3, width3, height3);
		System.out.println("Result 8 isNull: " + (result8 == null) + " [Expected: false]");
		System.out.println("Result 8 value: " + result8.getRed() + ", " + result8.getGreen() + ", " + result8.getBlue() + " [Expected: red dominant]");
	}
	
	private static void initPxlTracker(BufferedImage image, PixelMeta[][] pxlTracker) {
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				ColorTrackerHandler.updateTracker(image, x, y, pxlTracker);
			}
		}
	}
}
