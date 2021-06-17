package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

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
		
		System.out.println("Enter directory to start in: ");
		Scanner scanner = new Scanner(System.in);
		String startDir = scanner.nextLine().trim();
		scanner.close();
		
		digThroughImages(startDir);
		
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
	}
	
//	System.out.println(Arrays.toString(startDir.list()));
	
	private static void digThroughImages(String startDirPathStr) {
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
									//TODO Process image file
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
	private static void identifyZones(BufferedImage image, int startX, int startY) {
//		boolean[][] tracker = new boolean[image.getHeight()][image.getWidth()];
		Color[][] rgbVals = new Color[image.getHeight()][image.getWidth()];
		Queue<SeedCoord> coordQueue = new LinkedList<SeedCoord>();
//		Integer boundaryColorIndexCounter = 0;
//		Map<Integer, int[]> rgbToIndex = new HashMap<>();
		
		Color startRgbArr = ColorTrackerHandler.getRGB(image, startX, startY, rgbVals);
		
		if(isBoundary(startRgbArr)) {
			//TODO - started on a boundary 
//			if(!rgbToIndex.containsKey(rgbVals[startY][startX])) {
//				rgbToIndex.put(rgbVals[startY][startX], boundaryColorIndexCounter);
//				boundaryColorIndexCounter += 1;
//			}
			SeedCoord seed1 = new SeedCoord(startX + 1, startX + 1, startY, 1);
			SeedCoord seed2 = new SeedCoord(startX + 1, startX + 1, startY - 1, 1);
			coordQueue.add(seed1);
			coordQueue.add(seed2);
		} else {
			//TODO - started on a deadzone
		}
		
		while(!coordQueue.isEmpty()) {
			SeedCoord currSeed = coordQueue.poll();
			int localX = currSeed.x1;
			Color currRgbArr = ColorTrackerHandler.getRGB(image, localX, currSeed.y, rgbVals);
			if(!isBoundary(currRgbArr)) {
				
			} else {
				//Seed is no longer inside
			}
		}
	}
	
	public static boolean isBoundary(Color rgb) {
		if(rgb.getRed() == rgb.getBlue() && rgb.getRed() == rgb.getGreen()) {
			return false;
		} else {
			return true;
		}
	}
	
	private void isInside(int x, int y) {
		
	}
	
	private static class SeedCoord {
		private int x1;
		private int x2;
		private int y;
		private int dy;
		SeedCoord(int x1, int x2, int y, int dy) {
			super();
			this.x1 = x1;
			this.x2 = x2;
			this.y = y;
			this.dy = dy;
		}
	}
}
