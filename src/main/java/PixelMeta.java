package main.java;

import java.awt.Color;

public class PixelMeta {

	private int x;
	private int y;
	private Color color;
	private Color wrapGonColor;
	private Boolean isBoundary = null; 
	private final Coordinate coord;
	
	public PixelMeta(int x, int y, Color color, Coordinate coord) {
		super();
		this.x = x;
		this.y = y;
		this.color = color;
		this.coord = coord;
	} 
	
	public Color getWrapGonColor() {
		return wrapGonColor;
	}

	public void setWrapGonColor(Color wrapGonColor) {
		this.wrapGonColor = wrapGonColor;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}
	
	public Coordinate getCoord() {
		return coord;
	}
	
	public boolean isBoundary() {
		if(isBoundary == null) {
			if(color.getRed() == color.getBlue() && color.getRed() == color.getGreen()) {
				isBoundary = Boolean.FALSE;
				return false;
			} else {
				isBoundary = Boolean.TRUE;
				return true;
			}	
		} else {
			return isBoundary.booleanValue();
		}
	}
}
