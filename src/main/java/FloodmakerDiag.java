package main.java;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FloodmakerDiag {
	
	public static void flood(PixelMetaBorder[][] pxlTracker, int seedX, int seedY, int width, int height, Map<Integer, Set<PixelMetaBorder>> idToPxls, Integer currId, Color targetColor) throws LogicException {
		PixelMetaBorder currPixel = pxlTracker[seedY][seedX];
		currPixel.setGroupId(currId.intValue());
		currPixel.markFlooded();
		if(idToPxls.containsKey(currId)) {
			idToPxls.get(currId).add(currPixel);
		} else {
			Set<PixelMetaBorder> pxlSet = new HashSet<>();
			pxlSet.add(currPixel);
			idToPxls.put(currId, pxlSet);
		}
		
//		System.out.println("Recursive flood() for (" + seedX + ", " + seedY + ") Color: " + (wrapGonColor != null ? wrapGonColor.getRGB() : "null"));
		
		Queue<PixelMetaBorder> pxlsToAnalyze = new LinkedList<>();
		
		handleSurroundings(seedX, seedY, width, height, pxlTracker, targetColor, pxlsToAnalyze);
		//up
//		if(seedY-1 > -1 && isValidFloodTarget(pxlTracker[seedY-1][seedX], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY-1][seedX]);
//		}
//		//up right
//		if(seedX+1 < width && seedY-1 > -1 && isValidFloodTarget(pxlTracker[seedY-1][seedX+1], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY-1][seedX+1]);
//		}
//		//right
//		if(seedX+1 < width && isValidFloodTarget(pxlTracker[seedY][seedX+1], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY][seedX+1]);
//		}
//		//down right
//		if(seedX+1 < width && seedY+1 < height && isValidFloodTarget(pxlTracker[seedY+1][seedX+1], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY+1][seedX+1]);
//		}
//		//down
//		if(seedY+1 < height && isValidFloodTarget(pxlTracker[seedY+1][seedX], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY+1][seedX]);
//		}		
//		//down left
//		if(seedX-1 > -1 && seedY+1 < height && isValidFloodTarget(pxlTracker[seedY+1][seedX-1], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY+1][seedX-1]);
//		}		
//		//left
//		if(seedX-1 > -1 && isValidFloodTarget(pxlTracker[seedY][seedX-1], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY][seedX-1]);
//		}		
//		//up left
//		if(seedX-1 > -1 && seedY-1 > -1 && isValidFloodTarget(pxlTracker[seedY-1][seedX-1], targetColor)) {
//			pxlsToAnalyze.add(pxlTracker[seedY-1][seedX-1]);
//		}
		
		while(!pxlsToAnalyze.isEmpty()) {
			PixelMetaBorder connectedPixel = pxlsToAnalyze.poll();
//			System.out.println("Polled: "+connectedPixel.getX() +", "+ connectedPixel.getY());
			connectedPixel.setGroupId(currId.intValue());
			idToPxls.get(currId).add(connectedPixel);
			handleSurroundings(connectedPixel.getX(), connectedPixel.getY(), width, height, pxlTracker, targetColor, pxlsToAnalyze);
		}
	}
	
	private static boolean isValidFloodTarget(PixelMetaBorder pixel, Color targetColor) {
		return (pixel != null && !pixel.isFlooded() && pixel.getColor().equals(targetColor));
	}

	private static void handleSurroundings(int x, int y, int width, int height, PixelMetaBorder[][] pxlTracker, Color targetColor, Queue<PixelMetaBorder> pxlsToAnalyze) {
		//up
		if(y-1 > -1 && isValidFloodTarget(pxlTracker[y-1][x], targetColor)) {
			pxlTracker[y-1][x].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y-1][x]);
		}
		//up right
		if(x+1 < width && x-1 > -1 && isValidFloodTarget(pxlTracker[y-1][x+1], targetColor)) {
			pxlTracker[y-1][x+1].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y-1][x+1]);
		}
		//right
		if(x+1 < width && isValidFloodTarget(pxlTracker[y][x+1], targetColor)) {
			pxlTracker[y][x+1].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y][x+1]);
		}
		//down right
		if(x+1 < width && y+1 < height && isValidFloodTarget(pxlTracker[y+1][x+1], targetColor)) {
			pxlTracker[y+1][x+1].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y+1][x+1]);
		}
		//down
		if(y+1 < height && isValidFloodTarget(pxlTracker[y+1][x], targetColor)) {
			pxlTracker[y+1][x].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y+1][x]);
		}		
		//down left
		if(x-1 > -1 && y+1 < height && isValidFloodTarget(pxlTracker[y+1][x-1], targetColor)) {
			pxlTracker[y+1][x-1].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y+1][x-1]);
		}		
		//left
		if(x-1 > -1 && isValidFloodTarget(pxlTracker[y][x-1], targetColor)) {
			pxlTracker[y][x-1].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y][x-1]);
		}		
		//up left
		if(x-1 > -1 && y-1 > -1 && isValidFloodTarget(pxlTracker[y-1][x-1], targetColor)) {
			pxlTracker[y-1][x-1].markFlooded();
			pxlsToAnalyze.add(pxlTracker[y-1][x-1]);
		}
	}
}
