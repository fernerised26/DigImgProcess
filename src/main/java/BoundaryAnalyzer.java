package main.java;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BoundaryAnalyzer {
	
	public static Color isPixelInsidePolygon(BufferedImage image, int initX, int initY, PixelMeta[][] pxlTracker) {
		int width = image.getWidth();
		int height = image.getHeight();
		if(isBoundary(ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker).getColor())) {
			return null;
		}
		
		Map<Color, Integer> boundingColorsToCrossings = new LinkedHashMap<>();
		boolean insideBoundary = false;
		Color lastBoundaryColor = null; 
		
		//check pixel left (clockwise)
		if(initX - 1 >= 0) {
			PixelMeta leftPixel = pxlTracker[initY][initX - 1];
			Color currWrapGonColor = leftPixel.getWrapGonColor();
			if(leftPixel != null && currWrapGonColor != null) {
				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
				currPixel.setWrapGonColor(currWrapGonColor);
				return currWrapGonColor;
			}
		}
		//check pixel above
		if(initY - 1 >= 0) {
			PixelMeta abovePixel = pxlTracker[initY - 1][initX];
			Color currWrapGonColor = abovePixel.getWrapGonColor();
			if(abovePixel != null && currWrapGonColor != null) {
				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
				currPixel.setWrapGonColor(currWrapGonColor);
				return currWrapGonColor;
			}
		}
		//check pixel right
		if(initX + 1 >= 0) {
			PixelMeta rightPixel = pxlTracker[initY][initX + 1];
			Color currWrapGonColor = rightPixel.getWrapGonColor();
			if(rightPixel != null && currWrapGonColor != null) {
				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
				currPixel.setWrapGonColor(currWrapGonColor);
				return currWrapGonColor;
			}
		}
		//check pixel below
		if(initY + 1 >= 0) {
			PixelMeta belowPixel = pxlTracker[initY + 1][initX];
			if(belowPixel != null && belowPixel.getWrapGonColor() != null) {
				Color currWrapGonColor = belowPixel.getWrapGonColor();
				PixelMeta currPixel = ColorTrackerHandler.getPixel(image, initX, initY, pxlTracker);
				currPixel.setWrapGonColor(currWrapGonColor);
				return currWrapGonColor;
			}
		}
		
		//Cast ray to the left
		for(int i = initX; i > 0; i--) {
			PixelMeta currPixel = ColorTrackerHandler.getPixel(image, i, initY, pxlTracker);
			Color currRgb = currPixel.getColor();
			if(isBoundary(currRgb) && !insideBoundary) {
				Integer crossings = boundingColorsToCrossings.get(currRgb);
				if(crossings == null) {
					boundingColorsToCrossings.put(currRgb, Integer.valueOf(1));
				} else {
					boundingColorsToCrossings.put(currRgb, Integer.valueOf(crossings.intValue()+1));
				}
				insideBoundary = true;
				lastBoundaryColor = currRgb;
			} else if(isBoundary(currRgb) && insideBoundary) {
				if(!currRgb.equals(lastBoundaryColor)) {
					Integer crossings = boundingColorsToCrossings.get(currRgb);
					if(crossings == null) {
						boundingColorsToCrossings.put(currRgb, Integer.valueOf(1));
					} else {
						boundingColorsToCrossings.put(currRgb, Integer.valueOf(crossings.intValue()+1));
					}
					lastBoundaryColor = currRgb;
				}
			} else {
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
		return null;
	}

	public static boolean isBoundary(Color rgb) {
		if(rgb.getRed() == rgb.getBlue() && rgb.getRed() == rgb.getGreen()) {
			return false;
		} else {
			return true;
		}
	}
	
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
	
}
