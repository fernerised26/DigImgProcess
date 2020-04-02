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
import main.java.GaussianBlurrer;
import main.java.PixelBlender;

public class TestGaussianBlurrer {

	public static void main(String[] args) throws Exception {
		TestBlur myClass = new TestBlur();

		File input = new File("E:\\Pictures\\DigImgWork\\master.tif");
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getWidth(); //yIndex is bounded on width, assumes that the microscope outputs square images
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);

		GaussianBlurrer blurObj = new GaussianBlurrer();
		BufferedImage blurredImage = blurObj.blurImage(image, 1, 3);

		File output = new File("E:\\Pictures\\DigImgWork\\gaussian_blur2.tif");
		ImageIO.write(blurredImage, "tif", output);
		
		DrawMyThing dmt = new DrawMyThing(blurredImage);

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
}
