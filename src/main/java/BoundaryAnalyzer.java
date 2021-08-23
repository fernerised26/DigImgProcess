package main.java;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BoundaryAnalyzer {
	
	public static Color isPixelInsidePolygon(int initX, int initY, PixelMeta[][] pxlTracker, int width, int height) throws LogicException {
		if(pxlTracker[initY][initX].isBoundary()) {
			System.out.println("Boundary default to null");
			return null;
		}
		
		Map<Color, Integer> boundingColorsToCrossings = new LinkedHashMap<>();
		boolean insideBoundary = false;
		Color lastBoundaryColor = null;
		PixelMeta lastBoundaryEntryPoint = null;
		
		//Cast ray to the left
//		System.out.println("Casting ray from (" + initX + ", " + initY + ")");
		for(int i = initX; i >= 0; i--) {
			PixelMeta currPixel = pxlTracker[initY][i];
			Color currRgb = currPixel.getColor();
//			System.out.println("Current XY: (" + i + ", " + initY + ")");
//			System.out.println("Current Color: (" + currRgb.getRed() + ", " + currRgb.getGreen() + ", " + currRgb.getBlue() + ")");
			//account for boundaries that are multiple pixels thick
			if(currPixel.isBoundary() && !insideBoundary) {
				Integer crossings = boundingColorsToCrossings.get(currRgb);
				if(crossings == null) {
					boundingColorsToCrossings.put(currRgb, Integer.valueOf(1));
				} else {
					boundingColorsToCrossings.put(currRgb, Integer.valueOf(crossings.intValue()+1));
				}
				insideBoundary = true;
				lastBoundaryColor = currRgb;
				lastBoundaryEntryPoint = currPixel;
			} else if(currPixel.isBoundary() && insideBoundary) {
				if(!currRgb.equals(lastBoundaryColor)) {
					handleVertexCrossing(lastBoundaryEntryPoint, lastBoundaryColor, pxlTracker, i, initY, boundingColorsToCrossings, width, height);
					
					Integer crossings = boundingColorsToCrossings.get(currRgb);
					if(crossings == null) {
						boundingColorsToCrossings.put(currRgb, Integer.valueOf(1));
					} else {
						boundingColorsToCrossings.put(currRgb, Integer.valueOf(crossings.intValue()+1));
					}
					lastBoundaryColor = currRgb;
				}
			} else {
				if(insideBoundary && lastBoundaryColor != null) {
					handleVertexCrossing(lastBoundaryEntryPoint, lastBoundaryColor, pxlTracker, i, initY, boundingColorsToCrossings, width, height);
				}
				
				insideBoundary = false;
				lastBoundaryColor = null; 
			}
		}
		
		for(Entry<Color, Integer> crossCount : boundingColorsToCrossings.entrySet()) {
			if(crossCount.getValue().intValue() % 2 == 1) {
				//deepest boundary found, leveraging insertion order of the LinkedHashMap
				return crossCount.getKey();
			}
		}
//		System.out.println(boundingColorsToCrossings);
//		System.out.println("Boundary fallthrough to null");
		return null;
	}
	
	private static Contiguity checkVerticalContiguity(PixelMeta focalPoint, Color lastBoundaryColor, PixelMeta[][] pxlTracker, int width, int height) throws LogicException {
		boolean isContiguousBoundaryAbove = false;
		boolean isContiguousBoundaryBelow = false;
		
		if(focalPoint.getY() - 1 >= 0) {
			for(int j = -1 ; j <= 1; j++) {
				int columnToScan = focalPoint.getX() + j;
				if(columnToScan >= 0 && columnToScan < width) {
					PixelMeta abovePixel = pxlTracker[focalPoint.getY() - 1][columnToScan];
					if(lastBoundaryColor.equals(abovePixel.getColor())) {
						isContiguousBoundaryAbove = true;
						break;
					}
				}
			}
		}
		
		if(focalPoint.getY() + 1 < height) {
			for(int j = -1 ; j <= 1; j++) {
				int columnToScan = focalPoint.getX() + j;
				if(columnToScan >= 0 && columnToScan < width) {
					PixelMeta belowPixel = pxlTracker[focalPoint.getY() + 1][columnToScan];
					if(lastBoundaryColor.equals(belowPixel.getColor())) {
						isContiguousBoundaryBelow = true;
						break;
					}
				}
			}
		}
		
		if(isContiguousBoundaryAbove && isContiguousBoundaryBelow) {
			return Contiguity.BOTH;
		} else if(isContiguousBoundaryAbove){
			return Contiguity.ABOVE;
		} else if(isContiguousBoundaryBelow){
			return Contiguity.BELOW;
		} else {
			throw new LogicException("Boundary scanned is completely 1 dimensional, does not correspond to a polygon. "
					+ "At point: (" + focalPoint.getX() + ", " + focalPoint.getY() + ")");
		}
	}
	
	private static void handleVertexCrossing(PixelMeta lastBoundaryEntryPoint, Color lastBoundaryColor, PixelMeta[][] pxlTracker,
			int loopOffset, int yCoord, Map<Color, Integer> crossingColorMap, int width, int height) throws LogicException {
		Contiguity entryContiguity = checkVerticalContiguity(lastBoundaryEntryPoint, lastBoundaryColor, pxlTracker, width, height);
		PixelMeta exitPixel = pxlTracker[yCoord][loopOffset + 1]; 
		if(!exitPixel.equals(lastBoundaryEntryPoint)) {
			Contiguity exitContiguity = checkVerticalContiguity(exitPixel, lastBoundaryColor, pxlTracker, width, height);
			if((entryContiguity == Contiguity.ABOVE && exitContiguity == Contiguity.ABOVE) ||
					(entryContiguity == Contiguity.BELOW && exitContiguity == Contiguity.BELOW)) {
				Integer crossings = crossingColorMap.get(lastBoundaryColor);
				crossingColorMap.put(lastBoundaryColor, Integer.valueOf(crossings.intValue()+1));
			}
		} else {
			if(entryContiguity != Contiguity.BOTH) {
				Integer crossings = crossingColorMap.get(lastBoundaryColor);
				crossingColorMap.put(lastBoundaryColor, Integer.valueOf(crossings.intValue()+1));
			}
		}
	}
	
	private enum Contiguity {
		ABOVE, BELOW, BOTH;
	}
}
