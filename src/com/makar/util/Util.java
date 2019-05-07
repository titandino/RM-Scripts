package com.makar.util;

import com.runemate.game.api.hybrid.util.calculations.Random;

public class Util {

	public static int gaussian(int min, int max, int avg) {
		return ((int) Math.round(Random.nextGaussian(min, max, avg)));
	}
	
	public static int getLevelForXp(double xp) {
		double points = 0;
		double output = 0;
		for (int lvl = 1; lvl <= 120; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= xp) {
				return (int) lvl;
			}
		}
		return 120;
	}
}
