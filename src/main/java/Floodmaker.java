package main.java;

import java.awt.Color;
import java.util.Set;

public class Floodmaker {

	public static void floatFlood(PixelMeta[][] pxlTracker, int seedX, int seedY, int width, int height, Set<PixelMeta> backlog) throws LogicException {
		while(true) {
			int tempX = seedX, tempY = seedY;
			while(seedY != 0 && !pxlTracker[seedY - 1][seedX].isBoundary()) {
				seedY--;
			}
			while(seedX != 0 && !pxlTracker[seedY][seedX - 1].isBoundary()) {
				seedX--;
			}
			if(tempX == seedX && tempY == seedY) {
				break;
			}
		}
		Color wrapGonColor = BoundaryAnalyzer.isPixelInsidePolygon(seedX, seedY, pxlTracker, width, height);
//		System.out.println("flood() for (" + seedX + ", " + seedY + ") Color: " + (wrapGonColor != null ? wrapGonColor.getRGB() : "null"));
		flood(pxlTracker, seedX, seedY, width, height, backlog, wrapGonColor);
	}
	
	public static void floatFlood(PixelMeta[][] pxlTracker, int seedX, int seedY, int width, int height, Set<PixelMeta> backlog, Color wrapGonColor) throws LogicException {
		while(true) {
			int tempX = seedX, tempY = seedY;
			while(seedY != 0 && !pxlTracker[seedY - 1][seedX].isBoundary()) {
				seedY--;
			}
			while(seedX != 0 && !pxlTracker[seedY][seedX - 1].isBoundary()) {
				seedX--;
			}
			if(tempX == seedX && tempY == seedY) {
				break;
			}
		}
//		System.out.println("Recursive flood() for (" + seedX + ", " + seedY + ") Color: " + (wrapGonColor != null ? wrapGonColor.getRGB() : "null"));
		flood(pxlTracker, seedX, seedY, width, height, backlog, wrapGonColor);
	}
	
	public static void flood(PixelMeta[][] pxlTracker, int seedX, int seedY, int width, int height, Set<PixelMeta> backlog, Color wrapGonColor) throws LogicException {
		int scanRightBuffer = 0;
		
		do {
			int rowLength = 0, rightX = seedX;
			
			if(scanRightBuffer != 0 && !isValidFloodTarget(pxlTracker[seedY][seedX])) {
//				System.out.println("Started on a border: " + seedX + ", " + seedY + ")");
				do {
					if(--scanRightBuffer == 0) {
//						System.out.println("Reached the bottom, returning");
						return;
					}
				} while(pxlTracker[seedY][++seedX].isBoundary());
				rightX = seedX;
			} else {
//				System.out.println("Pushing left: " + seedX + ", " + seedY + ")");
				for(; seedX != 0 && isValidFloodTarget(pxlTracker[seedY][seedX - 1]); rowLength++, scanRightBuffer++) {
					PixelMeta currPixel = pxlTracker[seedY][--seedX];
					backlog.remove(currPixel);
					currPixel.markFlooded();
					currPixel.setWrapGonColor(wrapGonColor);
					if(seedY != 0 && isValidFloodTarget(pxlTracker[seedY - 1][seedX])) {
//						System.out.println("Checking top left: " + seedX + ", " + (seedY - 1) + ")");
						floatFlood(pxlTracker, seedX, seedY - 1, width, height, backlog, wrapGonColor);
					}
				}
			}
			
//			System.out.println("Pushing right: " + rightX + ", " + seedY + ") seedX: " + seedX);
			for(; rightX < width && isValidFloodTarget(pxlTracker[seedY][rightX]); rowLength++, rightX++) {
				PixelMeta currPixel = pxlTracker[seedY][rightX];
				backlog.remove(currPixel);
				currPixel.markFlooded();
				currPixel.setWrapGonColor(wrapGonColor);
			}
//			System.out.println("Push right done: " + rightX + ", " + seedY + ") seedX: " + seedX);
			
			if(rowLength < scanRightBuffer) {
//				System.out.println("Right side discontinuity | xFrontier: " + (seedX + scanRightBuffer));
				for(int xFrontier = seedX + scanRightBuffer; ++rightX < xFrontier; ) {
					if(isValidFloodTarget(pxlTracker[seedY][rightX])) {
//						System.out.println("Bottom right discontinuity crossed, recursing on (" + rightX + ", " + seedY + ")");
						flood(pxlTracker, rightX, seedY, width, height, backlog, wrapGonColor);
					}
				}
			} else if(rowLength > scanRightBuffer && seedY != 0) {
//				System.out.println("Uphook potential detected | tempX: " + (seedX + scanRightBuffer));
				for(int tempX = seedX + scanRightBuffer; ++tempX < rightX; ) {
					if(isValidFloodTarget(pxlTracker[seedY - 1][tempX])) {
//						System.out.println("Top right discontinuity crossed, recursing on (" + rightX + ", " + seedY + ")");
						floatFlood(pxlTracker, tempX, seedY - 1, width, height, backlog, wrapGonColor);
					}
				}
			}
			scanRightBuffer = rowLength;
		} while(scanRightBuffer != 0 && ++seedY < height);
	}
	
	private static boolean isValidFloodTarget(PixelMeta pixel) {
		if(pixel.isBoundary()) {
			return false;
		}
		return !pixel.isFlooded();
	}
}
