package main.java;

import java.awt.Color;

public class PixelMetaBorder extends PixelMeta{

	private int groupId = -1;
	
	public PixelMetaBorder(int x, int y, Color color, boolean isBoundary) {
		super(x, y, color, isBoundary);
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}

