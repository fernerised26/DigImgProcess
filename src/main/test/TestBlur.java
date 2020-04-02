package main.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import main.java.ContrastEnhancer;
import main.java.DrawMyThing;
import main.java.PixelBlender;

public class TestBlur {

	public static void main(String[] args) throws IOException {
		TestBlur myClass = new TestBlur();

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
		
		for (int yIndex = 0; yIndex < height; yIndex++) { 
			for (int xIndex = 0; xIndex < width; xIndex++) {
//				int blendedGrayscaleValue = PixelBlender.blendAround(image, xIndex, yIndex, width, height);
//				twoDList.get(yIndex).set(xIndex, new Color(blendedRgb, blendedRgb, blendedRgb).getRGB());
//				int contrastRgbColor = ContrastEnhancer.contrastToThreeBuckets(blendedGrayscaleValue, 187, 202);
//				twoDList.get(yIndex).set(xIndex, contrastRgbColor);
			}
		}
		
		BufferedImage moddedImage = DrawMyThing.createImage(twoDList);
		DrawMyThing dmt = new DrawMyThing(moddedImage);

		JFrame frame = new JFrame();
		frame.getContentPane().add(dmt);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static List<Integer> createEmptyList(int length) {
		List<Integer> newList = new ArrayList<Integer>(length);
		for (int i = 0; i < length; i++) {
			newList.add(0);
		}
		return newList;
	}
}
