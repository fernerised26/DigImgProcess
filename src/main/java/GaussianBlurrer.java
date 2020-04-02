package main.java;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class GaussianBlurrer {

	public double calcGaussian(double sigma, double xOffset, double yOffset){
		double denominator = (Math.sqrt(2*Math.PI)*sigma);
		double exponentNumerator = -1*(Math.pow(xOffset,2)+Math.pow(yOffset,2));
		double exponentDenominator = (2*Math.pow(sigma,2));
		double gaussianResult2 = Math.pow(Math.E, (exponentNumerator/exponentDenominator))/denominator;
		
		double gaussianResult = (1/(Math.sqrt(2*Math.PI)*sigma))*Math.pow(Math.E,(-1*(Math.pow(xOffset,2)+Math.pow(yOffset,2))/(2*Math.pow(sigma,2))));
		System.out.println("Sigma: "+sigma+", x: "+xOffset+", y: "+yOffset+" f(X): "+gaussianResult);
		System.out.println("Sigma: "+sigma+", x: "+xOffset+", y: "+yOffset+" f(X): "+gaussianResult2);
		return gaussianResult;
	}
	
	public int[] calcPascalTriRow(int row){
		if(row < 1){
			return null;
		}
		if(row == 1){
			return new int[]{1};
		} else{
			int[] outArray = new int[row+1];
			outArray[0] = 1;
			for(int i=0; i<row; i++){
				outArray[i+1] = (int) (outArray[i]*((row-(double)i)/(i+1)));
				System.out.println(outArray[i+1]);
				System.out.println(((row-i)/(i+1)));
			}
			return outArray;
		}
	}
	
	public double[][] buildApproxKernel(double sigma, int span) throws Exception{
		if(span%2 != 1){
			throw new Exception("Even number for span: "+span);
		}
		if(span <= 2){
			throw new Exception("Span must be greater than 2: "+span);
		}
		
		
		double[][] firstKernel = new double[span][span];
		double[][] lastKernel = new double[span][span];
		int maxOffset = (span-1)/2;
		
		double totalWeights = 0;
		
		for(int x=(0-maxOffset); x<=maxOffset; x++){
			for(int y=(0-maxOffset); y<=maxOffset; y++){
				double gaussian = calcGaussian(sigma, x, y);
				firstKernel[x+maxOffset][y+maxOffset] = gaussian;
				totalWeights += gaussian; 
			}
		}

		for(int x=0; x<span; x++){
			for(int y=0; y<span; y++){
				lastKernel[x][y] = firstKernel[x][y]/totalWeights;
			}
		}
		return lastKernel;
	}
	
	public BufferedImage blurImage(BufferedImage img, double sigma, int span) throws Exception{
		double[][] kernel = buildApproxKernel(sigma, span);
		int width = img.getWidth();
		int height = img.getHeight();
		int midpoint = (span-1)/2;
		int[][] outRGBArray = new int[width][height];
		
		//Image loop
		for(int xIndex=0; xIndex<width; xIndex++){
			for(int yIndex=0; yIndex<height; yIndex++){
				double blurredRed = 0;
				double blurredGreen = 0;
				double blurredBlue = 0;
				
				//Neighborhood loop
				for(int xNeighborIndex=(-1*midpoint); xNeighborIndex<=midpoint; xNeighborIndex++){
					int xLookupIndex = xIndex+xNeighborIndex;
					if(xLookupIndex < 0 || xLookupIndex >= width){
						continue;
					}
					for(int yNeighborIndex=(-1*midpoint); yNeighborIndex<midpoint; yNeighborIndex++){
						int yLookupIndex = yIndex+yNeighborIndex;
						if(yLookupIndex < 0 || yLookupIndex >= height){
							continue;
						}
						
						Color currColor = new Color(img.getRGB(xLookupIndex, yLookupIndex));
						
						double weightedRed = currColor.getRed()*(kernel[xNeighborIndex+midpoint][yNeighborIndex+midpoint]);
						double weightedGreen = currColor.getGreen()*(kernel[xNeighborIndex+midpoint][yNeighborIndex+midpoint]);
						double weightedBlue = currColor.getBlue()*(kernel[xNeighborIndex+midpoint][yNeighborIndex+midpoint]);
						blurredRed += weightedRed;
						blurredGreen += weightedGreen;
						blurredBlue += weightedBlue;
					}
				}
				Color blurredColor = new Color((int) Math.ceil(blurredRed), (int) Math.ceil(blurredGreen), (int) Math.ceil(blurredBlue));
				outRGBArray[xIndex][yIndex] = blurredColor.getRGB();
			}
		}
		BufferedImage blurredImg = DrawMyThing.createImage(outRGBArray);
		return blurredImg;
	}
	
	public static void main(String[] args){
		GaussianBlurrer gb = new GaussianBlurrer();
		try {
			double[][] myKernel = gb.buildApproxKernel(3, 5);
			for(int x=0; x<5; x++){
				for(int y=0; y<5; y++){
					System.out.print(myKernel[x][y]+" ");
				}
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		int[] currPascalRow = gb.calcPascalTriRow(9);
//		System.out.println(Arrays.toString(currPascalRow));
		
	}
}


