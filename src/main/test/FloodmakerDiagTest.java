package main.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import main.java.FloodmakerDiag;
import main.java.LogicException;
import main.java.PixelMetaBorder;

public class FloodmakerDiagTest {

	public static void main(String[] args) throws LogicException, IOException {
		test1();
		test2();
	}
	
	static void test1() throws IOException, LogicException {
		System.out.println("-------Test 1-------");
		File input = new File("onlyGons-0317-038.png");	
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();
		PixelMetaBorder[][] pxlTracker = new PixelMetaBorder[height][width];
		initPxlTracker(image, pxlTracker);
		
		int testCount = 0;
		int testBlueCount = 0;
		for(int y = 0; y < pxlTracker.length; y++) {
			for(int x = 0; x < pxlTracker[y].length; x++) {
				if(pxlTracker[y][x] != null) {
					testCount++;
					if(pxlTracker[y][x].getColor().equals(Color.BLUE)) {
						testBlueCount++;
					}
				}
			}
		}
		System.out.println("Total colored pixels: "+testCount); //3992
		System.out.println("Total blue pixels: "+testBlueCount); //3992
		
		int testX = 54;
		int testY = 3;
		
		PixelMetaBorder testPixel = pxlTracker[testY][testX];
		
		Map<Integer, Set<PixelMetaBorder>> testMap = new HashMap<>();
		Integer testId = Integer.valueOf(15);
		Set<PixelMetaBorder> testSet = new HashSet<>();
		testMap.put(testId, testSet);
		Color targetColor = testPixel.getColor();
		System.out.println("Target Color: "+targetColor);
		
		FloodmakerDiag.flood(pxlTracker, testX, testY, width, height, testMap, testId, targetColor);
		
		System.out.println("Total flooded pixels: "+testMap.get(testId).size());
	}
	
	static void test2() throws IOException, LogicException {
		System.out.println("-------Test 2-------");
		File input = new File("CornerFloodTest.png");	
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();
		PixelMetaBorder[][] pxlTracker = new PixelMetaBorder[height][width];
		initPxlTracker(image, pxlTracker);
		
		int testCount = 0;
		int testRedCount = 0;
		for(int y = 0; y < pxlTracker.length; y++) {
			for(int x = 0; x < pxlTracker[y].length; x++) {
				if(pxlTracker[y][x] != null) {
					testCount++;
					if(pxlTracker[y][x].getColor().equals(Color.RED)) {
						testRedCount++;
					}
				}
			}
		}
		System.out.println("Total colored pixels: "+testCount); //3992
		System.out.println("Total red pixels: "+testRedCount); //3992
		
		int testX = 4;
		int testY = 7;
		
		PixelMetaBorder testPixel = pxlTracker[testY][testX];
		
		Map<Integer, Set<PixelMetaBorder>> testMap = new HashMap<>();
		Integer testId = Integer.valueOf(15);
		Set<PixelMetaBorder> testSet = new HashSet<>();
		testMap.put(testId, testSet);
		Color targetColor = testPixel.getColor();
		System.out.println("Target Color: "+targetColor);
		
		FloodmakerDiag.flood(pxlTracker, testX, testY, width, height, testMap, testId, targetColor);
		
		System.out.println("Total flooded pixels: "+testMap.get(testId).size());
	}
	
	private static void initPxlTracker(BufferedImage image, PixelMetaBorder[][] pxlTracker) {
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				int rgb = image.getRGB(x, y);
				Color color = new Color(rgb);
				PixelMetaBorder pixel = null;
				if(color.getRed() != color.getBlue() || color.getRed() != color.getGreen()) {
					pixel = new PixelMetaBorder(x, y, color, true);
				}	
				
				pxlTracker[y][x] = pixel;
			}
		}
	}
}
