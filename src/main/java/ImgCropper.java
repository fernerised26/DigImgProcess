package main.java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImgCropper {
	
	private static final int SCALEBAR_TOP = 1851;
	private static final int SCALEBAR_BOT = 1905;
	
	private static final int BAR_SCAN_HEIGHT = 1860;
	private static final int WORD_SCAN_HEIGHT = 1886;
	

	public static void main(String[] args) throws IOException {
		System.out.println("Enter absolute directory of files for input: ");
		Scanner scanner = new Scanner(System.in);
		String inputDirString = scanner.nextLine().trim();
		scanner.close();
		File inputDir = new File(inputDirString);
		if (inputDir.isDirectory()) {
			File[] files = inputDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if(files[i].isFile()){
					cropImage(files[i], inputDir.getCanonicalPath()+"/cropped");
				}
			}

		} else {
//			String parent = inputDir.getParent();
			throw new IOException("Not a directory");
		}
	}

	private static void cropImage(File inputFile, String outputDir) throws IOException {
		System.out.println("Cropping: "+inputFile.getAbsolutePath());
		BufferedImage image = ImageIO.read(inputFile);
		int width = image.getWidth();
//		int height = width; Implicit - yIndex is bounded on width, assumes that the microscope outputs square images
		List<List<Integer>> twoDList = new ArrayList<List<Integer>>(width);
		for (int yIndex = 0; yIndex < width; yIndex++) {
			twoDList.add(createEmptyList(width));
		}

		for (int yIndex = 0; yIndex < width; yIndex++) {
			for (int xIndex = 0; xIndex < width; xIndex++) {
				int rgb = image.getRGB(xIndex, yIndex);
				twoDList.get(yIndex).set(xIndex, rgb);
			}
		}
		
		moveScaleBar(image, twoDList);

		BufferedImage moddedImage = DrawMyThing.createImage(twoDList);
		File outputDirObj = new File(outputDir);
		if(!outputDirObj.exists()) {
			outputDirObj.mkdir();
		}
		File output = new File(outputDir, inputFile.getName());
		ImageIO.write(moddedImage, "tif", output);
	}

	private static List<Integer> createEmptyList(int length) {
		List<Integer> newList = new ArrayList<Integer>(length);
		for (int i = 0; i < length; i++) {
			newList.add(0);
		}
		return newList;
	}
	
	private static void moveScaleBar(BufferedImage image, List<List<Integer>> twoDList) {
		int width = image.getWidth();
		int wordRightmostIndex = -1;
		int barRightmostIndex = -1;
		int barLeftmostIndex = -1;
		
		for(int xIndex = width-1; xIndex > 0; xIndex--) {
			int rgb = image.getRGB(xIndex, WORD_SCAN_HEIGHT);
			if(rgb <= -16777211) {
				wordRightmostIndex = xIndex;
				break;
			}
		}
		
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
		
		int rightmostIndex = wordRightmostIndex > barRightmostIndex ? wordRightmostIndex : barRightmostIndex;
		int scaleBarCutoutWidth = rightmostIndex - barLeftmostIndex + 4;
		for (int yIndex = SCALEBAR_TOP; yIndex < SCALEBAR_BOT; yIndex++) {
			int xCounter = 0;
			for (int xIndex = barLeftmostIndex - 2 ; xIndex < rightmostIndex + 2; xIndex++) {
				int rgb = image.getRGB(xIndex, yIndex);
				twoDList.get(yIndex - 85).set(image.getWidth() - scaleBarCutoutWidth + xCounter, rgb);
				xCounter++;
			}
		}
	}
}
