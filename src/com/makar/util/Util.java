package com.makar.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.runemate.game.api.hybrid.util.calculations.Random;

public class Util {

	public static int gaussian(int min, int max, int avg) {
		return ((int) Math.round(Random.nextGaussian(min, max, avg)));
	}
	
	public static<T> List<T> reverseList(List<T> list) {
		List<T> reverse = new ArrayList<>(list);
		Collections.reverse(reverse);
		return reverse;
	}
}
