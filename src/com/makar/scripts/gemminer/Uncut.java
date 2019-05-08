package com.makar.scripts.gemminer;

public enum Uncut {
	OPAL(1625, 15.0),
	JADE(1627, 20),
	RED_TOPAZ(1629, 25),
	SAPPHIRE(1623, 50),
	EMERALD(1621, 67),
	RUBY(1619, 85),
	DIAMOND(1617, 107.5),
	DRAGONSTONE(1631, 137.5);
	
	public static Uncut forId(int id) {
		for (Uncut u : Uncut.values()) {
			if (u.id == id)
				return u;
		}
		return null;
	}
	
	private int id;
	private double xp;
	
	private Uncut(int id, double xp) {
		this.id = id;
		this.xp = xp;
	}
	
	public int getId() {
		return id;
	}
	
	public double getXp() {
		return xp;
	}
}
