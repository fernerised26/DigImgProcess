package main.java;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class TestClass {
	
	public static void main(String[] args) throws IOException {
		TestClass myClass = new TestClass();

		File input = new File("E:\\Pictures\\DigImgWork\\master.tif");
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getWidth(); //yIndex is bounded on width, assumes that the microscope outputs square images
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);

		List<List<Integer>> twoDList = new ArrayList<List<Integer>>();

		for (int yIndex = 0; yIndex < height; yIndex++) {
			twoDList.add(createEmptyList(width));
		}
		int sidelength = width;
		int sectorPixelLength = sidelength/8;
		
//		for (int sectorYIndex = 0; sectorYIndex < 8; sectorYIndex++) { 
//			for (int sectorXIndex = 0; sectorXIndex < 8; sectorXIndex++) {
//				int startXIndex = sectorXIndex*sectorPixelLength;
//				int startYIndex = sectorYIndex*sectorPixelLength;
//				int endXIndex = -1;
//				int endYIndex = -1;
//				
//				if(sectorXIndex == 8){
//					endXIndex = sidelength;
//				} else {
//					endXIndex = (sectorXIndex+1)*sectorPixelLength;
//				}
//				
//				if(sectorYIndex == 8){
//					endYIndex = sidelength;
//				} else {
//					endYIndex = (sectorYIndex+1)*sectorPixelLength;
//				}
//				
//				for (int yIndex = startYIndex; yIndex < endYIndex; yIndex++) { 
//					for (int xIndex = startXIndex; xIndex < endXIndex; xIndex++) {
//						
//						
//					}
//				}
//			}
//		}

		for (int yIndex = 0; yIndex < 100; yIndex++) { 
			for (int xIndex = 0; xIndex < 100; xIndex++) {
//				 Color color = new Color(image.getRGB(xIndex,yIndex));
				// int red = color.getRed();
				// int blue = color.getBlue();
				// int green = color.getGreen();
				// if(red != blue || red != green || green != blue){
				// System.out.println("x: "+xIndex+" y: "+yIndex);
				// }

//				 System.out.println("Printing: "+xIndex+", "+yIndex+" | "+color.getRed());
				int blendedGrayscaleValue = PixelBlender.blendAround(image, xIndex, yIndex, width, height);
				
//				twoDList.get(yIndex).set(xIndex, new Color(blendedRgb, blendedRgb, blendedRgb).getRGB());
				
				int contrastRgbColor = ContrastEnhancer.contrastToThreeBuckets(blendedGrayscaleValue, 187, 202);
				twoDList.get(yIndex).set(xIndex, contrastRgbColor);
			}
		}

//		File output = new File("E:\\Pictures\\DigImgWork\\blended_master.tif");
		BufferedImage moddedImage = DrawMyThing.createImage(twoDList);
//		ImageIO.write(moddedImage, "tif", output);

		DrawMyThing dmt = new DrawMyThing(moddedImage);

		JFrame frame = new JFrame();
		frame.getContentPane().add(dmt);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

	private static List<Integer> createEmptyList(int length) {
		List<Integer> newList = new ArrayList<Integer>(length);
		for (int i = 0; i < length; i++) {
			newList.add(0);
		}
		return newList;
	}
	
	private void buildHistogram(BufferedImage image, int startXIndex, int endXIndex, int startYIndex, int endYIndex){
		Map<Integer, Integer> binToCount = new HashMap<Integer, Integer>();
		for (int yIndex = startYIndex; yIndex < endYIndex; yIndex++) { 
			for (int xIndex = startXIndex; xIndex < endXIndex; xIndex++) {
				Color color = new Color(image.getRGB(xIndex, yIndex));
				Integer bin = color.getRed()/15;
				if(binToCount.containsKey(bin)){
					Integer currCount = binToCount.get(bin);
					binToCount.put(bin, currCount+1);
				} else {
					binToCount.put(bin, 1);
				}
			}
		}
		List<Integer> bins = new ArrayList<Integer>();
		bins.addAll(binToCount.keySet());
		//build list of keys, order keys based on count in map, select based on most frequent color in the darkest third of available colors
	}
}
