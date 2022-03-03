package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Calibrator {

	public static void calibrate(BufferedImage img, PixelMeta[][] pxlTracker, Color calibrationColor) {
		if(calibrationColor == null) {
			return;
		}
		
		long redSum = 0;
		long count = 0;
		
		for(int i=0; i<img.getWidth(); i++) {
			for(int j=0; j<img.getWidth(); j++) {
				PixelMeta currPixel = pxlTracker[i][j];
				Color currWrapGonColor = currPixel.getWrapGonColor();
				if(calibrationColor.equals(currWrapGonColor)) {
					redSum += currPixel.getColor().getRed();
					count++;
				}
			}
		}
		
		long avgRed = redSum/count;
	}
}
