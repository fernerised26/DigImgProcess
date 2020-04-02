package main.java;
import java.awt.Color;

public class ContrastEnhancer {

	public static int contrastToThreeBuckets(int average, int lowerLimit, int upperLimit){
		int limit1 = lowerLimit;
		int limit2 = upperLimit;
		
		Color rgbColor = null;
		if (average < limit1) {
			rgbColor = new Color(100, 0, 0);
		} else if (average >= limit1 && average < limit2) {
			rgbColor = new Color(0, 100, 0);
		} else {
			rgbColor = new Color(0, 0, 100);
		}
		return rgbColor.getRGB();
	}
}
