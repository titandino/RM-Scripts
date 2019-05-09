package com.makar.util;

import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Screen;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.hybrid.util.calculations.Random;

public class Antiban {

	public static void mouseOff(int chance) {
		if (Util.gaussian(0, 100, chance) < chance) {
			Mouse.move(new InteractablePoint(getOffScreenX(), getOffScreenY()));
		}
	}

	private static int getOffScreenX() {
		if (Random.nextBoolean()) {
			final int width = Screen.getBounds().width;
			return width + Random.nextInt(20, 50);
		} else {
			return 0 - Random.nextInt(20, 50);
		}
	}

	private static int getOffScreenY() {
		if (Random.nextBoolean()) {
			final int height = Screen.getBounds().height;
			return height + Random.nextInt(20, 50);
		} else {
			return 0 - Random.nextInt(20, 50);
		}
	}

}
