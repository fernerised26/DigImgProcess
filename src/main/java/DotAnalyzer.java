package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

public class DotAnalyzer {
	
	private static final int SCALEBAR_TOP = 1851;
	private static final int SCALEBAR_BOT = 1905;
	
	private static final int BAR_SCAN_HEIGHT = 1860;
	private static final int WORD_SCAN_HEIGHT = 1886;
	
	private static int globalCounter = 0;
	

	public static void main(String[] args) throws IOException {
//		System.out.println("Enter absolute directory of scale bar file: ");
//		Scanner scanner = new Scanner(System.in);
//		String scaleBarFileLoc = scanner.nextLine().trim();
//		scanner.close();
//		
//		System.out.println("Enter absolute directory of image: ");
//		scanner = new Scanner(System.in);
//		String imageFileLoc = scanner.nextLine().trim();
//		scanner.close();
		
//		System.out.println("Enter directory to start in: ");
//		Scanner scanner = new Scanner(System.in);
//		String startDir = scanner.nextLine().trim();
//		scanner.close();
		
//		digThroughImages(startDir);
		
//		File scaleBarFile = new File(scaleBarFileLoc);
//		File imageFile = new File(imageFileLoc);
		
//		File scaleBarFile = new File("F:\\Pictures\\DigImgWork\\QDAnalysis\\P1ScaleBar.tif");
//		File imageFile = new File("F:\\Pictures\\DigImgWork\\QDAnalysis\\P1Sample1.tif");
		
//		getScaleBarLength(scaleBarFile);
//		int spotCount = highlightSpots(imageFile);
		
//		System.out.println("Spots found: "+spotCount);
//		BufferedImage moddedImage = DrawMyThing.createImage(highlightedArr);
//		File output = new File("F:\\Pictures\\DigImgWork\\QDAnalysis\\P1Sample2Mini-Modded.tif");
//		ImageIO.write(moddedImage, "tif", output);
		
		//500nm bar length = 76 pixels
		//1 pixel represents ~6.5789nm, 1.778 quantum dots
		//threshold rgbValue for a hit estimated at -8600000 
		
//		for(int i = 0; i < 10; i++) {
//			long start = System.currentTimeMillis();
//			File imageFile = new File("InkscapeTest2.png");
//			BufferedImage image = ImageIO.read(imageFile);
//			int height = image.getHeight();
//			int width = image.getWidth();
//			
//			PixelMeta[][] arr = new PixelMeta[height][width];
//			for(int y = 0; y < height; y++) {
//				for(int x = 0; x < width; x++) {
//					Color color = new Color(image.getRGB(x, y));
//					PixelMeta qp = new PixelMeta(x, y, color);
//					arr[y][x] = qp;
//				}
//			}
//			long end = System.currentTimeMillis();
//			System.out.println(end - start);
//		}
		
		try {
			digThroughImages("BoundaryAdvancedTestImgs");
		} catch (LogicException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
//	System.out.println(Arrays.toString(startDir.list()));
	
	private static void digThroughImages(String startDirPathStr) throws IOException, LogicException {
		System.out.println("Scanning Directory: "+startDirPathStr);
		File startDir = new File(startDirPathStr);
		
		if(startDir.isDirectory()) { //top level, dir containing dirs with times
			String[] startDirContents = startDir.list();
			for(int i=0; i < startDirContents.length; i++) {
				
				String timeDirName = startDirContents[i];
				File groupingTimeDir = new File(startDirPathStr + "\\" + timeDirName);
				
				if(groupingTimeDir.isDirectory()) { //one level down, dir containing dirs with scales
					String[] groupingTimeDirContents = groupingTimeDir.list();
					for(int j=0; j < groupingTimeDirContents.length; j++) {
						
						String scaleDirName = groupingTimeDirContents[j];
						File groupingScaleDir = new File(groupingTimeDir.getAbsolutePath() + "\\" + scaleDirName);
						
						if(groupingScaleDir.isDirectory()) { //two levels down, dir containing images
							String[] groupingScaleDirContents = groupingScaleDir.list();
							for(int k=0; k < groupingScaleDirContents.length; k++) {
								
								String imgFileName = groupingScaleDirContents[k];
								File imgFile = new File(groupingScaleDir.getAbsolutePath() + "\\" + imgFileName);
								
								if(imgFile.isFile()) {
									BufferedImage fileAsImg = ImageIO.read(imgFile);
									System.out.println("Processing file: "+imgFileName);
									PixelMeta[][] pixelsWithZones = identifyZones(fileAsImg, 0, 0);
									visualizeZones(pixelsWithZones);
								} else {
									System.err.println(imgFile.getAbsolutePath() + " is not a file. Please check the contents of the target directory.");
									System.exit(1);
								}
							}
						} else {
							System.err.println(groupingScaleDir.getAbsolutePath() + " is not a directory. Please check the contents of the target directory.");
							System.exit(1);
						}
					}
				} else {
					System.err.println(groupingTimeDir.getAbsolutePath() + " is not a directory. Please check the contents of the target directory.");
					System.exit(1);
				}
			}
		} else {
			System.err.println(startDirPathStr + " is not a directory. Please restart and enter a valid directory.");
			System.exit(1);
		}
	}

	private static int getScaleBarLength(BufferedImage image) throws IOException {
		if(image != null) {
			int width = image.getWidth();
			int barRightmostIndex = -1;
			int barLeftmostIndex = -1;
			
			for(int xIndex = width-1; xIndex > 0; xIndex--) {
				int rgb = image.getRGB(xIndex, BAR_SCAN_HEIGHT);
				if(rgb <= -16777211) {
					barRightmostIndex = xIndex;
					break;
				}
			}
			
			for(int xIndex = barRightmostIndex; xIndex > 0; xIndex--) {
				int rgb = image.getRGB(xIndex, BAR_SCAN_HEIGHT);
				if(rgb >= -5) {
					barLeftmostIndex = xIndex;
					break;
				}
			}
			
			return (barRightmostIndex - barLeftmostIndex);
		} else {
			return -1;
		}
	}
	
	private static int highlightSpots(BufferedImage image) throws IOException {
		if(image != null) {
//			System.out.println("Scanning for dark spots: "+inputFile.getAbsolutePath());
//			BufferedImage image = ImageIO.read(inputFile);
			int[][] highlightedImgArr = new int[image.getHeight()][image.getWidth()];
			boolean[][] tracker = new boolean[image.getHeight()][image.getWidth()];
			int spotCounter = 0;
			
			for(int yIndex = 0; yIndex < image.getHeight(); yIndex++) {
				for(int xIndex = 0; xIndex < image.getWidth(); xIndex++) {
					
					//Has pixel already been classified?
					if(!tracker[yIndex][xIndex]) { 
						//If not, extract color data
						int currRGB = image.getRGB(xIndex, yIndex);
						
						//If color within threshold for QD detection
						if(currRGB < -8600000) {
							handleContiguousSpot(image, xIndex, yIndex, tracker, highlightedImgArr);
							spotCounter++;
						//If not
						} else {
							tracker[yIndex][xIndex] = true;
							highlightedImgArr[yIndex][xIndex] = currRGB;
						}
					} 
				}
			}
			
//			for(int yIndex = 0; yIndex < image.getHeight(); yIndex++) {
//				System.out.println(Arrays.toString(highlightedImgArr[yIndex]));
//			}
			
			BufferedImage moddedImage = DrawMyThing.createImage(highlightedImgArr);
			File output = new File("F:\\Pictures\\DigImgWork\\QDAnalysis\\P1Sample1-Modded.tif");
			ImageIO.write(moddedImage, "tif", output);
			System.out.println(globalCounter);
			return spotCounter;
		}
		return -1;
	}
	
	private static void handleContiguousSpot(BufferedImage image, int currX, int currY, boolean[][] tracker, int[][] highlightedImgArr) {
		globalCounter++;
		if(!tracker[currY][currX]) {
			tracker[currY][currX] = true;
			int currRGB = image.getRGB(currX, currY);
			if(currRGB < -8600000) {
				highlightedImgArr[currY][currX] = 65280;
				handleContiguousSpot(image, currX+1, currY, tracker, highlightedImgArr);
				handleContiguousSpot(image, currX-1, currY, tracker, highlightedImgArr);
				handleContiguousSpot(image, currX, currY+1, tracker, highlightedImgArr);
				handleContiguousSpot(image, currX, currY-1, tracker, highlightedImgArr);
			} else {
				highlightedImgArr[currY][currX] = currRGB;
			}
		}
	}
	
	//Iterates through all pixels, classifying each pixel by which color they are most deeply found in
	private static PixelMeta[][] identifyZones(BufferedImage image, int startX, int startY) throws LogicException {
		int imgWidth = image.getWidth();
		PixelMeta[][] pxlTracker = new PixelMeta[imgWidth][imgWidth];
		int pxlCount = imgWidth*imgWidth;
		Set<PixelMeta> pxlsToAnalyze = new HashSet<PixelMeta>(pxlCount);
		
		System.out.println("Initializing 2D arrays | timestamp:"+System.currentTimeMillis());
		for(int y = 0; y < imgWidth; y++) {
			for(int x = 0; x < imgWidth; x++) {
				Color color = new Color(image.getRGB(x, y));
				PixelMeta pixel = null;
				if(color.getRed() == color.getBlue() && color.getRed() == color.getGreen()) {
					pixel = new PixelMeta(x, y, color, false);
				} else {
					pixel = new PixelMeta(x, y, color, true);
				}	
				pxlTracker[y][x] = pixel;
				pxlsToAnalyze.add(pixel);
			}
		}
		System.out.println("2D arrays initialized | timestamp:"+System.currentTimeMillis());
		
		int refillCounter = 0;
		
		int seedY = startY;
		int seedX = startX;
		while(!pxlsToAnalyze.isEmpty()) {
			PixelMeta seedPixel = pxlTracker[seedY][seedX];
			
			if(!seedPixel.isBoundary()) {
				System.out.println("Pixels to analyze: " + pxlsToAnalyze.size());
				if(pxlsToAnalyze.size() == 0) { 
					break;
				}
				Floodmaker.floatFlood(pxlTracker, seedX, seedY, imgWidth, imgWidth, pxlsToAnalyze);
			} else {
				pxlsToAnalyze.remove(seedPixel);
			}
//				 && refillCounter < 10
			if(!pxlsToAnalyze.isEmpty()) {
				System.out.println("Refilling coordinate queue | timestamp:"+System.currentTimeMillis());
				Iterator<PixelMeta> iterator = pxlsToAnalyze.iterator();
				while(iterator.hasNext()) {
					PixelMeta nextSeedPixel = iterator.next();
					if(nextSeedPixel.isBoundary()) {
						iterator.remove();
					} else {
						seedY = nextSeedPixel.getY();
						seedX = nextSeedPixel.getX();
						System.out.println("Refilled with: " + nextSeedPixel.getX() + ", " + nextSeedPixel.getY());
						break;
					}
				}
				System.out.println("Refill complete | timestamp:"+System.currentTimeMillis());
				refillCounter++;
			}
		}
		
		return pxlTracker;
	}
	
	private static void visualizeZones(PixelMeta[][] pixelArray) {
		System.out.println("Visualizing | timestamp: "+System.currentTimeMillis());
		int[][] rgbout = new int[pixelArray.length][pixelArray.length];
		
		for(int y=0; y < pixelArray.length; y++) {
			PixelMeta[] row = pixelArray[y];
			for(int x=0; x < row.length; x++) {
				try {
					Color wrapGonColor = pixelArray[y][x].getWrapGonColor();
					if(wrapGonColor == null) {
						rgbout[y][x] = Color.BLACK.getRGB();
					} else {
						rgbout[y][x] = pixelArray[y][x].getWrapGonColor().getRGB();
					}
				} catch(NullPointerException e) {
					System.err.println("NPE for (" + x + ", " + y + ")");
				}
			}
		}
		BufferedImage imgout = DrawMyThing.createImage(rgbout);
		File output = new File("testout\\BoundAdvancedTestOut.tif");
		try {
			ImageIO.write(imgout, "tif", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
