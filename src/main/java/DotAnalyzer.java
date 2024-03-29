package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class DotAnalyzer {
	
	private static final int SCALEBAR_TOP = 1851;
	private static final int SCALEBAR_BOT = 1905;
	
	private static final int BAR_SCAN_HEIGHT = 1860;
	private static final int WORD_SCAN_HEIGHT = 1886;
	
	private static final DecimalFormat DF = new DecimalFormat("0.000");
	
	private static int globalCounter = 0;
	
	private static StringBuilder outHtmlHeadAndStyle = new StringBuilder("<!DOCTYPE html><html><head><title>Image processing results</title></head><style>"
			+ "table, th, td {border: 1px solid}</style><body>");
	
	private static StringBuilder outHtmlNavTags = new StringBuilder("<div id=\"navtags\"/>");
	private static StringBuilder outHtmlBody = new StringBuilder("");

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
			digThroughImages("E:\\Pictures\\DigImgWork\\Aggregate");
//			digThroughImagesShort("derp");
//			digThroughImagesMedium("derp");
		} catch (LogicException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void digThroughImages(String startDirPathStr) throws IOException, LogicException {
		System.out.println("Scanning Directory: "+startDirPathStr);
		File startDir = new File(startDirPathStr);
		
		if(startDir.isDirectory()) { //top level, dir containing dirs with times
			String[] startDirContents = startDir.list();
			for(int i=0; i < startDirContents.length; i++) {
				
				String timeDirName = startDirContents[i];
				File groupingTimeDir = new File(startDirPathStr + "\\" + timeDirName);
				
				if(groupingTimeDir.isDirectory()) { //one level down, dir containing dirs with scales
					Map<Integer, Integer> roundedCountsBySize = new TreeMap<Integer, Integer>();
					
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
//									visualizeZones(pixelsWithZones);
									processQDs(pixelsWithZones, imgFile.getName(), scaleDirName, fileAsImg, roundedCountsBySize);
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
					
					createTimeAggregateOutput(timeDirName, roundedCountsBySize);
				} else {
					System.err.println(groupingTimeDir.getAbsolutePath() + " is not a directory. Please check the contents of the target directory.");
					System.exit(1);
				}
			}
		} else {
			System.err.println(startDirPathStr + " is not a directory. Please restart and enter a valid directory.");
			System.exit(1);
		}
		finishHtmlOutput();
	}
	
	private static void digThroughImagesMedium(String startDirPathStr) throws IOException, LogicException {
		System.out.println("Scanning Directory: "+startDirPathStr);
		File startDir = new File(startDirPathStr);
		
		if(startDir.isDirectory()) { //top level, dir containing dirs with times
			String[] startDirContents = startDir.list();
			for(int i=0; i < startDirContents.length; i++) {
				
				String scaleDirName = startDirContents[i];
				File groupingScaleDir = new File(startDir.getAbsolutePath() + "\\" + scaleDirName);
				
				if(groupingScaleDir.isDirectory()) { //two levels down, dir containing images
					String[] groupingScaleDirContents = groupingScaleDir.list();
					for(int k=0; k < groupingScaleDirContents.length; k++) {
						
						String imgFileName = groupingScaleDirContents[k];
						File imgFile = new File(groupingScaleDir.getAbsolutePath() + "\\" + imgFileName);
						
						if(imgFile.isFile()) {
							BufferedImage fileAsImg = ImageIO.read(imgFile);
							System.out.println("Processing file: "+imgFileName);
							PixelMeta[][] pixelsWithZones = identifyZones(fileAsImg, 0, 0);
//									visualizeZones(pixelsWithZones);
							processQDs(pixelsWithZones, imgFile.getName(), scaleDirName, fileAsImg);
							finishHtmlOutput();
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
			System.err.println(startDirPathStr + " is not a directory. Please restart and enter a valid directory.");
			System.exit(1);
		}
	}
	
	private static void digThroughImagesShort(String startDirPathStr) throws IOException, LogicException {
		File startDir = new File(startDirPathStr);
		if(startDir.isDirectory()) {
			String[] dirContents = startDir.list();
			for(int k=0; k < dirContents.length; k++) {
				
				String imgFileName = dirContents[k];
				File imgFile = new File(startDir.getAbsolutePath() + "\\" + imgFileName);
				
				if(imgFile.isFile()) {
					BufferedImage fileAsImg = ImageIO.read(imgFile);
					System.out.println("Processing file: "+imgFileName);
					try {
						PixelMeta[][] pixelsWithZones = identifyZones(fileAsImg, 0, 0);
						processQDs(pixelsWithZones, imgFileName, "500", fileAsImg);
						finishHtmlOutput();
//						scanForBlack(fileAsImg, 0, 0);
					} catch(LogicException e) {
						System.err.println(e.getMessage());
						System.err.println("Skipping: "+imgFileName);
						continue;
					}
//					visualizeZones(pixelsWithZones);
				} else {
					System.err.println(imgFile.getAbsolutePath() + " is not a file. Please check the contents of the target directory.");
					System.exit(1);
				}
			}
		} else {
			System.err.println(startDir.getAbsolutePath() + " is not a directory. Please check the contents of the target directory.");
			System.exit(1);
		}
	}

	private static int getScaleBarLength(BufferedImage image) {
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
	
//	private static void scanForBlack(BufferedImage image, int startX, int startY) {
//		int imgWidth = image.getWidth();
//		int imgHeight = image.getHeight();
//		
//		System.out.println("Scanning for black");
//		for(int y = 0; y < imgWidth; y++) {
//			for(int x = 0; x < imgWidth; x++) {
//				Color color = new Color(image.getRGB(x, y));
//				if(color.getRed() == color.getBlue() && color.getRed() == color.getGreen() && color.getRed() == 0) {
//					System.out.println("Found black: ("+x+", "+y+")");
//				} 
//			}
//		}
//	}
	
	//Iterates through all pixels, classifying each pixel by which color they are most deeply found in
	private static PixelMeta[][] identifyZones(BufferedImage image, int startX, int startY) throws LogicException {
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		PixelMeta[][] pxlTracker = new PixelMeta[imgHeight][imgWidth];
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
		
		//retain footer
		for(int y = imgWidth; y < imgHeight; y++) {
			for(int x = 0; x < imgWidth; x++) {
				Color color = new Color(image.getRGB(x, y));
				PixelMeta pixel = new PixelMeta(x, y, color, true);
				pxlTracker[y][x] = pixel;
			}
		}
		System.out.println("2D arrays initialized | timestamp:"+System.currentTimeMillis());
		
		int refillCounter = 0;
		
		int seedY = startY;
		int seedX = startX;
		while(!pxlsToAnalyze.isEmpty()) {
			PixelMeta seedPixel = pxlTracker[seedY][seedX];
			
			if(!seedPixel.isBoundary()) {
//				System.out.println("Pixels to analyze: " + pxlsToAnalyze.size());
				if(pxlsToAnalyze.size() == 0) { 
					break;
				}
				Floodmaker.floatFlood(pxlTracker, seedX, seedY, imgWidth, imgWidth, pxlsToAnalyze);
			} else {
				pxlsToAnalyze.remove(seedPixel);
			}
//				 && refillCounter < 10
			if(!pxlsToAnalyze.isEmpty()) {
//				System.out.println("Refilling coordinate queue | timestamp:"+System.currentTimeMillis());
				Iterator<PixelMeta> iterator = pxlsToAnalyze.iterator();
				while(iterator.hasNext()) {
					PixelMeta nextSeedPixel = iterator.next();
					if(nextSeedPixel.isBoundary()) {
						iterator.remove();
					} else {
						seedY = nextSeedPixel.getY();
						seedX = nextSeedPixel.getX();
//						System.out.println("Refilled with: " + nextSeedPixel.getX() + ", " + nextSeedPixel.getY());
						break;
					}
				}
//				System.out.println("Refill complete | timestamp:"+System.currentTimeMillis());
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
				PixelMeta currPixel = pixelArray[y][x];
				Color wrapGonColor = currPixel.getWrapGonColor();
				if(currPixel.isBoundary()) {
					rgbout[y][x] = Color.BLACK.getRGB();
				}
				else if(wrapGonColor == null) {
					rgbout[y][x] = Color.GRAY.getRGB();
				} else {
					rgbout[y][x] = currPixel.getWrapGonColor().getRGB();
				}
			}
		}
		BufferedImage imgout = DrawMyThing.createImage(rgbout);
		File output = new File("testout\\BoundAdvancedTestOut2.tif");
		try {
			ImageIO.write(imgout, "tif", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static void processQDs(PixelMeta[][] pixelArray, String filename, String scaleDirName, BufferedImage rawImg) throws LogicException {
		System.out.println("Visualizing QDs | timestamp: "+System.currentTimeMillis());
		int[][] rgbout = new int[pixelArray.length][pixelArray[0].length];
		PixelMeta[][] clusteringTemp = new PixelMeta[pixelArray.length][pixelArray[0].length];
		Queue<PixelMeta> clusterCandidates = new LinkedList<>();
		Map<Color, CountTuple> boundaryColorToQDCount = new HashMap<>();
		
		for(int y=0; y < pixelArray.length; y++) {
			PixelMeta[] row = pixelArray[y];
			for(int x=0; x < row.length; x++) {
				PixelMeta currPixel = pixelArray[y][x];
				Color currWrapGonColor = currPixel.getWrapGonColor();
				int currRed = currPixel.getColor().getRed();
				
				CountTuple currColorCount = null;
				if(boundaryColorToQDCount.containsKey(currWrapGonColor)) {
					currColorCount = boundaryColorToQDCount.get(currWrapGonColor);
				} else {
					currColorCount = new CountTuple();
					boundaryColorToQDCount.put(currWrapGonColor, currColorCount);
				}
				currColorCount.totalCount += 1;
				
				if(currRed < 75 && !currPixel.isBoundary()) {
					currColorCount.hitCount += 1;
					if(currWrapGonColor == null) {
						rgbout[y][x] = Color.BLACK.getRGB();
					} else {
						rgbout[y][x] = currWrapGonColor.getRGB();
						PixelMeta clusterAnalysisPixel = new PixelMeta(currPixel);
						clusterCandidates.add(clusterAnalysisPixel);
						clusteringTemp[y][x] = clusterAnalysisPixel;
					}
				} else {
					rgbout[y][x] = currPixel.getColor().getRGB();
				}
				
			}
		}
		BufferedImage imgout = DrawMyThing.createImage(rgbout);
		
		File output = new File("testout\\"+filename.substring(0, filename.indexOf("."))+".tif");
		try {
			ImageIO.write(imgout, "tif", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(filename);
		System.out.println(boundaryColorToQDCount);
		
		int[][] rgbout2 = new int[pixelArray.length][pixelArray[0].length];
		System.arraycopy(rgbout, 0, rgbout2, 0, rgbout.length);
		
		Map<Integer, Integer> countByClusterSize = ClusterAnalyzer.analyzeClusters(clusteringTemp, clusterCandidates, pixelArray[0].length, pixelArray.length, rgbout2, filename);
		
		int scaleBarLength = -5;
		scaleBarLength = getScaleBarLength(rawImg);
		if(scaleBarLength < 1) {
			throw new LogicException("Scale bar of invalid length: "+ scaleBarLength);
		}
		
		double lengthPerPixel = (double) Integer.parseInt(scaleDirName)/scaleBarLength;
		double areaPerPixel = lengthPerPixel*lengthPerPixel;
		
		addFileToHtmlOutput(boundaryColorToQDCount, filename, countByClusterSize, areaPerPixel);
	}
	
	private static void processQDs(PixelMeta[][] pixelArray, String filename, String scaleDirName, BufferedImage rawImg, Map<Integer, Integer> roundedCountsBySize) throws LogicException {
		System.out.println("Visualizing QDs | timestamp: "+System.currentTimeMillis());
		System.out.println(filename);
		int[][] rgbout = new int[pixelArray.length][pixelArray[0].length];
		PixelMeta[][] clusteringTemp = new PixelMeta[pixelArray.length][pixelArray[0].length];
		Queue<PixelMeta> clusterCandidates = new LinkedList<>();
		Map<Color, CountTuple> boundaryColorToQDCount = new HashMap<>();
		
		for(int y=0; y < pixelArray.length; y++) {
			PixelMeta[] row = pixelArray[y];
			for(int x=0; x < row.length; x++) {
				PixelMeta currPixel = pixelArray[y][x];
				Color currWrapGonColor = currPixel.getWrapGonColor();
				int currRed = currPixel.getColor().getRed();
				
				CountTuple currColorCount = null;
				if(boundaryColorToQDCount.containsKey(currWrapGonColor)) {
					currColorCount = boundaryColorToQDCount.get(currWrapGonColor);
				} else {
					currColorCount = new CountTuple();
					boundaryColorToQDCount.put(currWrapGonColor, currColorCount);
				}
				currColorCount.totalCount += 1;
				
				if(currRed < 75 && !currPixel.isBoundary()) {
					currColorCount.hitCount += 1;
					if(currWrapGonColor == null) {
						rgbout[y][x] = Color.BLACK.getRGB();
					} else {
						rgbout[y][x] = currWrapGonColor.getRGB();
						PixelMeta clusterAnalysisPixel = new PixelMeta(currPixel);
						clusterCandidates.add(clusterAnalysisPixel);
						clusteringTemp[y][x] = clusterAnalysisPixel;
					}
				} else {
					rgbout[y][x] = currPixel.getColor().getRGB();
				}
				
			}
		}
		BufferedImage imgout = DrawMyThing.createImage(rgbout);
		
//		File output = new File("testout\\"+filename.substring(0, filename.indexOf("."))+".tif");
//		try {
//			ImageIO.write(imgout, "tif", output);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		System.out.println(boundaryColorToQDCount);
		
		int[][] rgbout2 = new int[pixelArray.length][pixelArray[0].length];
		System.arraycopy(rgbout, 0, rgbout2, 0, rgbout.length);
		
		Map<Integer, Integer> countByClusterSize = ClusterAnalyzer.analyzeClusters(clusteringTemp, clusterCandidates, pixelArray[0].length, pixelArray.length, rgbout2, filename);
		
		int scaleBarLength = -5;
		scaleBarLength = getScaleBarLength(rawImg);
		if(scaleBarLength < 1) {
			throw new LogicException("Scale bar of invalid length: "+ scaleBarLength);
		}
		
		double lengthPerPixel = (double) Integer.parseInt(scaleDirName)/scaleBarLength;
		double areaPerPixel = lengthPerPixel*lengthPerPixel;
		
		List<ClusterTuple> clusterSizeDataList = new ArrayList<>();
		
		for(Entry<Integer, Integer> entry : countByClusterSize.entrySet()) {
			Integer sizeInPixelsWrapped = entry.getKey();
			int sizeInPixels = sizeInPixelsWrapped.intValue();
			double trueSize = sizeInPixels*areaPerPixel;
			int trueSizeRounded = (int) (trueSize + 0.5);
			Integer frequencyWrapped = entry.getValue();
			int frequency = frequencyWrapped.intValue();
			ClusterTuple clusterTuple = new ClusterTuple(entry.getKey(), trueSize, trueSizeRounded, frequency);
			clusterSizeDataList.add(clusterTuple);
			
			Integer roundedSize = Integer.valueOf(trueSizeRounded);
			Integer roundedSizeFreq = roundedCountsBySize.get(roundedSize);
			if(roundedSizeFreq != null) {
				roundedCountsBySize.put(roundedSize, Integer.valueOf(roundedSizeFreq.intValue() + frequency));
			} else {
				roundedCountsBySize.put(roundedSize, frequencyWrapped);
			}
		}
		addFileToHtmlOutput(boundaryColorToQDCount, filename, clusterSizeDataList, areaPerPixel);
	}
	
	private static void addFileToHtmlOutput(Map<Color, CountTuple> boundaryColorToTuple, String filename, Map<Integer, Integer> countByClusterSize, double areaPerPixel) {
		outHtmlBody.append("<div>");
		outHtmlBody.append(filename);
		outHtmlBody.append("<table>");
		
		outHtmlBody.append("<tr>");
		outHtmlBody.append("<th>").append("color").append("</th>");
		outHtmlBody.append("<th>").append("# of dark pixels").append("</th>");
		outHtmlBody.append("<th>").append("total surface area (nm^2)").append("</th>");
		outHtmlBody.append("<th>").append("surface area of dark pixels (nm^2)").append("</th>").append("</tr>");
		for(Entry<Color, CountTuple> entry : boundaryColorToTuple.entrySet()) {
			Color currColor = entry.getKey();
			if(currColor != null) {
				outHtmlBody.append("<tr>");
				outHtmlBody.append("<td style=\"height: 50px;background-color: rgb(");
				outHtmlBody.append(currColor.getRed());
				outHtmlBody.append(",");
				outHtmlBody.append(currColor.getGreen());
				outHtmlBody.append(",");
				outHtmlBody.append(currColor.getBlue());
				outHtmlBody.append(");\"></td><td>");
				outHtmlBody.append(entry.getValue().hitCount);
				outHtmlBody.append("</td><td>");
				outHtmlBody.append(DF.format(areaPerPixel*entry.getValue().totalCount));
				outHtmlBody.append("</td><td>");
				outHtmlBody.append(DF.format(areaPerPixel*entry.getValue().hitCount));
				outHtmlBody.append("</td>").append("</tr>");;
			}
		}
		outHtmlBody.append("</table>").append("<br/>").append("<table>").append("<tr>");
		outHtmlBody.append("<th>").append("# of pixels in cluster").append("</th>");
		outHtmlBody.append("<th>").append("cluster area (nm^2)").append("</th>");
		outHtmlBody.append("<th>").append("count").append("</th>").append("</tr>");
		for(Entry<Integer, Integer> entry : countByClusterSize.entrySet()) {
			int sizeInPxls = entry.getKey();
			outHtmlBody.append("<tr>");
			outHtmlBody.append("<td>").append(sizeInPxls).append("</td>");
			outHtmlBody.append("<td>").append(DF.format(areaPerPixel*sizeInPxls)).append("</td>");
			outHtmlBody.append("<td>").append(entry.getValue()).append("</td>").append("</tr>");
		}
		
		outHtmlBody.append("</table>").append("</div>").append("</br>");
	}
	
	private static void addFileToHtmlOutput(Map<Color, CountTuple> boundaryColorToTuple, String filename, List<ClusterTuple> clusterData, double areaPerPixel) {
		outHtmlBody.append("<div>");
		outHtmlBody.append(filename);
		outHtmlBody.append("<table>");
		
		outHtmlBody.append("<tr>");
		outHtmlBody.append("<th>").append("color").append("</th>");
		outHtmlBody.append("<th>").append("# of dark pixels").append("</th>");
		outHtmlBody.append("<th>").append("total surface area (nm^2)").append("</th>");
		outHtmlBody.append("<th>").append("surface area of dark pixels (nm^2)").append("</th>").append("</tr>");
		for(Entry<Color, CountTuple> entry : boundaryColorToTuple.entrySet()) {
			Color currColor = entry.getKey();
			if(currColor != null) {
				outHtmlBody.append("<tr>");
				outHtmlBody.append("<td style=\"height: 50px;background-color: rgb(");
				outHtmlBody.append(currColor.getRed());
				outHtmlBody.append(",");
				outHtmlBody.append(currColor.getGreen());
				outHtmlBody.append(",");
				outHtmlBody.append(currColor.getBlue());
				outHtmlBody.append(");\"></td><td>");
				outHtmlBody.append(entry.getValue().hitCount);
				outHtmlBody.append("</td><td>");
				outHtmlBody.append(DF.format(areaPerPixel*entry.getValue().totalCount));
				outHtmlBody.append("</td><td>");
				outHtmlBody.append(DF.format(areaPerPixel*entry.getValue().hitCount));
				outHtmlBody.append("</td>").append("</tr>");;
			}
		}
		outHtmlBody.append("</table>").append("<br/>").append("Raw Cluster Data with precision").append("<table>").append("<tr>");
		outHtmlBody.append("<th>").append("# of pixels in cluster").append("</th>");
		outHtmlBody.append("<th>").append("cluster area (nm^2)").append("</th>");
		outHtmlBody.append("<th>").append("rounded area (nm^2)").append("</th>");
		outHtmlBody.append("<th>").append("count").append("</th>").append("</tr>");
		for(ClusterTuple clusterEntry : clusterData) {
			outHtmlBody.append("<tr>");
			outHtmlBody.append("<td>").append(clusterEntry.sizeInPixels).append("</td>");
			outHtmlBody.append("<td>").append(DF.format(clusterEntry.trueSize)).append("</td>");
			outHtmlBody.append("<td>").append(clusterEntry.trueSizeRounded).append("</td>");
			outHtmlBody.append("<td>").append(clusterEntry.frequency).append("</td>").append("</tr>");
		}
		
		outHtmlBody.append("</table>").append("</div>").append("</br>");
	}
	
	private static void finishHtmlOutput() throws IOException {
		outHtmlBody.append("</body></html>");
		File htmlFileOut = new File("testout\\htmlReport.html");
		htmlFileOut.createNewFile();
		FileWriter writer = null;
		try {
			writer = new FileWriter(htmlFileOut);
			writer.write(outHtmlHeadAndStyle.append(outHtmlNavTags).append(outHtmlBody).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
		
	}
	
//	private static void addTimeAggregateToHtmlOutput(String timeDirName, Map<Integer, Integer> roundedCountsBySize) throws LogicException {
//		String tagId = timeDirName.replace(" ", "_");
//		outHtmlNavTags.append("<a href=\"#").append(tagId).append("\">Go to aggregate for ").append(timeDirName).append("</a><br/>");
//		
//		outHtmlBody.append("<a href=\"#navtags\"/>Go back to nav tags</a><br/>");
//		outHtmlBody.append("<div id=").append(tagId).append(">Aggregate Data for ").append(timeDirName).append("<table>").append("<tr>");
//		outHtmlBody.append("<th>").append("rounded area (nm^2)").append("</th>");
//		outHtmlBody.append("<th>").append("count").append("</th>").append("</tr>");
//		
//		int tracker = 0;
//		for(Entry<Integer, Integer> entry : roundedCountsBySize.entrySet()) {
//			int currSize = entry.getKey().intValue();
//			if(currSize - tracker > 1) {
//				while(currSize - tracker > 1) {
//					outHtmlBody.append("<tr>");
//					outHtmlBody.append("<td>").append(Integer.toString(tracker)).append("</td>");
//					outHtmlBody.append("<td>").append("0").append("</td>").append("</tr>");
//					tracker++;
//				}
//			} else if(currSize - tracker < 1) {
//				throw new LogicException("Tracker skipped ahead of (current smallest size - 1). Current dir: " + timeDirName + " Current size: " + currSize + " Current tracker: " + tracker);
//			}
//			
//			outHtmlBody.append("<tr>");
//			outHtmlBody.append("<td>").append(entry.getKey().toString()).append("</td>");
//			outHtmlBody.append("<td>").append(entry.getValue().toString()).append("</td>").append("</tr>");
//			tracker = currSize;
//		}
//		
//		outHtmlBody.append("</table>").append("</div>").append("</br>");
//		outHtmlBody.append("<a href=\"navtags\"/>Go back to nav tags</a><br/>");
//	}
	
	private static void createTimeAggregateOutput(String timeDirName, Map<Integer, Integer> roundedCountsBySize) throws IOException, LogicException {
		int tracker = 0;
		StringBuilder timeAggSB = new StringBuilder();
		
		timeAggSB.append("rounded area (nm^2)").append(",");
		timeAggSB.append("count").append("\n");
		
		for(Entry<Integer, Integer> entry : roundedCountsBySize.entrySet()) {
			int currSize = entry.getKey().intValue();
			if(currSize - tracker > 1) {
				while(currSize - tracker > 1) {
					tracker++;
					timeAggSB.append(Integer.toString(tracker)).append(",");
					timeAggSB.append("0").append("\n");
				}
			} else if(currSize - tracker < 1) {
				throw new LogicException("Tracker skipped ahead of (current smallest size - 1). Current dir: " + timeDirName + " Current size: " + currSize + " Current tracker: " + tracker);
			}
			
			timeAggSB.append(entry.getKey().toString()).append(",");
			timeAggSB.append(entry.getValue().toString()).append("\n");
			tracker = currSize;
		}
		
		File timeAggFileOut = new File("testout\\timeAgg-"+timeDirName+".csv");
		timeAggFileOut.createNewFile();
		FileWriter writer = null;
		try {
			writer = new FileWriter(timeAggFileOut);
			writer.write(timeAggSB.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}
	
	private static class CountTuple {
		int hitCount;
		int totalCount;
		
		CountTuple(){
			hitCount = 0;
			totalCount = 0;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("CountTuple [hitCount=").append(hitCount).append(", totalCount=").append(totalCount).append("]");
			return builder.toString();
		}
	}
	
	private static class ClusterTuple {
		int sizeInPixels;
		double trueSize;
		int trueSizeRounded;
		int frequency;
		public ClusterTuple(int sizeInPixels, double trueSize, int trueSizeRounded, int frequency) {
			super();
			this.sizeInPixels = sizeInPixels;
			this.trueSize = trueSize;
			this.trueSizeRounded = trueSizeRounded;
			this.frequency = frequency;
		}
	}
}
