package main.java;

import java.awt.Color;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FloodmakerCluster {
	
	public static void floatFlood(PixelMeta[][] pxlTracker, int seedX, int seedY, int width, int height, Queue<PixelMeta> backlog, Set<PixelMeta> clusterTracker) throws LogicException {
		while(true) {
			int tempX = seedX, tempY = seedY;
			while(seedY != 0 && isValidFloodTarget(pxlTracker[seedY - 1][seedX])) {
				seedY--;
			}
			while(seedX != 0 && isValidFloodTarget(pxlTracker[seedY][seedX - 1])) {
				seedX--;
			}
			if(tempX == seedX && tempY == seedY) {
				break;
			}
		}
//		System.out.println("Recursive flood() for (" + seedX + ", " + seedY + ")");
		flood(pxlTracker, seedX, seedY, width, height, backlog, clusterTracker);
	}
	
	public static void flood(PixelMeta[][] pxlTracker, int seedX, int seedY, int width, int height, Queue<PixelMeta> backlog, Set<PixelMeta> clusterTracker) throws LogicException {
		int scanRightBuffer = 0;
		
		do {
			int rowLength = 0, rightX = seedX;
			
			if(scanRightBuffer != 0 && !isValidFloodTarget(pxlTracker[seedY][seedX])) {
//				System.out.println("Started on an invalid pixel: " + seedX + ", " + seedY + ")");
				do {
					if(--scanRightBuffer == 0) {
//						System.out.println("Reached the right edge, returning");
						return;
					}
				} while(pxlTracker[seedY][++seedX] == null);
				rightX = seedX;
			} else {
//				System.out.println("Pushing left: " + seedX + ", " + seedY + ")");
				for(; seedX != 0 && isValidFloodTarget(pxlTracker[seedY][seedX - 1]); rowLength++, scanRightBuffer++) {
					PixelMeta currPixel = pxlTracker[seedY][--seedX];
					backlog.remove(currPixel);
					currPixel.markFlooded();
					clusterTracker.add(currPixel);
					if(seedY != 0 && isValidFloodTarget(pxlTracker[seedY - 1][seedX])) {
//						System.out.println("Checking top left: " + seedX + ", " + (seedY - 1) + ")");
						floatFlood(pxlTracker, seedX, seedY - 1, width, height, backlog, clusterTracker);
					}
				}
			}
			
//			System.out.println("Pushing right: " + rightX + ", " + seedY + ") seedX: " + seedX);
			for(; rightX < width && isValidFloodTarget(pxlTracker[seedY][rightX]); rowLength++, rightX++) {
				PixelMeta currPixel = pxlTracker[seedY][rightX];
				backlog.remove(currPixel);
				currPixel.markFlooded();
				clusterTracker.add(currPixel);
			}
//			System.out.println("Push right done: " + rightX + ", " + seedY + ") seedX: " + seedX);
			
			if(rowLength < scanRightBuffer) {
//				System.out.println("Right side discontinuity | xFrontier: " + (seedX + scanRightBuffer));
				for(int xFrontier = seedX + scanRightBuffer; ++rightX < xFrontier; ) {
					if(isValidFloodTarget(pxlTracker[seedY][rightX])) {
//						System.out.println("Bottom right discontinuity crossed, recursing on (" + rightX + ", " + seedY + ")");
						flood(pxlTracker, rightX, seedY, width, height, backlog, clusterTracker);
					}
				}
			} else if(rowLength > scanRightBuffer && seedY != 0) {
//				System.out.println("Uphook potential detected | tempX: " + (seedX + scanRightBuffer));
				for(int tempX = seedX + scanRightBuffer; ++tempX < rightX; ) {
					if(isValidFloodTarget(pxlTracker[seedY - 1][tempX])) {
//						System.out.println("Top right discontinuity crossed, recursing on (" + rightX + ", " + seedY + ")");
						floatFlood(pxlTracker, tempX, seedY - 1, width, height, backlog, clusterTracker);
					}
				}
			}
			scanRightBuffer = rowLength;
		} while(scanRightBuffer != 0 && ++seedY < height);
	}
	
	private static boolean isValidFloodTarget(PixelMeta pixel) {
		if(pixel == null || pixel.isFlooded()) {
			return false;
		}
		return true;
	}
}
