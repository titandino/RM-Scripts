package com.makar.util;

import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;

public class Movement {

	public static void runToWithVariance(Locatable locatable, int variance) {
		if (locatable != null) {
			Area random = new Area.Rectangular(new Coordinate(locatable.getPosition().getX() - variance, locatable.getPosition().getY() - variance, 0), new Coordinate(locatable.getPosition().getX() + variance, locatable.getPosition().getY() + variance, 0));
			BresenhamPath path = BresenhamPath.buildTo(random.getRandomCoordinate());
			if (path != null) {
				path.step();
			}
		}
	}

}
