package main.java;
import java.util.List;

public class StatsHelper {
	

	public void sectorizeTo64(List<List<Integer>> pixelArray, int sidelength){
		int sectorPixelLength = sidelength/8;
		
		for (int sectorYIndex = 0; sectorYIndex < 8; sectorYIndex++) { 
			for (int sectorXIndex = 0; sectorXIndex < 8; sectorXIndex++) {
				int startXIndex = sectorXIndex*sectorPixelLength;
				int startYIndex = sectorYIndex*sectorPixelLength;
				int endXIndex = -1;
				int endYIndex = -1;
				
				if(sectorXIndex == 8){
					endXIndex = sidelength;
				} else {
					endXIndex = (sectorXIndex+1)*sectorPixelLength;
				}
				
				if(sectorYIndex == 8){
					endYIndex = sidelength;
				} else {
					endYIndex = (sectorYIndex+1)*sectorPixelLength;
				}
				
				for (int yIndex = startYIndex; yIndex < endYIndex; yIndex++) { 
					for (int xIndex = startXIndex; xIndex < endXIndex; xIndex++) {
						
						
					}
				}
			}
		}
	}
}
