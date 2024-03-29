package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorTrackerHandler {
	
	private static int counter = 0;
	
//	public static Color getRGB(BufferedImage image, int x, int y, Color[][] rgbTracker) {
//		if(rgbTracker[y][x] != null) {
//			return rgbTracker[y][x];
//		} else {
//			return updateTrackers(image, x, y, rgbTracker);
//		}
//	}
//
//	public static Color updateTrackers(BufferedImage image, int x, int y, Color[][] rgbTracker) {
//		int rgb = image.getRGB(x, y);
//		Color color = new Color(rgb);
//		
//		rgbTracker[y][x] = color;
//		return color;
//	}
	
	public static void report() {
		System.out.println("GetPixel count: "+counter);
	}
	
	public static PixelMeta getPixel(BufferedImage image, int x, int y, PixelMeta[][] pxlTracker) {
		counter++;
		if(pxlTracker[y][x] != null) {
			return pxlTracker[y][x];
		} else {
			return updateTracker(image, x, y, pxlTracker);
		}
	}

	public static PixelMeta updateTracker(BufferedImage image, int x, int y, PixelMeta[][] pxlTracker) {
		int rgb = image.getRGB(x, y);
		Color color = new Color(rgb);
		PixelMeta pixel = null;
		if(color.getRed() == color.getBlue() && color.getRed() == color.getGreen()) {
			pixel = new PixelMeta(x, y, color, false);
		} else {
			pixel = new PixelMeta(x, y, color, true);
		}	
		
		pxlTracker[y][x] = pixel;
		return pixel;
	}
}
