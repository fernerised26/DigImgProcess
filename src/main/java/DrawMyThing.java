package main.java;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

public class DrawMyThing extends JPanel {

	private Image img;

	public DrawMyThing() {

	}

	public DrawMyThing(Image img) {
		this.img = img;
	}

	public void paint(Graphics g) {
		g.drawImage(img, 20, 20, this);
	}
	
	public static BufferedImage createImage(List<List<Integer>> rgbArray) {
		int height = rgbArray.size();
		int width = rgbArray.get(0).size();

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int yIndex = 0; yIndex < height; yIndex++) {
			for (int xIndex = 0; xIndex < width; xIndex++) {
				bufferedImage.setRGB(xIndex, yIndex, rgbArray.get(yIndex).get(xIndex));
			}
		}
		return bufferedImage;
	}
	
	public static BufferedImage createGrayscaleImage(List<List<Integer>> rgbArray) {
		int height = rgbArray.size();
		int width = rgbArray.get(0).size();

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for (int yIndex = 0; yIndex < height; yIndex++) {
			for (int xIndex = 0; xIndex < width; xIndex++) {
				bufferedImage.setRGB(xIndex, yIndex, rgbArray.get(yIndex).get(xIndex));
			}
		}
		return bufferedImage;
	}

	public static BufferedImage createImage(int[][] rgbArray) {
		int width = rgbArray.length;
		int height = rgbArray[0].length;

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int yIndex = 0; yIndex < height; yIndex++) {
			for (int xIndex = 0; xIndex < width; xIndex++) {
				bufferedImage.setRGB(xIndex, yIndex, rgbArray[xIndex][yIndex]);
			}
		}
		return bufferedImage;
	}
}
