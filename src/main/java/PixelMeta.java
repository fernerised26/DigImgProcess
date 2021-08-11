package main.java;

import java.awt.Color;

public class PixelMeta {

	private int x;
	private int y;
	private Color color;
	private Color wrapGonColor;
	private Boolean isBoundary = null; 
	
	public PixelMeta(int x, int y, Color color) {
		super();
		this.x = x;
		this.y = y;
		this.color = color;
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
