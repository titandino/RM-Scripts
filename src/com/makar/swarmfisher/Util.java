package com.makar.swarmfisher;

import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Screen;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.util.calculations.Random;

public class Util {

	public static void runToAdvanced(Locatable o, Integer variance) {
		if (o != null) {
			Area random = new Area.Rectangular(new Coordinate(o.getPosition().getX() - variance, o.getPosition().getY() - variance, 0), new Coordinate(o.getPosition().getX() + variance, o.getPosition().getY() + variance, 0));
			BresenhamPath path = BresenhamPath.buildTo(random.getRandomCoordinate());
			if (path != null) {
				path.step();
			}
		}
	}

	public static int gaussian(int min, int max, int avg) {
		return ((int) Math.round(Random.nextGaussian(min, max, avg)));
	}

	public static void mouseOff(Integer chance) {
		double rand = Random.nextGaussian(0, 100, chance);
		if (rand < chance) {
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
