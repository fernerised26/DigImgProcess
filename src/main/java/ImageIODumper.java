package main.java;
import javax.imageio.ImageIO;

public class ImageIODumper {

	public static void main(String[] args){
		String[] suffixes = ImageIO.getWriterFileSuffixes();
		String[] formatNames = ImageIO.getWriterFormatNames();
		
		System.out.println("Suffixes:");
		for(int i=0; i<suffixes.length ; i++){
			System.out.println(suffixes[i]);
		}
		
		System.out.println("Format Names:");
		for(int i=0; i<formatNames.length ; i++){
			System.out.println(formatNames[i]);
		}
	}
}
