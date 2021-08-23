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
		flood(pxlTracker, seedX, seedY, width, height, backlog, wrapGonColor);
	}
	
	public static void floatFlood(PixelMeta[][] pxlTracker, int seedX, int seedY, int width, int height, Set<PixelMeta> backlog, Color wrapGonColor) throws LogicException {
		if(wrapGonColor == null) {
			throw new LogicException("Recursive prepFlood invocation must have a wrapping polygon color specified. "
					+ "Seed coordinate: (" + seedX + ", " + seedY + ")");
		}
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
		flood(pxlTracker, seedX, seedY, width, height, backlog, wrapGonColor);
	}
	
	public static void flood(PixelMeta[][] pxlTracker, int seedX, int seedY, int width, int height, Set<PixelMeta> backlog, Color wrapGonColor) throws LogicException {
		if(wrapGonColor == null) {
			throw new LogicException("flood invocation must have a wrapping polygon color specified. "
					+ "Seed coordinate: (" + seedX + ", " + seedY + ")");
		}
		int scanRightBuffer = 0;
		
		do {
			int rowLength = 0, rightX = seedX;
			
			if(scanRightBuffer != 0 && !isValidFloodTarget(pxlTracker[seedY][seedX])) { 
				do {
					if(--scanRightBuffer == 0) {
						return;
					}
				} while(pxlTracker[seedY][++seedX].isBoundary());
				rightX = seedX;
			} else {
				for(; seedX != 0 && isValidFloodTarget(pxlTracker[seedY][seedX - 1]); rowLength++, scanRightBuffer++) {
					PixelMeta currPixel = pxlTracker[seedY][--seedX];
					backlog.remove(currPixel);
					currPixel.setWrapGonColor(wrapGonColor);
					if(seedY != 0 && isValidFloodTarget(pxlTracker[seedY - 1][seedX])) {
						floatFlood(pxlTracker, seedX, seedY - 1, width, height, backlog, wrapGonColor);
					}
				}
			}
			
			for(; rightX < width && isValidFloodTarget(pxlTracker[seedY][rightX]); rowLength++, rightX++) {
				PixelMeta currPixel = pxlTracker[seedY][rightX];
				backlog.remove(currPixel);
				currPixel.setWrapGonColor(wrapGonColor);
			}
			
			if(rowLength < scanRightBuffer) {
				for(int xFrontier = seedX + scanRightBuffer; ++rightX < xFrontier; ) {
					if(isValidFloodTarget(pxlTracker[seedY][rightX])) {
						flood(pxlTracker, rightX, seedY, width, height, backlog, wrapGonColor);
					}
				}
			} else if(rowLength > scanRightBuffer && seedY != 0) {
				for(int tempX = seedX + scanRightBuffer; ++tempX < rightX; ) {
					if(isValidFloodTarget(pxlTracker[seedY - 1][tempX])) {
						floatFlood(pxlTracker, seedX, seedY - 1, width, height, backlog, wrapGonColor);
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
		return pixel.getWrapGonColor() == null;
	}
}
