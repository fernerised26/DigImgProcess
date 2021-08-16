package main.java;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BoundaryAnalyzer {
	
	public static Color isPixelInsidePolygon(BufferedImage image, int initX, int initY, PixelMeta[][] pxlTracker) throws LogicException {
		int width = image.getWidth();
		int height = image.getHeight();
		if(ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker).isBoundary()) {
			System.out.println("Boundary default to null");
			return null;
		}
		
		Map<Color, Integer> boundingColorsToCrossings = new LinkedHashMap<>();
		boolean insideBoundary = false;
		Color lastBoundaryColor = null;
		PixelMeta lastBoundaryEntryPoint = null;
		
		
		//TODO revise logic, when are wrapping gons first determined? 
		
		//check pixel left (clockwise)
//		if(initX - 1 >= 0) {
//			PixelMeta leftPixel = pxlTracker[initY][initX - 1];
//			Color currWrapGonColor = leftPixel.getWrapGonColor();
//			if(leftPixel != null && currWrapGonColor != null) {
//				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
//				currPixel.setWrapGonColor(currWrapGonColor);
//				return currWrapGonColor;
//			}
//		}
		//check pixel above
//		if(initY - 1 >= 0) {
//			PixelMeta abovePixel = pxlTracker[initY - 1][initX];
//			Color currWrapGonColor = abovePixel.getWrapGonColor();
//			if(abovePixel != null && currWrapGonColor != null) {
//				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
//				currPixel.setWrapGonColor(currWrapGonColor);
//				return currWrapGonColor;
//			}
//		}
		//check pixel right
//		if(initX + 1 >= 0) {
//			PixelMeta rightPixel = pxlTracker[initY][initX + 1];
//			Color currWrapGonColor = rightPixel.getWrapGonColor();
//			if(rightPixel != null && currWrapGonColor != null) {
//				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
//				currPixel.setWrapGonColor(currWrapGonColor);
//				return currWrapGonColor;
//			}
//		}
		//check pixel below
//		if(initY + 1 >= 0) {
//			PixelMeta belowPixel = pxlTracker[initY + 1][initX];
//			if(belowPixel != null && belowPixel.getWrapGonColor() != null) {
//				Color currWrapGonColor = belowPixel.getWrapGonColor();
//				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
//				currPixel.setWrapGonColor(currWrapGonColor);
//				return currWrapGonColor;
//			}
//		}
		
		//Cast ray to the left
//		System.out.println("Casting ray from (" + initX + ", " + initY + ")");
		for(int i = initX; i >= 0; i--) {
			PixelMeta currPixel = ColorTrackerHandler.getPixel(image, i, initY, pxlTracker);
			Color currRgb = currPixel.getColor();
//			System.out.println("Current XY: (" + i + ", " + initY + ")");
//			System.out.println("Current Color: (" + currRgb.getRed() + ", " + currRgb.getGreen() + ", " + currRgb.getBlue() + ")");
			if(currPixel.isBoundary() && !insideBoundary) { //account for boundaries that are multiple pixels thick
				
				//TODO Analyze whether enter exit points of boundary have adjacent boundary pixels that are both in the same direction. If yes, is vertex, and must adjust crossing count
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
					handleVertexCrossing(lastBoundaryEntryPoint, image, lastBoundaryColor, pxlTracker, i, initY, boundingColorsToCrossings);
					
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
					handleVertexCrossing(lastBoundaryEntryPoint, image, lastBoundaryColor, pxlTracker, i, initY, boundingColorsToCrossings);
				}
				
				insideBoundary = false;
				lastBoundaryColor = null; 
			}
		}
		
		/*TODO - Current strategy is defeated by edge cases of tangential intersections. 
		Raster format restriction prevents reliable vector data of the polygons from being known
		despite originating from vector data. Can leverage original svg file
		and process both .png and .svg simultaneously. However, SVG parsing will need significant work.
		*/  
		
		for(Entry<Color, Integer> crossCount : boundingColorsToCrossings.entrySet()) {
			if(crossCount.getValue().intValue() % 2 == 1) {
				//deepest boundary found, leveraging insertion order of the LinkedHashMap
				return crossCount.getKey();
			}
		}
		System.out.println(boundingColorsToCrossings);
		System.out.println("Boundary fallthrough to null");
		return null;
	}

//	public static boolean isBoundary(Color rgb) {
//		if(rgb.getRed() == rgb.getBlue() && rgb.getRed() == rgb.getGreen()) {
//			return false;
//		} else {
//			return true;
//		}
//	}
	
	public static void main(String[] args) throws IOException{
//		int[] testa = new int[] {0, 1, 2};
//		int[] testb = new int[] {0, 1, 2};
//		
//		System.out.println(testa.equals(testb));
//		System.out.println(Arrays.equals(testa, testb));
		
//		Color color1 = new Color(255);
//		Color color2 = new Color(255);
//		
//		System.out.println(color1.equals(color2));
	}
	
	private static Contiguity checkVerticalContiguity(PixelMeta focalPoint, BufferedImage image, Color lastBoundaryColor, PixelMeta[][] pxlTracker) throws LogicException {
		boolean isContiguousBoundaryAbove = false;
		boolean isContiguousBoundaryBelow = false;
		
		if(focalPoint.getY() - 1 >= 0) {
			for(int j = -1 ; j <= 1; j++) {
				int columnToScan = focalPoint.getX() + j;
				if(columnToScan >= 0 && columnToScan < image.getWidth()) {
					PixelMeta abovePixel = ColorTrackerHandler.getPixel(image, columnToScan, focalPoint.getY() - 1, pxlTracker);
					if(lastBoundaryColor.equals(abovePixel.getColor())) {
						isContiguousBoundaryAbove = true;
						break;
					}
				}
			}
		}
		
		if(focalPoint.getY() + 1 < image.getHeight()) {
			for(int j = -1 ; j <= 1; j++) {
				int columnToScan = focalPoint.getX() + j;
				if(columnToScan >= 0 && columnToScan < image.getWidth()) {
					PixelMeta belowPixel = ColorTrackerHandler.getPixel(image, columnToScan, focalPoint.getY() + 1, pxlTracker);
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
	
	private static void handleVertexCrossing(PixelMeta lastBoundaryEntryPoint, BufferedImage image, Color lastBoundaryColor, PixelMeta[][] pxlTracker,
			int loopOffset, int yCoord, Map<Color, Integer> crossingColorMap) throws LogicException {
		Contiguity entryContiguity = checkVerticalContiguity(lastBoundaryEntryPoint, image, lastBoundaryColor, pxlTracker);
		PixelMeta exitPixel = ColorTrackerHandler.getPixel(image, loopOffset + 1, yCoord, pxlTracker);
		if(!exitPixel.equals(lastBoundaryEntryPoint)) {
			Contiguity exitContiguity = checkVerticalContiguity(exitPixel, image, lastBoundaryColor, pxlTracker);
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
