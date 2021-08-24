package main.java;

import java.awt.Color;

public class PixelMeta {

	private int x;
	private int y;
	private Color color;
	private Color wrapGonColor;
	private boolean isBoundary = false; 
	private boolean isFlooded = false;

	public PixelMeta(int x, int y, Color color, boolean isBoundary) {
		super();
		this.x = x;
		this.y = y;
		this.color = color;
		this.isBoundary = isBoundary;
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
		return isBoundary;
	}
	
	public boolean isFlooded() {
		return isFlooded;
	}

	public void markFlooded() {
		isFlooded = true;
	}
}
