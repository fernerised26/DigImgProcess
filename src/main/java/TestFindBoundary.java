package main.java;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TestFindBoundary {

	public static void main(String[] args) throws IOException{
		TestClass myClass = new TestClass();

		File input = new File("E:\\Pictures\\DigImgWork\\master.tif");
		BufferedImage image = ImageIO.read(input);
		int width = image.getWidth();
		int height = image.getHeight();
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);

		List<List<Integer>> twoDList = new ArrayList<List<Integer>>();

		for (int yIndex = 0; yIndex < height; yIndex++) {
			Color color = new Color(image.getRGB(0, yIndex));
			if(color.getRed() == 255){
				System.out.println(yIndex);
			}
		}
	}
}
