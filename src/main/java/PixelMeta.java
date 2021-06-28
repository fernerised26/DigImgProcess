package main.java;

import java.awt.Color;

public class PixelMeta {

	private int x;
	private int y;
	private Color color;
	private Color wrapGonColor;
	
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
}
