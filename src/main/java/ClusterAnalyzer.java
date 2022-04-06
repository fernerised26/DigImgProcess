package main.java;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class ClusterAnalyzer {
	
	private static final int[] MARKINGS = new int[]{Color.RED.getRGB(), Color.PINK.getRGB(), Color.ORANGE.getRGB(), Color.YELLOW.getRGB(), Color.GREEN.getRGB(), Color.MAGENTA.getRGB(), Color.CYAN.getRGB(), Color.BLUE.getRGB()};

	public static Map<Integer, Integer> analyzeClusters(PixelMeta[][] clusterPxls, Queue<PixelMeta> clusterCandidates, int width, int height, int[][] pictureArr, String filename) throws LogicException {
		List<Cluster> clustersList = new ArrayList<>();
		int idTracker = 0;
		while(!clusterCandidates.isEmpty()) {
			PixelMeta currPxl = clusterCandidates.poll();
			Set<PixelMeta> clusterSet = new HashSet<>(5000);
			Cluster cluster = new Cluster(clusterSet, Integer.valueOf(idTracker));
			FloodmakerCluster.floatFlood(clusterPxls, currPxl.getX(), currPxl.getY(), width, height, clusterCandidates, clusterSet);
			clustersList.add(cluster);
			idTracker++;
		}
		
		Map<Integer, Integer> countBySize = new TreeMap<Integer, Integer>();
		List<Integer> clusterSizes = new ArrayList<Integer>(clustersList.size());
		for(Cluster cluster : clustersList) {
			int currSize = cluster.getSetSize();
			clusterSizes.add(currSize);
			Integer currCount = countBySize.get(Integer.valueOf(currSize));
			if(currCount == null) {
				countBySize.put(currSize, Integer.valueOf(1));
			} else {
				countBySize.put(currSize, Integer.valueOf(currCount.intValue() + 1));
			}
		}
		
//		System.out.println(clusterSizes);
//		System.out.println(countBySize);
		
		//Visualization for cluster detection behavior, no current use
		
		/*Collections.sort(clustersList, new Comparator<Cluster>() {
			@Override
			public int compare(Cluster o1, Cluster o2) {
				if(o1.getSetSize() > o2.getSetSize()) {
					return -1;
				} else if(o1.getSetSize() == o2.getSetSize()) {
					return 0;
				} else {
					return 1;
				}
			}
			
		});
		
		Iterator<Cluster> clusterIter = clustersList.iterator();
		for(int i=0; i<MARKINGS.length; i++) {
			if(clusterIter.hasNext()) {
				Cluster currCluster = clusterIter.next();
				for(PixelMeta currPixel : currCluster.clusterSet) {
					pictureArr[currPixel.getY()][currPixel.getX()] = MARKINGS[i];
				}
			} else {
				break;
			}
		}
		
		BufferedImage imgout = DrawMyThing.createImage(pictureArr);
		
		File output = new File("testout\\"+filename.substring(0, filename.indexOf("."))+"_largest_clusters.tif");
		try {
			ImageIO.write(imgout, "tif", output);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return countBySize;
	}
	
	private static class Cluster {
		Set<PixelMeta> clusterSet;
		Integer id;
		int setSize = -1;

		Cluster(Set<PixelMeta> clusterSet, Integer id) {
			super();
			this.clusterSet = clusterSet;
			this.id = id;
		}
		
		int getSetSize() {
			if(setSize < 0) {
				setSize = clusterSet.size();
			}
			return setSize;
		}

	}
}
