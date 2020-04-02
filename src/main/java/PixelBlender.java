package main.java;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PixelBlender {

	public static int blendAround(BufferedImage image, int currXIndex, int currYIndex, int maxX, int maxY) {
		int xstart = currXIndex - 2;
		int xend = currXIndex + 2;
		int ystart = currYIndex - 2;
		int yend = currYIndex + 2;
		List<Integer> redValues = new ArrayList<Integer>();

		for (int i = xstart; i < xend; i++) {
			if (i >= 0 && i < maxX) {
				for (int j = ystart; j < yend; j++) {
					if (j >= 0 && j < maxY) {
						try {
							Color color = new Color(image.getRGB(i, j));
							redValues.add(color.getRed()); //assumption - grayscale
						} catch (Exception e) {
							System.out.println("Failure on " + i + ", " + j);
							throw e;
						}
					}
				}
			}
		}

		int cumulative = 0;
		for (Integer redValue : redValues) {
			cumulative += redValue;
		}
		double rawAverage = cumulative / redValues.size();
		int average = (int) Math.floor(rawAverage);
		System.out.println(average);
		return average;
	}
}
