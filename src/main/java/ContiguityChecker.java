package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

public class ContiguityChecker {

	public static void main(String[] args) {
		
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
									PixelMeta[][] pxlArr = isolateColors(fileAsImg, 0, 0);

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
	
	private static PixelMeta[][] isolateColors(BufferedImage image, int startX, int startY) throws LogicException {
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
				if(!(color.getRed() == color.getBlue() && color.getRed() == color.getGreen())) {
					pixel = new PixelMeta(x, y, color, true);
					pxlTracker[y][x] = pixel;
					pxlsToAnalyze.add(pixel);
				}	
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
		return pxlTracker;
	}
	
	private static void scan(PixelMeta[][] pxlTracker, Set<PixelMeta> pxlsToAnalyze, int imgWidth) {
		//topmost -> leftmost
		for(int y = 0; y < imgWidth; y++) {
			for(int x = 0; x < imgWidth; x++) {
				if(pxlTracker[y][x] == null) {
					continue;
				}
				PixelMeta seedPixel = pxlTracker[y][x];
				pxlsToAnalyze.remove(seedPixel);
				Adjacencies adj = new Adjacencies();
//				1 2 3
//				4 x 5
//				6 7 8
				boolean inboundDown = (y+1) < imgWidth;
				boolean inboundUp = (y-1) > -1;
				boolean inboundRight = (x+1) < imgWidth;
				boolean inboundLeft = (x-1) > -1;
				
				if(inboundUp) {
					if(inboundLeft && pxlTracker[y-1][x-1] != null && seedPixel.getColor().equals(pxlTracker[y-1][x].getColor())) {
						adj.pos1 = true;
						adj.total += 1;
					}
					
					if(pxlTracker[y-1][x] != null && seedPixel.getColor().equals(pxlTracker[y-1][x].getColor())) {
						adj.pos2 = true;
						adj.total += 1;
					}
					
					if(pxlTracker[y-1][x] != null && seedPixel.getColor().equals(pxlTracker[y-1][x].getColor())) {
						adj.pos2 = true;
						adj.total += 1;
					}
				}
			}
		}
	}
	
	private static class Adjacencies {
		boolean pos1 = false;
		boolean pos2 = false;
		boolean pos3 = false;
		boolean pos4 = false;
		boolean pos5 = false;
		boolean pos6 = false;
		boolean pos7 = false;
		boolean pos8 = false;
		int total = 0;
	}
}
