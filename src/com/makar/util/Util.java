package com.makar.util;

import com.runemate.game.api.hybrid.util.calculations.Random;

public class Util {

	public static int gaussian(int min, int max, int avg) {
		return ((int) Math.round(Random.nextGaussian(min, max, avg)));
	}
}
